package com.example.SinSenas.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.SinSenas.Class.ModeloTemario;
import com.example.SinSenas.Class.Punto;
import com.example.SinSenas.Class.Sena;

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
            values.put("Y", y);
            values.put("idSena", idSena);
            id = db.insert(TABLE_PUNTO, null, values);
        } catch (Exception ex) {
            ex.toString();
        }
        return id;
    }

    public ArrayList<Punto> mostrarPunto(int idSena) {
        DbHelper dbHelper=new DbHelper(context);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ArrayList<Punto>ListaPunto=new ArrayList<>();
        Punto punto=null;
        Cursor cursorPunto=null;
        cursorPunto=db.rawQuery("SELECT * FROM "+TABLE_PUNTO+" WHERE idSena="+idSena,null );
        if(cursorPunto.moveToFirst()){
            do{
                punto=new Punto();
                punto.setId(cursorPunto.getInt(0));
                punto.setVectorX(cursorPunto.getFloat(1));
                punto.setVectorY(cursorPunto.getFloat(2));
                punto.setId(cursorPunto.getInt(3));
                ListaPunto.add(punto);
            }while(cursorPunto.moveToNext());
        }
        cursorPunto.close();
        Log.i("", String.valueOf(ListaPunto));
        return ListaPunto;
    }

}
