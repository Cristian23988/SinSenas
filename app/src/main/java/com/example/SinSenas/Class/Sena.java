package com.example.SinSenas.Class;

import java.util.ArrayList;

public class Mano {
    int id;
    String mano;
    ArrayList<Dedo> dedos;

    public Mano(int id, String mano, ArrayList<Dedo> dedos) {
        this.id = id;
        this.mano = mano;
        this.dedos = dedos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMano() {
        return mano;
    }

    public void setMano(String mano) {
        this.mano = mano;
    }

    public ArrayList<Dedo> getDedos() {
        return dedos;
    }

    public void setDedos(ArrayList<Dedo> dedos) {
        this.dedos = dedos;
    }
}