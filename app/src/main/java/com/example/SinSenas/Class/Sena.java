package com.example.SinSenas.Class;

import java.util.ArrayList;

public class Sena {
    int id;
    String sena;
    ArrayList<Punto> puntos;

    public Sena(int id, String sena, ArrayList<Punto> puntos) {
        this.id = id;
        this.sena = sena;
        this.puntos = puntos;
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

    public ArrayList<Punto> getPuntos() {
        return puntos;
    }

    public void setPuntos(ArrayList<Punto> puntos) {
        this.puntos = puntos;
    }
}