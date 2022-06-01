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
import java.util.ArrayList;
import java.util.List;

// ContentResolver dependency
import com.example.SinSenas.Class.Punto;
import com.example.SinSenas.Class.Sena;
import com.example.SinSenas.databinding.TranslateActivityMainBinding;
import com.example.SinSenas.db.DbPunto;
import com.example.SinSenas.db.DbSena;
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

    /** Calcular distancia entre dos puntos*/
    private double calcularDistancia(double x1, double y1, double x2, double y2) {
        double diferenciaY = Math.abs(y2 - y1);
        double diferenciaX = Math.abs(x2 - x1);

        //Retorna la distancia eucladiana evitando desbordamiento si es muy grande el resultado.
        return Math.hypot(diferenciaY, diferenciaX);
    }

    /** Reconocer Seña
     * @return*/
    private String reconocerSena(HandsResult result) {
        //Referencia de puntos de los dedos
        int[] refBaseDedos = new int[]{0,2,5,9,13,17}; //{base, pulgar, indice, medio, anular, menique}
        int[] refDedos = new int[]{4,8,12,16,20}; //{pulgar, indice, medio, anular, menique}
        int[] refDedosA = new int[]{3,3,3,3,3,3,3,3,3,3,3,3,4,4,4,4,4,4,4,4,4,4,4,4,6,8,10,12,14,15};
        int[] refDedosB = new int[]{5,9,13,17,6,10,14,18,8,12,16,20,5,9,13,17,6,10,14,18,8,12,16,20,10,12,14,16,18,20};
        String mensaje = "";

        //Numero de manos
        int numHands = result.multiHandLandmarks().size();

        //Cargar datos de la mano en mensaje
        for (int i = 0; i < numHands; ++i) {
            //Lista de coordenada de los puntos
            List<LandmarkProto.NormalizedLandmark> handLandmarkList = result.multiHandLandmarks().get(i).getLandmarkList();

            ArrayList<Double> conexion = new ArrayList<Double>();
            double[] arriba = new double[5]; //Dedos arriba
            double distanciaDedos = 0.0;

            //Guardas dedos arriba
            for(int j = 0; j<5;j++){
                double distanciaBase = this.calcularDistancia(handLandmarkList.get(refBaseDedos[0]).getX(), handLandmarkList.get(refBaseDedos[0]).getY(), handLandmarkList.get(refBaseDedos[j+1]).getX(), handLandmarkList.get(refBaseDedos[j+1]).getY());
                double distanciaDedo = this.calcularDistancia(handLandmarkList.get(refBaseDedos[0]).getX(), handLandmarkList.get(refBaseDedos[0]).getY(), handLandmarkList.get(refDedos[j]).getX(), handLandmarkList.get(refDedos[j]).getY());
                //agrega los dedos que estan abajo o arriba
                if(distanciaDedo > distanciaBase ){
                    arriba[j] = refDedos[j]*1.0;
                }
            }

            //Guardar distancias entre dedos
            ArrayList<Punto> ListaConexiones = new ArrayList<>();
            Punto punto = null;
            for(int j = 0 ; j < refDedosA.length;j++) {
                distanciaDedos = this.calcularDistancia(handLandmarkList.get(refDedosA[j]).getX(), handLandmarkList.get(refDedosA[j]).getY(), handLandmarkList.get(refDedosB[j]).getX(), handLandmarkList.get(refDedosB[j]).getY());
                punto = new Punto();
                punto.setPuntoA(refDedosA[j] * 1.0);
                punto.setPuntoB(refDedosB[j] * 1.0);
                punto.setDistanciaMin(0.0);
                punto.setDistanciaMax(distanciaDedos);
                ListaConexiones.add(punto);
            }

            //comparar datos con la BD
            boolean isContinue = true;
            ArrayList<Punto> ListaPunto = new ArrayList<>();
            DbPunto dbpunto = new DbPunto(this);
            ArrayList<Sena> ListaSena = new ArrayList<>();
            DbSena dbsena = new DbSena(this);
            ListaSena = dbsena.mostrarSena();

            for(Sena sen : ListaSena) {
                ListaPunto = dbpunto.mostrarPunto(sen.getId());
                int cont = 1;
                for (Punto punt : ListaPunto) {
                    for (Punto conex : ListaConexiones) {
                        if(arriba[0] == punt.getIsUp() || arriba[1] == punt.getIsUp() || arriba[2] == punt.getIsUp() || arriba[3] == punt.getIsUp()) {
                            if (conex.getPuntoA() == punt.getPuntoA() && conex.getPuntoB() == punt.getPuntoB()) {
                                if (conex.getDistanciaMax() > punt.getDistanciaMin() && conex.getDistanciaMax() < punt.getDistanciaMax()) {
                                    cont++;
                                    break;
                                }
                                break;
                            }
                        }
                    }

                    if (cont == ListaPunto.size()) {
                        mensaje += sen.getSena();
                        isContinue = false;
                        break;
                    }
                }
                if(!isContinue){
                    break;
                }
            }
        }

        //Retornar mensaje reconocido
        if(mensaje == "" || mensaje == " " || mensaje == "  "){
            mensaje = null;
        }
        return mensaje;
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
                    String u = this.reconocerSena(handsResult);
                    if(u != null && u != "" && u != " " && u != "  "){
                        //Actualiza el chat despues de cargar imagen
                        handler.postDelayed(new Runnable() {
                            @SuppressLint("ResourceType")
                            public void run() {
                                //Crear TextView para ser agregado en el chat
                                    TextView chat = new TextView(Translate.this);
                                    chat.setText(u);
                                    chat.setId(1);
                                    chat.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    ((LinearLayout) linearLayout).addView(chat);
                                }
                        }, 50);
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
                    String u = this.reconocerSena(handsResult);
                    if(u != null && u != "" && u != " " && u != "  "){
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
                        }, 500);
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