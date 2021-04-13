package com.mentit.dartify.Models.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mentit.dartify.Models.PersonaOculta;

import java.util.List;

@Dao
public interface PersonaOcultaDao {
    @Insert
    void insert(PersonaOculta mensaje);

    @Update
    void update(PersonaOculta mensaje);

    @Query("SELECT * FROM PersonaOculta WHERE userid1 = :userid" +
            " UNION " +
            "SELECT * FROM PersonaOculta WHERE userid2=:userid")
    LiveData<List<PersonaOculta>> getLiveData(long userid);

    @Query("SELECT userid1 AS userid1, userid2 AS userid2, bloqueado, favorito, descartado FROM PersonaOculta WHERE userid1 = :userid" +
            " UNION " +
           "SELECT userid2 AS userid1, userid1 AS userid2, bloqueado, favorito, descartado FROM PersonaOculta WHERE userid2 = :userid")
    List<PersonaOculta> getData(long userid);

    @Query("DELETE FROM PersonaOculta WHERE 1")
    void deleteAll();

}
