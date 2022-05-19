package com.example.SinSenas.Class;

import com.google.common.primitives.Chars;

public class ModeloTemario {

    int id;
    private String nombre;
    private int imagen;
    private String estado;

    public ModeloTemario(String nombre, int imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }
    public ModeloTemario(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getLetraNombre() {
        return nombre.substring(0,1);
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}
