package com.mentit.dartify.Models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.mentit.dartify.util.FormatUtil;

import java.io.Serializable;

@Entity(tableName = "MensajeChat", indices = {@Index(value = {"remoteId"}, unique = true)})
public class MensajeChat implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String remoteId;
    private long idRemitente;
    private long idDestinatario;
    private String strMensaje;
    private String strChatroom;
    private String strFecha;
    private String strResource1;
    private String strFirstname1;
    private String strResource2;
    private String strFirstname2;
    private boolean leido;
    private boolean fromme;
    private Boolean oculto = false;

    @Ignore
    public final int TIPO_FECHA = 0;

    @Ignore
    public final int TIPO_MENSAJE = 1;

    @Ignore
    public final int TIPO_STICKER = 2;

    @Ignore
    private Object drawable1 = 0;

    private int tipo;

    public MensajeChat() {
        this.oculto = false;
    }

    /*
                cviewmodel.guardarMensaje(fu.getChatRoom(userid1,userid2),userid1,userid2,v.getTag().toString(),2);
    public MensajeChat(DateTime hora) {
        tipo = TIPO_FECHA;
        this.hora = hora;
    }*/

    public MensajeChat(Object drawablePrincipal, String mensaje, boolean leido, boolean fromme) {
        tipo = TIPO_MENSAJE;
        this.drawable1 = drawablePrincipal;
        this.strMensaje = mensaje;
        this.leido = leido;
        this.fromme = fromme;
        this.oculto = false;
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

    public long getIdRemitente() {
        return idRemitente;
    }

    public void setIdRemitente(long idRemitente) {
        this.idRemitente = idRemitente;
    }

    public long getIdDestinatario() {
        return idDestinatario;
    }

    public void setIdDestinatario(long idDestinatario) {
        this.idDestinatario = idDestinatario;
    }

    public String getStrMensaje() {
        return strMensaje;
    }

    public void setStrMensaje(String strMensaje) {
        this.strMensaje = strMensaje;
    }

    public String getStrChatroom() {
        return strChatroom;
    }

    public void setStrChatroom(String strChatroom) {
        this.strChatroom = strChatroom;
    }

    public String getStrFecha() {
        return strFecha;
    }

    public void setStrFecha(String strFecha) {
        this.strFecha = strFecha;
    }

    public String getStrResource1() {
        return strResource1;
    }

    public void setStrResource1(String strResource1) {
        this.strResource1 = strResource1;
    }

    public String getStrFirstname1() {
        return strFirstname1;
    }

    public void setStrFirstname1(String strFirstname1) {
        this.strFirstname1 = strFirstname1;
    }

    public String getStrResource2() {
        return strResource2;
    }

    public void setStrResource2(String strResource2) {
        this.strResource2 = strResource2;
    }

    public String getStrFirstname2() {
        return strFirstname2;
    }

    public void setStrFirstname2(String strFirstname2) {
        this.strFirstname2 = strFirstname2;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    public boolean isFromme() {
        return fromme;
    }

    public void setFromme(boolean fromme) {
        this.fromme = fromme;
    }

    public int getTipo() {
        return tipo;
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

    public String getFormato(int t) {
        if (t == TIPO_FECHA) {
            return FormatUtil.getTiempo(this.getStrFecha());
        }
        if (t == TIPO_MENSAJE) {
            return FormatUtil.getHora(this.getStrFecha());
        }
        if (t == TIPO_STICKER) {
            return FormatUtil.getHora(this.getStrFecha());
        }
        return "?";
    }
}
