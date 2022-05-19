package com.example.SinSenas.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.SinSenas.Class.ModeloTemario;
import com.example.SinSenas.R;

import java.util.ArrayList;

public class DbCatego extends DbHelper {

    Context context;

    public DbCatego(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public boolean Vacia(){
        boolean rta;
        DbHelper dbHelper=new DbHelper(context);
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        return false;
    }
    public long insertCatego(String nombre, String estado) {
        long id = 0;
        boolean vacia=Vacia();
        if (vacia==false){
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("imagen", R.mipmap.ic_launcher);
            values.put("nombre", nombre);
            values.put("estado", estado);
            id = db.insert(TABLE_CATEGO, null, values);
        } catch (Exception ex) {
            ex.toString();
        }
            return id;
        }else{

        }
        return id;
    }

    public ArrayList<ModeloTemario> mostrarCategorias() {
        DbHelper dbHelper=new DbHelper(context);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ArrayList<ModeloTemario>ListaCatego=new ArrayList<>();
        ModeloTemario categorias=null;
        Cursor cursorCateg=null;
        cursorCateg=db.rawQuery("SELECT * FROM "+TABLE_CATEGO,null );
        if(cursorCateg.moveToFirst()){
            do{
                categorias=new ModeloTemario();
                categorias.setId(cursorCateg.getInt(0));
                categorias.setImagen(cursorCateg.getInt(1));
                categorias.setNombre(cursorCateg.getString(2));
                categorias.setEstado(cursorCateg.getString(3));
                ListaCatego.add(categorias);
            }while(cursorCateg.moveToNext());
        }
        cursorCateg.close();
        return ListaCatego;
    }

}