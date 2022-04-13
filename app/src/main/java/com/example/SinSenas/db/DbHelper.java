package com.example.SinSenas.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NOMBRE = "Aprendizaje.db";
    public static final String TABLE_CATEGO = "categorias";
    public static final String TABLE_DESCRIPCION = "descripcion";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_CATEGO + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "imagen INTEGER NOT NULL," +
                "nombre TEXT NOT NULL)");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_DESCRIPCION + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "imagen INTEGER NOT NULL," +
                "titulo TEXT NOT NULL," +
                "descripcion TEXT NOT NULL," +
                "id_Catego INTEGER)");
            }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_DESCRIPCION);
        onCreate(sqLiteDatabase);
    }
}
