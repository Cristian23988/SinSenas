package com.example.SinSenas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowInsets;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.SinSenas.databinding.ActivityFullscreenBinding;
import com.example.SinSenas.db.DbCatego;
import com.example.SinSenas.db.DbDescripcion;
import com.example.SinSenas.db.DbHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FullscreenActivity extends AppCompatActivity {
    /**
     * Variables full screen
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            if (Build.VERSION.SDK_INT >= 30) {
                mContentView.getWindowInsetsController().hide(
                        WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            } else {
                // Note that some of these constants are new as of API 16 (Jelly Bean)
                // and API 19 (KitKat). It is safe to use them, as they are inlined
                // at compile-time and do nothing on earlier devices.
                mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        }
    };
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    private ActivityFullscreenBinding binding;

    /**
     * Variables BD FIREBASE
     */
    private TextView fireRta;
    private Context contexto = FullscreenActivity.this;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mRooChild = databaseReference.child("texto");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFullscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fireRta = (TextView) findViewById(R.id.fullscreen_content);
        mContentView = binding.fullscreenContent;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Cambia a full screen
        delayedHide(5);

        //Cargar base de datos
        mRooChild.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //fireRta.setText("Cargando BD...");
                String texto = snapshot.getValue().toString();
                //fireRta.setText(texto);
                //fireRta.setTextColor(Color.RED);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                createDB();
                Intent intent = new Intent(contexto, MainActivity.class);
                startActivity(intent);
            }
        }, 5000);
    }

    /**
     * Funciones full screen
     */
    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    /**
     * Crea la base de datos
     */
    public void createDB() {
        DbHelper dbHelper = new DbHelper(contexto);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("categorias",null,null);
        db.delete("descripcion",null,null);
        if (db != null) {
            createDatosInicio();
        } else {
            Toast.makeText(contexto, "ERROR AL CREAR BD", Toast.LENGTH_LONG).show();
        }
    }

    public void createDatosInicio() {
        //---------------------------------CATEGORIAS
        String[] categorias = {"Diccionario", "Alfabeto", "Frases", "Expresiones"};
        DbCatego dbcatego = new DbCatego(contexto);

        for (int i = 0; i < categorias.length; i++) {
            dbcatego.insertCatego(categorias[i]);
        }

        //---------------------------------DESCRIPCION
        DbDescripcion dbDescripcion = new DbDescripcion(contexto);

        ////DICCIONARIO
        dbDescripcion.insertDescription("Buenos días", "Saludo basico empleado por gran parte de la poblacióna", 0);
        dbDescripcion.insertDescription("Buenas tardes", "Saludo basico empleado por gran parte de la poblacióna", 0);
        dbDescripcion.insertDescription("Buenas noches", "Saludo basico empleado por gran parte de la poblacióna", 0);
        dbDescripcion.insertDescription("Hasta luego", "Saludo basico empleado por gran parte de la poblacióna", 0);
        ////ALFABETO
        dbDescripcion.insertDescription("TITULO 2", "Saludo basico empleado por gran parte de la poblacióna", 1);
        dbDescripcion.insertDescription("TITULO 2", "Saludo basico empleado por gran parte de la poblacióna", 1);
        dbDescripcion.insertDescription("TITULO 2", "Saludo basico empleado por gran parte de la poblacióna", 1);
        dbDescripcion.insertDescription("TITULO 2", "Saludo basico empleado por gran parte de la poblacióna", 1);

        ////FRASES
        dbDescripcion.insertDescription("TITULO 3", "Saludo basico empleado por gran parte de la poblacióna", 2);
        dbDescripcion.insertDescription("TITULO 3", "Saludo basico empleado por gran parte de la poblacióna", 2);
        dbDescripcion.insertDescription("TITULO 3", "Saludo basico empleado por gran parte de la poblacióna",2);
        dbDescripcion.insertDescription("TITULO 3", "Saludo basico empleado por gran parte de la poblacióna", 2);

        ////EXPRESIONES
        dbDescripcion.insertDescription("TITULO 4", "Saludo basico empleado por gran parte de la poblacióna", 3);
        dbDescripcion.insertDescription("TITULO 4", "Saludo basico empleado por gran parte de la poblacióna", 3);
        dbDescripcion.insertDescription("TITULO 4", "Saludo basico empleado por gran parte de la poblacióna", 3);
        dbDescripcion.insertDescription("TITULO 4", "Saludo basico empleado por gran parte de la poblacióna", 3);
    }
}