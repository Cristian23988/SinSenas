package com.example.SinSenas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.SinSenas.db.DbCatego;
import com.example.SinSenas.Class.ModeloTemario;
import com.example.SinSenas.R;

import java.util.ArrayList;
import java.util.List;


public class Menu extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static final String EXTRA_MESSAGE = "com.example.sinsenas.MESSAGE";
    private List<ModeloTemario> mlista = new ArrayList<>();
    ListAdapterTemas mAdapter;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        /*Menu Nav*/
        Button btnHome = (Button) findViewById(R.id.btnHome);
        Button btnTraductor = (Button) findViewById(R.id.btnTraductor);
        Button btnAprednizaje = (Button) findViewById(R.id.btnAprendizaje);

        btnHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnAprednizaje.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, Menu.class);
                String subtitle = "Categorias";
                intent.putExtra(EXTRA_MESSAGE, subtitle);
                startActivity(intent);
            }
        });
        btnTraductor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, Translate.class);
                String subtitle = "Gestos a texto";
                intent.putExtra(EXTRA_MESSAGE, subtitle);
                startActivity(intent);
            }
        });


        /*Iniciailizar*/
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = findViewById(R.id.textViewTemas);
        textView.setText(message);
        mListView = (ListView) findViewById(R.id.listaDinamica);
        mListView.setOnItemClickListener(this);

        DbCatego dbCatego=new DbCatego(Menu.this);
        /*Añadir elementos a la lista de temas (nombre-imagen)*/
        /*Ejecución*/
        mAdapter = new ListAdapterTemas(Menu.this, R.layout.adapter_layout_temas, dbCatego.mostrarCategorias());
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        //Toast.makeText(Menu.this, "Has pulsado: " + position, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Menu.this, TemasAprend.class);
        String message = String.valueOf(position);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra("nombreItem", mAdapter.getItem(position).getNombre());
        intent.putExtra("IdItem", String.valueOf(position));

        startActivity(intent);
    }
}