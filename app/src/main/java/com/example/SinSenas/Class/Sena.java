package com.example.SinSenas.Class;

import java.util.ArrayList;

public class Sena {
    int id;
    String sena;

    public Sena() {
    }
    public Sena(int id, String sena) {
        this.id = id;
        this.sena = sena;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSena() {
        return sena;
    }

    public void setSena(String sena) {
        this.sena = sena;
    }
}