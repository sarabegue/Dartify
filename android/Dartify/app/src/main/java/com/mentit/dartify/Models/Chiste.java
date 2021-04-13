package com.mentit.dartify.Models;

import java.io.Serializable;

public class Chiste implements Serializable {
    private String chiste;
    private int categoria;

    public Chiste(int categoria, String chiste) {
        this.chiste = chiste;
        this.categoria = categoria;
    }

    public String getChiste() {
        return chiste;
    }

    public void setChiste(String chiste) {
        this.chiste = chiste;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }
}
