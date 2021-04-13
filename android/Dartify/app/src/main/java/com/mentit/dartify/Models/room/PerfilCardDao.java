package com.mentit.dartify.Models.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.mentit.dartify.Models.PerfilCard;

import java.util.List;

@Dao
public interface PerfilCardDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(PerfilCard c);

    @Update
    void update(PerfilCard c);

    @Delete
    void delete(PerfilCard c);

    @Query("DELETE FROM PerfilCard WHERE 1")
    void deleteAll();

    @Query("SELECT * FROM PerfilCard WHERE userid= :userid")
    PerfilCard getPerfil(long userid);

    @Query("SELECT * FROM PerfilCard WHERE userid= :userid")
    LiveData<List<PerfilCard>> getAllCards(long userid);
}
