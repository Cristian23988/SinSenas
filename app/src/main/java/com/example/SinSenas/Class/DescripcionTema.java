package com.example.SinSenas.Class;

public class DescripcionTema {

    int id;
    int image;
    String titulo;
    String description;
    int id_Catego;

    public DescripcionTema(int image, String titulo, String description) {
        this.image = image;
        this.titulo = titulo;
        this.description = description;
    }

    public DescripcionTema() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId_Catego() {
        return id_Catego;
    }

    public void setId_Catego(int id_Catego) {
        this.id_Catego = id_Catego;
    }
}
