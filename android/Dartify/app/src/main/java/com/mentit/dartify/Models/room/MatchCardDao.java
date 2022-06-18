package com.mentit.dartify.Models.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mentit.dartify.Models.MatchCard;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface MatchCardDao {
    @Insert
    void insert(MatchCard c);

    @Update
    void update(MatchCard c);

    @Delete
    void delete(MatchCard c);

    @Query("DELETE FROM MatchCard WHERE 1")
    void deleteAll();

    @Query("UPDATE MatchCard SET visto = 1 WHERE userid2 = :userid2")
    void visto(long userid2);

    @Query("SELECT * FROM MatchCard WHERE userid1= :userid AND visto = 0 AND oculto = 0 AND (tipomatch BETWEEN 1 AND :tipomatch) ")
    LiveData<List<MatchCard>> getAllCards(long userid, int tipomatch);

    @Query("UPDATE MatchCard SET tipomatch = :tipomatch WHERE userid2 = :userid2")
    void tmatch(long userid2, int tipomatch);

    @Query("SELECT COUNT(*) FROM MatchCard")
    int getCount();

    @Query("SELECT * FROM MatchCard WHERE userid2= :userid2")
    LiveData<List<MatchCard>> getCard(long userid2);

    @Query("SELECT DISTINCT(userid2) FROM MatchCard WHERE oculto = 1 AND userid1 = :userid ")
    List<Long> getOcultos(long userid);

    @Query("UPDATE MatchCard SET oculto = 1 WHERE userid2 IN(:items)")
    void ocultar(ArrayList<Long> items);

    @Query("UPDATE MatchCard SET oculto = 1 WHERE userid2 = :item")
    void mostrar(Long item);

    @Query("SELECT * FROM MatchCard WHERE userid2= :userid2 LIMIT 1")
    MatchCard getMatch(long userid2);

}