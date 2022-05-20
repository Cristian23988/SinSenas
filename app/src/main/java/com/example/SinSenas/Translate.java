package com.example.SinSenas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

// ContentResolver dependency
import com.example.SinSenas.databinding.TranslateActivityMainBinding;
import com.google.mediapipe.formats.proto.LandmarkProto;
import com.google.mediapipe.solutioncore.CameraInput;
import com.google.mediapipe.solutioncore.SolutionGlSurfaceView;
import com.google.mediapipe.solutioncore.VideoInput;
import com.google.mediapipe.solutions.hands.Hands;
import com.google.mediapipe.solutions.hands.HandsOptions;
import com.google.mediapipe.solutions.hands.HandsResult;


public class Translate extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.sinsenas.MESSAGE";
    private TranslateActivityMainBinding binding;
    private static final String TAG = "MainActivity";

    private Hands hands;
    // Run the pipeline and the model inference on GPU or CPU.
    private static final boolean RUN_ON_GPU = true;

    private enum InputSource {
        UNKNOWN,
        IMAGE,
        VIDEO,
        CAMERA,
    }
    private InputSource inputSource = InputSource.UNKNOWN;

    // Image demo UI and image loader components.
    private ActivityResultLauncher<Intent> imageGetter;
    private HandsResultImageView imageView;
    // Video demo UI and video loader components.
    private VideoInput videoInput;
    private ActivityResultLauncher<Intent> videoGetter;
    // Live camera demo UI and camera components.
    private CameraInput cameraInput;

    private SolutionGlSurfaceView<HandsResult> glSurfaceView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = TranslateActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*Menu Nav*/
        Button btnHome = (Button) findViewById(R.id.btnHome);
        Button btnAprendizaje = (Button) findViewById(R.id.btnAprendizaje);
        Button btnTraductor = (Button) findViewById(R.id.btnTraductor);

        btnHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Translate.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnAprendizaje.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Translate.this, Menu.class);
                startActivity(intent);
            }
        });
        btnTraductor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Translate.this, Translate.class);
                startActivity(intent);
            }
        });

        //Funciones de traduccion
        setupStaticImageDemoUiComponents();//Funcion imagen
        setupVideoDemoUiComponents();//Funcion video
        setupLiveDemoUiComponents();//Funcion tiempo real
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (inputSource == InputSource.CAMERA) {
            // Restarts the camera and the opengl surface rendering.
            cameraInput = new CameraInput(this);
            cameraInput.setNewFrameListener(textureFrame -> hands.send(textureFrame));
            glSurfaceView.post(this::startCamera);
            glSurfaceView.setVisibility(View.VISIBLE);
        } else if (inputSource == InputSource.VIDEO) {
            videoInput.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (inputSource == InputSource.CAMERA) {
            glSurfaceView.setVisibility(View.GONE);
            cameraInput.close();
        } else if (inputSource == InputSource.VIDEO) {
            videoInput.pause();
        }

    }

    private Bitmap downscaleBitmap(Bitmap originalBitmap) {
        double aspectRatio = (double) originalBitmap.getWidth() / originalBitmap.getHeight();
        int width = imageView.getWidth();
        int height = imageView.getHeight();
        if (((double) imageView.getWidth() / imageView.getHeight()) > aspectRatio) {
            width = (int) (height * aspectRatio);
        } else {
            height = (int) (width / aspectRatio);
        }
        return Bitmap.createScaledBitmap(originalBitmap, width, height, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Bitmap rotateBitmap(Bitmap inputBitmap, InputStream imageData) throws IOException {
        int orientation =
                new ExifInterface(imageData)
                        .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        if (orientation == ExifInterface.ORIENTATION_NORMAL) {
            return inputBitmap;
        }
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                break;
            default:
                matrix.postRotate(0);
        }
        return Bitmap.createBitmap(
                inputBitmap, 0, 0, inputBitmap.getWidth(), inputBitmap.getHeight(), matrix, true);

    }

    /** Sets up the UI components for the static image demo. */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setupStaticImageDemoUiComponents() {
        // The Intent to access gallery and read images as bitmap.
        imageGetter =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            Intent resultIntent = result.getData();
                            if (resultIntent != null) {
                                if (result.getResultCode() == RESULT_OK) {
                                    Bitmap bitmap = null;
                                    try {
                                        bitmap =
                                                downscaleBitmap(
                                                        MediaStore.Images.Media.getBitmap(
                                                                this.getContentResolver(), resultIntent.getData()));
                                    } catch (IOException e) {
                                        Log.e(TAG, "Bitmap reading error:" + e);
                                    }
                                    try {
                                        InputStream imageData =
                                                this.getContentResolver().openInputStream(resultIntent.getData());
                                        bitmap = rotateBitmap(bitmap, imageData);
                                    } catch (IOException e) {
                                        Log.e(TAG, "Bitmap rotation error:" + e);
                                    }
                                    if (bitmap != null) {
                                        hands.send(bitmap);
                                    }
                                }
                            }
                        });

        Button loadImageButton = findViewById(R.id.button_load_picture);
        loadImageButton.setOnClickListener(
                v -> {
                    if (inputSource != InputSource.IMAGE) {
                        stopCurrentPipeline();
                        //Ejecuta la funcion de reconomiento
                        setupStaticImageModePipeline();
                    }
                    // Reads images from gallery.
                    Intent pickImageIntent = new Intent(Intent.ACTION_PICK);
                    pickImageIntent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                    imageGetter.launch(pickImageIntent);

                });
        imageView = new HandsResultImageView(this);
    }

    /** Sets up core workflow for static image mode. */
    private void setupStaticImageModePipeline() {
        this.inputSource = InputSource.IMAGE;
        // Initializes a new MediaPipe Hands solution instance in the static image mode.
        hands =
                new Hands(
                        this,
                        HandsOptions.builder()
                                .setStaticImageMode(true)
                                .setMaxNumHands(2)
                                .setRunOnGpu(RUN_ON_GPU)
                                .build());

        // Connects MediaPipe Hands solution to the user-defined HandsResultImageView
        Handler handler = new Handler();
        hands.setResultListener(
                handsResult -> {
                    //Envia los datos de la imagen a la clase HandsResultImageView
                    imageView.setHandsResult(handsResult);
                    //Actualiza la imagen en la interfaz
                    runOnUiThread(() -> imageView.update());
        //--Mensaje CHAT---------------------------------------------
                    //Elementos del chat
                    View linearLayout =  findViewById(R.id.linearChat);
                    //String u = this.reconocerSena(handsResult);
                    String u = imageView.getMensaje();
                    if(u != null || u != ""){
                        //Actualiza el chat despues de cargar imagen
                        handler.postDelayed(new Runnable() {
                            @SuppressLint("ResourceType")
                            public void run() {
                                //Captura el mensaje que se ha guardado en HandsResultImageView
                                //String u = imageView.getMensaje();
                                //Crear TextView para ser agregado en el chat
                                    TextView chat = new TextView(Translate.this);
                                    chat.setText(u);
                                    chat.setId(1);
                                    chat.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    ((LinearLayout) linearLayout).addView(chat);
                                }
                        }, 500);
                    }
                });

        hands.setErrorListener((message, e) -> Log.e(TAG, "MediaPipe Hands error:" + message));
        // Updates the preview layout.
        FrameLayout frameLayout = findViewById(R.id.preview_display_layout);
        frameLayout.removeAllViewsInLayout();
        imageView.setImageDrawable(null);
        frameLayout.addView(imageView);
        imageView.setVisibility(View.VISIBLE);

    }

    /** Sets up the UI components for the video demo. */
    private void setupVideoDemoUiComponents() {
        // The Intent to access gallery and read a video file.
        videoGetter =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            Intent resultIntent = result.getData();
                            if (resultIntent != null) {
                                if (result.getResultCode() == RESULT_OK) {
                                    glSurfaceView.post(
                                            () ->
                                                    videoInput.start(
                                                            this,
                                                            resultIntent.getData(),
                                                            hands.getGlContext(),
                                                            glSurfaceView.getWidth(),
                                                            glSurfaceView.getHeight()));
                                }
                            }
                        });
        Button loadVideoButton = findViewById(R.id.button_load_video);
        loadVideoButton.setOnClickListener(

                v -> {
                    stopCurrentPipeline();
                    setupStreamingModePipeline(InputSource.VIDEO);
                    // Reads video from gallery.
                    Intent pickVideoIntent = new Intent(Intent.ACTION_PICK);
                    pickVideoIntent.setDataAndType(MediaStore.Video.Media.INTERNAL_CONTENT_URI, "video/*");
                    videoGetter.launch(pickVideoIntent);
                });

    }

    /** Sets up the UI components for the live demo with camera input. */
    private void setupLiveDemoUiComponents() {
        Button startCameraButton = findViewById(R.id.button_start_camera);
        startCameraButton.setOnClickListener(
                v -> {
                    if (inputSource == InputSource.CAMERA) {
                        return;
                    }
                    stopCurrentPipeline();
                    setupStreamingModePipeline(InputSource.CAMERA);
                });

    }

    /** Sets up core workflow for streaming mode. */
    private void setupStreamingModePipeline(InputSource inputSource) {
        this.inputSource = inputSource;
        // Initializes a new MediaPipe Hands solution instance in the streaming mode.
        hands =
                new Hands(
                        this,
                        HandsOptions.builder()
                                .setStaticImageMode(false)
                                .setMaxNumHands(2)
                                .setRunOnGpu(RUN_ON_GPU)
                                .build());
        hands.setErrorListener((message, e) -> Log.e(TAG, "MediaPipe Hands error:" + message));

        if (inputSource == InputSource.CAMERA) {
            cameraInput = new CameraInput(this);
            cameraInput.setNewFrameListener(textureFrame -> hands.send(textureFrame));
        } else if (inputSource == InputSource.VIDEO) {
            videoInput = new VideoInput(this);
            videoInput.setNewFrameListener(textureFrame -> hands.send(textureFrame));
        }

        HandsResultGlRenderer hRGIRenderer = new HandsResultGlRenderer();
        // Initializes a new Gl surface view with a user-defined HandsResultGlRenderer.
        glSurfaceView = new SolutionGlSurfaceView<>(this, hands.getGlContext(), hands.getGlMajorVersion());
        glSurfaceView.setSolutionResultRenderer(hRGIRenderer);
        glSurfaceView.setRenderInputImage(true);

        Handler handler = new Handler();

        hands.setResultListener(
            handsResult -> {
                if(!handsResult.multiHandLandmarks().isEmpty() || handsResult.multiHandLandmarks() != null){
                    glSurfaceView.setRenderData(handsResult);
                    glSurfaceView.requestRender();

                    //--Mensaje CHAT---------------------------------------------
                    String u = hRGIRenderer.getMensaje();
                    if(u != "" || u != " " || u != null){
                        View linearLayout =  findViewById(R.id.linearChat);
                        //Actualiza despues de cargar imagen
                        handler.postDelayed(new Runnable() {
                            @SuppressLint("ResourceType")
                            public void run() {
                                TextView chat = new TextView(Translate.this);
                                chat.setText(u);
                                chat.setId(1);
                                chat.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                ((LinearLayout) linearLayout).addView(chat);
                            }
                        }, 1000);
                    }
                }
            }
        );

        // The runnable to start camera after the gl surface view is attached.
        // For video input source, videoInput.start() will be called when the video uri is available.
        if (inputSource == InputSource.CAMERA) {
            glSurfaceView.post(this::startCamera);
        }

        // Updates the preview layout.
        FrameLayout frameLayout = findViewById(R.id.preview_display_layout);
        imageView.setVisibility(View.GONE);
        frameLayout.removeAllViewsInLayout();
        frameLayout.addView(glSurfaceView);
        glSurfaceView.setVisibility(View.VISIBLE);
        frameLayout.requestLayout();
    }

    private void startCamera() {
        cameraInput.start(
                this,
                hands.getGlContext(),
                CameraInput.CameraFacing.BACK,
                glSurfaceView.getWidth(),
                glSurfaceView.getHeight());

    }

    private void stopCurrentPipeline() {
        if (cameraInput != null) {
            cameraInput.setNewFrameListener(null);
            cameraInput.close();
        }
        if (videoInput != null) {
            videoInput.setNewFrameListener(null);
            videoInput.close();
        }
        if (glSurfaceView != null) {
            glSurfaceView.setVisibility(View.GONE);
        }
        if (hands != null) {
            hands.close();
        }

    }
}