package com.mentit.dartify.Models;

public class ElementoLista {
    int id = 0;
    String strnombre = "";
    String unidad = "";


    public ElementoLista() {

    }

    public ElementoLista(int id) {
        this.id = id;
        this.strnombre = id + "";
    }

    public ElementoLista(int id, String strnombre) {
        this.id = id;
        this.strnombre = strnombre;
    }

    public int getID() {
        return id;
    }

    public String getStrnombre() {
        return strnombre;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    @Override
    public String toString() {
        if (unidad.length() > 0) {
            return strnombre + " " + unidad;
        }
        return strnombre;
    }
}
