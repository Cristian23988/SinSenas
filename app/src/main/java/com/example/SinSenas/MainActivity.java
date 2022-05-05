package com.example.SinSenas;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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

    }
    @Override
    public void onBackPressed() {

    }
}