package com.mentit.dartify.Models.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mentit.dartify.Models.NotificationCard;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface NotificationCardDao {
    @Insert
    void insert(NotificationCard c);

    @Update
    void update(NotificationCard c);

    @Delete
    void delete(NotificationCard c);

    @Query("DELETE FROM NotificationCard WHERE 1")
    void deleteAll();

    @Query("UPDATE NotificationCard SET leido = 1 WHERE userid2 = :userid")
    void readAll(long userid);

    @Query("SELECT * From NotificationCard WHERE userid1 = :userid1 AND userid2 = :userid2 AND tipo = :tipo")
    NotificationCard selectCard(long userid1, long userid2, int tipo);

    @Query("SELECT * FROM NotificationCard WHERE userid2 = :userid AND oculto = 0 ORDER BY fecha Desc")
    LiveData<List<NotificationCard>> getAllNotificationCards(long userid);

    @Query(" SELECT DISTINCT(userid1) FROM NotificationCard WHERE oculto = 1 AND userid2  = :userid ")
    List<Long> getOcultos(long userid);

    @Query("UPDATE NotificationCard SET oculto = 0 WHERE userid1 = :item ")
    void mostrar(Long item);

    @Query("UPDATE NotificationCard SET oculto = 1 WHERE userid1 IN(:items) ")
    void ocultar(ArrayList<Long> items);

}
