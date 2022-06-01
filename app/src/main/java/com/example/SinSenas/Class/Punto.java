package com.example.SinSenas.Class;

public class Punto {
    int id;
    int idSena;
    double isUp;
    double puntoA;
    double puntoB;
    double distanciaMin;
    double distanciaMax;

    public Punto(){}
    public Punto(int id, int idSena, double isUp, double puntoA, double puntoB, double distanciaMin, double distanciaMax) {
        this.id = id;
        this.idSena = idSena;
        this.isUp = isUp;
        this.puntoA = puntoA;
        this.puntoB = puntoB;
        this.distanciaMin = distanciaMin;
        this.distanciaMax = distanciaMax;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdSena() {
        return idSena;
    }

    public void setIdSena(int idSena) {
        this.idSena = idSena;
    }

    public double getIsUp() {
        return isUp;
    }

    public void setIsUp(double isUp) {
        this.isUp = isUp;
    }

    public double getPuntoA() {
        return puntoA;
    }

    public void setPuntoA(double puntoA) {
        this.puntoA = puntoA;
    }

    public double getPuntoB() {
        return puntoB;
    }

    public void setPuntoB(double puntoB) {
        this.puntoB = puntoB;
    }

    public double getDistanciaMin() {
        return distanciaMin;
    }

    public void setDistanciaMin(double distanciaMin) {
        this.distanciaMin = distanciaMin;
    }

    public double getDistanciaMax() {
        return distanciaMax;
    }

    public void setDistanciaMax(double distanciaMax) {
        this.distanciaMax = distanciaMax;
    }
}
