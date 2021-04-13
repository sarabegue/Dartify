package com.mentit.dartify.Models.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mentit.dartify.Models.EstadoCard;

import java.util.List;

@Dao
public interface EstadoCardDao {
    @Insert
    void insert(EstadoCard c);

    @Update
    void update(EstadoCard c);

    @Delete
    void delete(EstadoCard c);

    @Query("DELETE FROM EstadoCard WHERE 1")
    void deleteAll();

    @Query("SELECT * FROM EstadoCard WHERE userid1= :userid ORDER BY strFecha DESC LIMIT 5")
    LiveData<List<EstadoCard>> getAllCards(long userid);
}
