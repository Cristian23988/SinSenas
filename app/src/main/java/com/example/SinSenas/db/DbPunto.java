package com.example.SinSenas.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.SinSenas.Class.ModeloTemario;

import java.util.ArrayList;

public class DbPunto extends DbHelper {

    Context context;

    public DbPunto(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public boolean Vacia(){
        boolean rta;
        DbHelper dbHelper=new DbHelper(context);
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        return false;
    }

    public long insertPunto(float x,float y, int idSena) {
        long id = 0;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("x", x);
            values.put("y", y);
            values.put("idSena", idSena);
            id = db.insert(TABLE_DESCRIPCION, null, values);
        } catch (Exception ex) {
            ex.toString();
        }
        return id;
    }

    public ArrayList<ModeloTemario> mostrarSena() {
        DbHelper dbHelper=new DbHelper(context);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ArrayList<ModeloTemario>ListaSena=new ArrayList<>();
        ModeloTemario senas=null;
        Cursor cursorSena=null;
        cursorSena=db.rawQuery("SELECT * FROM "+TABLE_SENA,null );
        if(cursorSena.moveToFirst()){
            do{
                senas=new ModeloTemario();
                senas.setId(cursorSena.getInt(0));
                senas.setImagen(cursorSena.getInt(1));
                senas.setNombre(cursorSena.getString(2));
                ListaSena.add(senas);
            }while(cursorSena.moveToNext());
        }
        cursorSena.close();
        Log.i("", String.valueOf(ListaSena));
        return ListaSena;
    }

}
