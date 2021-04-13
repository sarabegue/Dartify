package com.mentit.dartify.Models;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.mentit.dartify.util.FormatUtil;

@Entity(tableName = "NotificationCard", indices = {@Index(value = {"userid1", "userid2", "tipo"}, unique = true)})
public class NotificationCard {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String remoteId;
    private String texto1;
    private String texto2;
    private String resource1;
    private String resource2;
    private String fecha = "";
    private int leido;
    private int tipo;
    private long userid1;
    private long userid2;
    private Boolean oculto = false;

    public NotificationCard(String remoteId, long userid1, long userid2, String texto1, String texto2, String resource1, String resource2, String fecha, int leido, int tipo) {
        this.remoteId = remoteId;
        this.texto1 = texto1;
        this.texto2 = texto2;
        this.resource1 = resource1;
        this.resource2 = resource2;
        this.fecha = fecha;
        this.leido = leido;
        this.tipo = tipo;
        this.userid1 = userid1;
        this.userid2 = userid2;
        this.oculto = false;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(String remoteid) {
        this.remoteId = remoteid;
    }

    public String getTexto1() {
        return texto1;
    }

    public String getTexto2() {
        return texto2;
    }

    public String getResource1() {
        return resource1;
    }

    public String getResource2() {
        return resource2;
    }

    public String getFecha() {
        return fecha;
    }

    public int getLeido() {
        return leido;
    }

    public int getTipo() {
        return tipo;
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

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setTexto1(String texto1) {
        this.texto1 = texto1;
    }

    public void setTexto2(String texto2) {
        this.texto2 = texto2;
    }

    public void setResource1(String resource1) {
        this.resource1 = resource1;
    }

    public void setResource2(String resource2) {
        this.resource2 = resource2;
    }

    public void setLeido(int leido) {
        this.leido = leido;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Boolean getOculto() {
        return oculto;
    }

    public void setOculto(Boolean oculto) {
        this.oculto = oculto;
    }

    public String getHora() {
        return FormatUtil.getTiempo(this.getFecha());
    }
}
