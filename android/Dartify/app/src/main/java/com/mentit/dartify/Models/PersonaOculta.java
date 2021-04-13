package com.mentit.dartify.Models;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.mentit.dartify.util.FormatUtil;

@Entity(tableName = "PersonaOculta", primaryKeys = {"userid1", "userid2"})
public class PersonaOculta {

    private long userid1;
    private long userid2;
    private Boolean bloqueado;
    private Boolean favorito;
    private Boolean descartado;

    public PersonaOculta(long userid1, long userid2, Boolean bloqueado, Boolean favorito, Boolean descartado) {
        this.userid1 = userid1;
        this.userid2 = userid2;
        this.bloqueado = bloqueado;
        this.favorito = favorito;
        this.descartado = descartado;
    }

    public long getUserid1() {
        return userid1;
    }

    public void setUserid1(long userid1) {
        this.userid1 = userid1;
    }

    public long getUserid2() {
        return userid2;
    }

    public void setUserid2(long userid2) {
        this.userid2 = userid2;
    }

    public Boolean getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(Boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public Boolean getFavorito() {
        return favorito;
    }

    public void setFavorito(Boolean favorito) {
        this.favorito = favorito;
    }

    public Boolean getDescartado() {
        return descartado;
    }

    public void setDescartado(Boolean descartado) {
        this.descartado = descartado;
    }
}
