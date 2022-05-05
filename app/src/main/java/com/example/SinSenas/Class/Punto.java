package com.example.SinSenas.Class;

public class Punto {
    int id;
    float VectorX;
    float VectorY;

    public Punto(int id, float vectorX, float vectorY) {
        this.id = id;
        VectorX = vectorX;
        VectorY = vectorY;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getVectorX() {
        return VectorX;
    }

    public void setVectorX(float vectorX) {
        VectorX = vectorX;
    }

    public float getVectorY() {
        return VectorY;
    }

    public void setVectorY(float vectorY) {
        VectorY = vectorY;
    }
}
