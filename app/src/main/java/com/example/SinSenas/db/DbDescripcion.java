package com.example.SinSenas.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.SinSenas.Class.DescripcionTema;
import com.example.SinSenas.R;

import java.util.ArrayList;

public class DbDescripcion extends DbHelper {

    Context context;

    public DbDescripcion(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertDescription(String titulo,String descripcion, int id_Catego) {
        long id = 0;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("imagen", R.mipmap.ic_launcher);
            values.put("titulo", titulo);
            values.put("descripcion", descripcion);
            values.put("id_Catego", id_Catego);

            id = db.insert(TABLE_DESCRIPCION, null, values);
        } catch (Exception ex) {
            ex.toString();
        }
        return id;
    }
    public ArrayList<DescripcionTema> mostrarDescripcion(int idCate) {
        DbHelper dbHelper=new DbHelper(context);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ArrayList<DescripcionTema>ListaDescrip=new ArrayList<>();
        DescripcionTema descripcion=null;
        Cursor cursorDescrip=null;
        cursorDescrip=db.rawQuery("SELECT * FROM "+TABLE_DESCRIPCION+" WHERE id_Catego ="+idCate,null );
        if(cursorDescrip.moveToFirst()){
            do{
                descripcion=new DescripcionTema();
                descripcion.setId(cursorDescrip.getInt(0));
                descripcion.setImage(cursorDescrip.getInt(1));
                descripcion.setTitulo(cursorDescrip.getString(2));
                descripcion.setDescription(cursorDescrip.getString(3));
                descripcion.setId_Catego(cursorDescrip.getInt(4));
                ListaDescrip.add(descripcion);
            }while(cursorDescrip.moveToNext());
        }
        cursorDescrip.close();
        Log.i("", String.valueOf(ListaDescrip));
        return ListaDescrip;

    }

}
