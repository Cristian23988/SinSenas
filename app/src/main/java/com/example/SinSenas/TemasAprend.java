package com.example.SinSenas;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.SinSenas.db.DbDescripcion;
import com.example.SinSenas.Class.DescripcionTema;
import com.example.SinSenas.R;

import java.util.ArrayList;

public class TemasAprend extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ArrayList<DescripcionTema> ListaDescrip = new ArrayList<>();
    ViewPager2 viewPager2;
    ImageView flecha_imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temas_aprend);

        //Animacion
        final Animation animacion = AnimationUtils.loadAnimation(this, R.anim.move_right);
        final Animation animacion2 = AnimationUtils.loadAnimation(this, R.anim.to_transparent);
        flecha_imageview = findViewById(R.id.imageViewArrow);
        flecha_imageview.setAnimation(animacion);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                flecha_imageview.setAnimation(animacion2);
                new Handler().postDelayed(new Runnable() { @Override public void run() {flecha_imageview.setImageResource(0);} }, 2190);
            }
        }, 8450);

        /*RecibirTema*/
        Intent intent = getIntent();
        String message = intent.getStringExtra("nombreItem");
        TextView textView = findViewById(R.id.textViewTemas);
        textView.setText(message);

        int idItem = Integer.valueOf(intent.getStringExtra("IdItem"));

        DbDescripcion dbDescrip=new DbDescripcion(TemasAprend.this);
        ListaDescrip = dbDescrip.mostrarDescripcion(idItem);
        AdapterDescripcionTema adapterDescripcionTema = new AdapterDescripcionTema(ListaDescrip);

        viewPager2 = findViewById(R.id.viewpager);
        viewPager2.setAdapter(adapterDescripcionTema);//Quemar datos en la vista
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Toast.makeText(TemasAprend.this, "Has pulsado: " + position, Toast.LENGTH_LONG).show();
    }
}