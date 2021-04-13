package com.mentit.dartify.Models;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.mentit.dartify.util.FormatUtil;

import java.io.Serializable;

@Entity(tableName = "FavoriteCard", indices = {@Index(value = {"longUserId1", "longUserId2"}, unique = true)})
public class FavoriteCard implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String strRemoteId;
    private String strResource;
    private String strFirstname;
    private String strFecha;
    private long longUserId1;
    private long longUserId2;
    private Boolean oculto = false;

    public FavoriteCard() {
        this.oculto = false;
    }

    @Ignore
    public FavoriteCard(String strRemoteId, String strResource, String strFirstname, String strFecha, long longUserId1, long longUserId2) {
        this.strRemoteId = strRemoteId;
        this.strResource = strResource;
        this.strFirstname = strFirstname;
        this.strFecha = strFecha;
        this.longUserId1 = longUserId1;
        this.longUserId2 = longUserId2;
        this.oculto = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStrRemoteId() {
        return strRemoteId;
    }

    public void setStrRemoteId(String strRemoteId) {
        this.strRemoteId = strRemoteId;
    }

    public String getStrResource() {
        return strResource;
    }

    public void setStrResource(String strResource) {
        this.strResource = strResource;
    }

    public String getStrFirstname() {
        return strFirstname;
    }

    public void setStrFirstname(String strFirstname) {
        this.strFirstname = strFirstname;
    }

    public String getStrFecha() {
        return strFecha;
    }

    public void setStrFecha(String strFecha) {
        this.strFecha = strFecha;
    }

    public long getLongUserId1() {
        return longUserId1;
    }

    public void setLongUserId1(long longUserId1) {
        this.longUserId1 = longUserId1;
    }

    public long getLongUserId2() {
        return longUserId2;
    }

    public void setLongUserId2(long longUserId2) {
        this.longUserId2 = longUserId2;
    }

    public Boolean getOculto() {
        return oculto;
    }

    public void setOculto(Boolean oculto) {
        this.oculto = oculto;
    }

    public int getEdad() {
        return FormatUtil.getEdad(this.getStrFecha());
    }
}
