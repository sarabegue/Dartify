package com.mentit.dartify.Models;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.mentit.dartify.util.FormatUtil;

@Entity(tableName = "MatchCard", indices = {@Index(value = {"userid1", "userid2"}, unique = true)})
public class MatchCard {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String remoteId;
    private long userid1;

    private long userid2;
    private String texto1;
    private String texto2;
    private String texto3;
    private String resource1;
    private String resource2;
    private Boolean oculto;
    private int tipomatch = 1;

    private int visto;

    public MatchCard(String remoteId, long userid1, long userid2, String texto1, String texto2, String texto3, String resource1, String resource2, int visto, int tipomatch) {
        this.remoteId = remoteId;
        this.userid1 = userid1;
        this.userid2 = userid2;
        this.texto1 = texto1;
        this.texto2 = texto2;
        this.texto3 = texto3;
        this.resource1 = resource1;
        this.resource2 = resource2;
        this.visto = visto;
        this.oculto = false;
        this.tipomatch = tipomatch;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
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

    public String getResource1() {
        return resource1;
    }

    public void setResource1(String resource1) {
        this.resource1 = resource1;
    }

    public String getResource2() {
        return resource2;
    }

    public void setResource2(String resource2) {
        this.resource2 = resource2;
    }

    public int getVisto() {
        return visto;
    }

    public void setVisto(int visto) {
        this.visto = visto;
    }

    public int getEdad() {
        return FormatUtil.getEdad(this.getTexto2());
    }

    public Boolean getOculto() {
        return oculto;
    }

    public void setOculto(Boolean oculto) {
        this.oculto = oculto;
    }

    public void setTipomatch(int tipomatch) {
        this.tipomatch = tipomatch;
    }

    public int getTipomatch() {
        return this.tipomatch;
    }
}
