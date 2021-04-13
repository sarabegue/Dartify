package com.mentit.dartify.Models;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "FotoUsuario", indices = {
        @Index(value = {"remoteId"}, unique = true),
        @Index(value = {"userid1"})
})
public class FotoUsuario {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long userid1;
    private String remoteId;
    private String resource1;
    private String strFecha;
    private int numorden;
    private int numtipo;
    private String texto1;
    private String texto2;
    private String texto3;

    public FotoUsuario(long userid1, String remoteId, String resource1, String strFecha, int numorden, int numtipo, String texto1, String texto2, String texto3) {
        this.userid1 = userid1;
        this.remoteId = remoteId;
        this.resource1 = resource1;
        this.strFecha = strFecha;
        this.numorden = numorden;
        this.texto1 = texto1;
        this.texto2 = texto2;
        this.texto3 = texto3;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserid1() {
        return userid1;
    }

    public void setUserid1(long userid1) {
        this.userid1 = userid1;
    }

    public String getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

    public String getResource1() {
        return resource1;
    }

    public void setResource1(String resource1) {
        this.resource1 = resource1;
    }

    public String getStrFecha() {
        return strFecha;
    }

    public void setStrFecha(String strFecha) {
        this.strFecha = strFecha;
    }

    public String getTexto1() {
        return texto1;
    }

    public void setTexto1(String texto1) {
        this.texto1 = texto1;
    }

    public String getTexto2() {
        return texto2;
    }

    public void setTexto2(String texto2) {
        this.texto2 = texto2;
    }

    public String getTexto3() {
        return texto3;
    }

    public void setTexto3(String texto3) {
        this.texto3 = texto3;
    }

    public int getNumorden() {
        return numorden;
    }

    public void setNumorden(int numorden) {
        this.numorden = numorden;
    }

    public int getNumtipo() {
        return numtipo;
    }

    public void setNumtipo(int numtipo) {
        this.numtipo = numtipo;
    }
}
