package com.example.SinSenas;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.SinSenas.db.DbCatego;
import com.example.SinSenas.db.DbDescripcion;
import com.example.SinSenas.db.DbHelper;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.SinSenas.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AlertDialog.Builder showMessage = new AlertDialog.Builder(this);
        Button btnHome = (Button) findViewById(R.id.btnHome);
        Button btnTraductor = (Button) findViewById(R.id.btnTraductor);
        Button btnAprednizaje = (Button) findViewById(R.id.btnAprendizaje);

        btnHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnAprednizaje.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Menu.class);
                String subtitle = "Categorias";
                intent.putExtra(EXTRA_MESSAGE, subtitle);
                startActivity(intent);
            }
        });
        btnTraductor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Translate.class);
                String subtitle = "Gestos a texto";
                intent.putExtra(EXTRA_MESSAGE, subtitle);
                startActivity(intent);
            }
        });

        createDB();


    }

    public void createDB() {
        DbHelper dbHelper = new DbHelper(MainActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("categorias",null,null);
        db.delete("descripcion",null,null);
        if (db != null) {
          createDatos();
        } else {
            Toast.makeText(MainActivity.this, "ERROR AL CREAR BD", Toast.LENGTH_LONG).show();

        }
    }

    public void createDatos() {

        //---------------------------------CATEGORIAS
        String[] categorias = {"Diccionario", "Alfabeto", "Frases", "Expresiones"};
        DbCatego dbcatego = new DbCatego(MainActivity.this);

        for (int i = 0; i < categorias.length; i++) {
            dbcatego.insertCatego(categorias[i]);
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