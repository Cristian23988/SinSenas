package com.example.SinSenas;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.SinSenas.db.DbCatego;
import com.example.SinSenas.db.DbDescripcion;
import com.example.SinSenas.db.DbHelper;
import com.example.SinSenas.R;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.mediapipe.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AlertDialog.Builder showMessage = new AlertDialog.Builder(this);
        Button btntraductor = (Button) findViewById(R.id.traductor);
        Button btnaprednizaje = (Button) findViewById(R.id.aprendizaje);
        //EditText textC = (EditText) findViewById(R.id.editTextTextPersonName);

        createDB();


        btnaprednizaje.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Menu.class);
                //EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
                //String message = editText.getText().toString();
                String subtitle = "Categorias";
                intent.putExtra(EXTRA_MESSAGE, subtitle);
                startActivity(intent);
            }
        });
        btntraductor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Translate.class);
                //EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
                String subtitle = "Gestos a texto";
                intent.putExtra(EXTRA_MESSAGE, subtitle);
                startActivity(intent);
            }
        });


    }

    public void createDB() {
        DbHelper dbHelper = new DbHelper(MainActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        createDatos();
        if (db != null) {
            // Toast.makeText(MainActivity.this, "Base de datos creada", Toast.LENGTH_LONG).show();

        } else {
            //   Toast.makeText(MainActivity.this, "ERROR AL CREAR BD", Toast.LENGTH_LONG).show();

        }
    }

    public void createDatos() {

        //---------------------------------CATEGORIAS
        String[] paises = {"Diccionario", "Alfabeto", "Frases", "Expresiones"};
        DbCatego dbcatego = new DbCatego(MainActivity.this);
        for (int i = 0; i < paises.length; i++) {
            dbcatego.insertCatego(paises[i]);
            Log.i("asd ",paises[i]);
        }


        //---------------------------------DESCRIPCION
       DbDescripcion dbDescripcion = new DbDescripcion(MainActivity.this);

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