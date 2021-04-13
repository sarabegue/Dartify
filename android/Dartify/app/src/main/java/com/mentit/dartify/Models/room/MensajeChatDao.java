package com.mentit.dartify.Models.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mentit.dartify.Models.MensajeChat;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface MensajeChatDao {
    @Insert
    void insert(MensajeChat mensaje);

    @Update
    void update(MensajeChat mensaje);

    @Query("SELECT * FROM MensajeChat WHERE strChatroom = :chatRoom AND oculto = 0 ORDER BY ID ASC")
    LiveData<List<MensajeChat>> getAllMensajes(String chatRoom);

    @Query("SELECT * FROM MensajeChat WHERE remoteId = :remoteid ")
    MensajeChat getMensaje(String remoteid);

    @Query("SELECT " +
            "id, remoteId, strmensaje, strchatroom, strfecha, leido, fromme, tipo, oculto, " +
            "CASE fromme WHEN 1 THEN idRemitente WHEN 0 THEN idDestinatario END AS idRemitente," +
            "CASE fromme WHEN 0 THEN idRemitente WHEN 1 THEN idDestinatario END AS idDestinatario, " +
            "CASE fromme WHEN 1 THEN strFirstname1 WHEN 0 THEN strFirstname2 END AS strFirstname1, " +
            "CASE fromme WHEN 1 THEN strFirstname2 WHEN 0 THEN strFirstname1 END AS strFirstname2, " +
            "CASE fromme WHEN 1 THEN strResource1 WHEN 0 THEN strResource2 END AS strResource1, " +
            "CASE fromme WHEN 1 THEN strResource2 WHEN 0 THEN strResource1 END AS strResource2 " +
            "FROM MensajeChat " +
            "WHERE id IN ( " +
            "   SELECT id " +
            "   FROM MensajeChat " +
            "   WHERE idDestinatario = :userid OR idRemitente = :userid " +
            "   GROUP BY strchatroom " +
            "   HAVING MAX(strfecha) " +
            ") AND oculto = 0 " +
            "ORDER BY strfecha DESC ")
    LiveData<List<MensajeChat>> getChatList(long userid);

    @Query("DELETE FROM MensajeChat WHERE 1")
    void deleteAll();

    @Query(" SELECT DISTINCT(idDestinatario) FROM MensajeChat WHERE fromme = 1 AND oculto = 1 AND idRemitente    = :userid " +
            "UNION " +
            "SELECT DISTINCT(idRemitente)    FROM MensajeChat WHERE fromme = 0 AND oculto = 1 AND idDestinatario = :userid")
    List<Long> getOcultos(long userid);

    @Query("UPDATE MensajeChat SET oculto = 0 WHERE (idDestinatario IN(:item) OR idRemitente IN(:item) ) ")
    void mostrar(Long item);

    @Query("UPDATE MensajeChat SET oculto = 1 WHERE (idDestinatario IN(:items) OR idRemitente IN(:items) ) ")
    void ocultar(ArrayList<Long> items);

}