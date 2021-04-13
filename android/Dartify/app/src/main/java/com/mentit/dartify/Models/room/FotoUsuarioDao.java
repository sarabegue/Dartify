package com.mentit.dartify.Models.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mentit.dartify.Models.FotoUsuario;

import java.util.List;

@Dao
public interface FotoUsuarioDao {
    @Insert
    void insert(FotoUsuario c);

    @Update
    void update(FotoUsuario c);

    @Delete
    void delete(FotoUsuario c);

    @Query("DELETE FROM FotoUsuario WHERE 1")
    void deleteAll();

    @Query("UPDATE FotoUsuario SET numorden = :numorden WHERE id = :rid ")
    void updatePosition(int numorden, long rid);

    @Query("SELECT * FROM FotoUsuario WHERE userid1= :userid ORDER BY numorden ASC LIMIT 5")
    LiveData<List<FotoUsuario>> getAllCards(long userid);
}
