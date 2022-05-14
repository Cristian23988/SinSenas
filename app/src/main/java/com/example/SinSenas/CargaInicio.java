package com.example.SinSenas;

import com.example.SinSenas.Class.Sena;
import com.example.SinSenas.db.DbSena;
import com.google.firebase.database.DataSnapshot;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.SinSenas.db.DbCatego;
import com.example.SinSenas.db.DbDescripcion;
import com.example.SinSenas.db.DbHelper;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CargaInicio extends AppCompatActivity {

    private TextView fireRta;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    DatabaseReference mRooChild=databaseReference.child("texto");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga_inicio);
        //fireRta=(TextView) findViewById(R.id.rtaFirebase);
        Continuar(5000);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mRooChild.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //fireRta.setText("Cargando BD...");
                String texto = snapshot.getValue().toString();
                /*fireRta.setText(texto);
                fireRta.setTextColor(Color.RED);*/

                }

           // }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void Continuar(int milisegundos) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                createDB();
                Intent intent = new Intent(CargaInicio.this, MainActivity.class);
                startActivity(intent);
            }
        }, milisegundos);
    }
    public void createDB() {
        DbHelper dbHelper = new DbHelper(CargaInicio.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("categorias",null,null);
        db.delete("descripcion",null,null);
        if (db != null) {
            createDatosInicio();
        } else {
            Toast.makeText(CargaInicio.this, "ERROR AL CREAR BD", Toast.LENGTH_LONG).show();

        }
    }

    public void createDatosInicio() {
        //---------------------------------CATEGORIAS
        String[] categorias = {"Diccionario", "Alfabeto", "Frases", "Expresiones"};
        DbCatego dbcatego = new DbCatego(CargaInicio.this);

        for (int i = 0; i < categorias.length; i++) {
            dbcatego.insertCatego(categorias[i]);
        }


        //---------------------------------DESCRIPCION
        DbDescripcion dbDescripcion = new DbDescripcion(CargaInicio.this);

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