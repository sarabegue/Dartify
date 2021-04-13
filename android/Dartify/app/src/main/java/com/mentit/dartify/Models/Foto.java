package com.mentit.dartify.Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Foto implements Serializable {
    private long id = 0;
    private String strsource = "";
    private int numancho = 0;
    private int numalto = 0;

    public Foto() {
    }

    public Foto(JSONObject fobject) {
        try {
            id = fobject.getLong("id");
            setSource(fobject.getString("strsource"));
            setAncho(fobject.getInt("numancho"));
            setAlto(fobject.getInt("numalto"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public long getID() {
        return id;
    }

    public String getSource() {
        return strsource;
    }

    public void setSource(String strsource) {
        this.strsource = strsource;
    }

    public int getAncho() {
        return numancho;
    }

    public void setAncho(int numancho) {
        this.numancho = numancho;
    }

    public int getAlto() {
        return numalto;
    }

    public void setAlto(int numalto) {
        this.numalto = numalto;
    }
}
