package com.example.SinSenas.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.SinSenas.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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
        sqLiteDatabase.execSQL(DATABASE_TABLE_CATEGO);
        sqLiteDatabase.execSQL(DATABASE_TABLE_DESCRIPCION);
    }

    public static final String DATABASE_TABLE_CATEGO = "CREATE TABLE " + TABLE_CATEGO + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "imagen INTEGER NOT NULL," +
            "nombre TEXT NOT NULL)";

    public static final String DATABASE_TABLE_DESCRIPCION = "CREATE TABLE " + TABLE_DESCRIPCION + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "imagen INTEGER NOT NULL," +
            "titulo TEXT NOT NULL," +
            "descripcion TEXT NOT NULL," +
            "id_Catego INTEGER)";



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DESCRIPCION);
        onCreate(db);
    }
}
