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
        String[][] categorias = {{"Saludos","Activo"}, {"Alfabeto","Activo"},{"Diccionario","Inactivo"}, {"Expresiones","Inactivo"}};
        DbCatego dbcatego = new DbCatego(contexto);

        for (int i = 0; i < categorias.length; i++) {
            dbcatego.insertCatego(categorias[i][0], categorias[i][1]);
        }

        //---------------------------------DESCRIPCION
        DbDescripcion dbDescripcion = new DbDescripcion(contexto);

        ////Saludos
        dbDescripcion.insertDescription("Buenos días", R.drawable.apre_saludos_buenosdias, "Saludo basico empleado por gran parte de la población.", 0);
        dbDescripcion.insertDescription("Buenas tardes", R.drawable.apre_saludos_buenasnoches, "Saludo basico empleado por gran parte de la población", 0);
        dbDescripcion.insertDescription("Buenas noches", R.drawable.apre_saludos_buenastardes, "Saludo basico empleado por gran parte de la población", 0);
        dbDescripcion.insertDescription("Hola", R.drawable.apre_saludos_hola, "Saludo basico empleado por gran parte de la población", 0);

        ////ALFABETO
        dbDescripcion.insertDescription("Aa", R.drawable.apre_abc_a, "Saludo basico empleado por gran parte de la población", 1);
        dbDescripcion.insertDescription("Bb", R.drawable.apre_abc_b, "Saludo basico empleado por gran parte de la población", 1);
        dbDescripcion.insertDescription("Cc", R.drawable.apre_abc_c, "Saludo basico empleado por gran parte de la población", 1);
        dbDescripcion.insertDescription("Dd", R.drawable.apre_abc_d, "Saludo basico empleado por gran parte de la población", 1);
        dbDescripcion.insertDescription("Ee", R.drawable.apre_abc_e, "Saludo basico empleado por gran parte de la población", 1);
        dbDescripcion.insertDescription("Ff", R.drawable.apre_abc_f, "Saludo basico empleado por gran parte de la población", 1);
        dbDescripcion.insertDescription("Gg", R.drawable.apre_abc_g, "Saludo basico empleado por gran parte de la población", 1);
        dbDescripcion.insertDescription("Hh", R.drawable.apre_abc_h, "Saludo basico empleado por gran parte de la población", 1);
        dbDescripcion.insertDescription("Ii", R.drawable.apre_abc_i, "Saludo basico empleado por gran parte de la población", 1);
        dbDescripcion.insertDescription("Jj", R.drawable.apre_abc_j, "Saludo basico empleado por gran parte de la población", 1);
        dbDescripcion.insertDescription("Kk", R.drawable.apre_abc_k, "Saludo basico empleado por gran parte de la población", 1);
        dbDescripcion.insertDescription("Ll", R.drawable.apre_abc_l, "Saludo basico empleado por gran parte de la población", 1);
        dbDescripcion.insertDescription("Mm", R.drawable.apre_abc_m, "Saludo basico empleado por gran parte de la población", 1);
        dbDescripcion.insertDescription("Nn", R.drawable.apre_abc_n, "Saludo basico empleado por gran parte de la población", 1);
        dbDescripcion.insertDescription("Ññ", R.drawable.apre_abc_nn, "Saludo basico empleado por gran parte de la población", 1);
        dbDescripcion.insertDescription("Oo", R.drawable.apre_abc_o, "Saludo basico empleado por gran parte de la población", 1);
        dbDescripcion.insertDescription("Pp", R.drawable.apre_abc_p, "Saludo basico empleado por gran parte de la población", 1);
        dbDescripcion.insertDescription("Qq", R.drawable.apre_abc_q, "Saludo basico empleado por gran parte de la población", 1);
        dbDescripcion.insertDescription("Rr", R.drawable.apre_abc_r, "Saludo basico empleado por gran parte de la población", 1);
        dbDescripcion.insertDescription("Ss", R.drawable.apre_abc_s, "Saludo basico empleado por gran parte de la población", 1);
        dbDescripcion.insertDescription("Tt", R.drawable.apre_abc_t, "Saludo basico empleado por gran parte de la población", 1);
        dbDescripcion.insertDescription("Uu", R.drawable.apre_abc_u, "Saludo basico empleado por gran parte de la población", 1);
        dbDescripcion.insertDescription("Vv", R.drawable.apre_abc_v, "Saludo basico empleado por gran parte de la población", 1);
        dbDescripcion.insertDescription("Ww", R.drawable.apre_abc_w, "Saludo basico empleado por gran parte de la población", 1);
        dbDescripcion.insertDescription("Xx", R.drawable.apre_abc_x, "Saludo basico empleado por gran parte de la población", 1);
        dbDescripcion.insertDescription("Yy", R.drawable.apre_abc_y, "Saludo basico empleado por gran parte de la población", 1);
        dbDescripcion.insertDescription("Zz", R.drawable.apre_abc_z, "Saludo basico empleado por gran parte de la población", 1);

        ////DICCIONARIO
        dbDescripcion.insertDescription("TITULO 3", R.drawable.apre_abc_s, "Saludo basico empleado por gran parte de la población", 2);
        dbDescripcion.insertDescription("TITULO 3", R.drawable.apre_abc_a, "Saludo basico empleado por gran parte de la población", 2);
        dbDescripcion.insertDescription("TITULO 3", R.drawable.apre_abc_f, "Saludo basico empleado por gran parte de la población",2);
        dbDescripcion.insertDescription("TITULO 3", R.drawable.apre_abc_e, "Saludo basico empleado por gran parte de la población", 2);

        ////EXPRESIONES
        dbDescripcion.insertDescription("TITULO 4", R.drawable.apre_abc_h, "Saludo basico empleado por gran parte de la población", 3);
        dbDescripcion.insertDescription("TITULO 4", R.drawable.apre_abc_j, "Saludo basico empleado por gran parte de la población", 3);
        dbDescripcion.insertDescription("TITULO 4", R.drawable.apre_abc_k, "Saludo basico empleado por gran parte de la población", 3);
        dbDescripcion.insertDescription("TITULO 4", R.drawable.apre_abc_u, "Saludo basico empleado por gran parte de la población", 3);
    }
}