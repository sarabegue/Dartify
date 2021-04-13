package com.mentit.dartify.Models.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mentit.dartify.Models.FavoriteCard;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface FavoriteCardDao {
    @Insert
    void insert(FavoriteCard c);

    @Update
    void update(FavoriteCard c);

    @Delete
    void delete(FavoriteCard c);

    @Query("DELETE FROM FavoriteCard WHERE 1")
    void deleteAll();

    @Query("DELETE FROM FavoriteCard WHERE longUserId1 = :userid1 AND longUserId2 = :userid2")
    void deleteFavorite(long userid1, long userid2);

    @Query("SELECT * FROM FavoriteCard WHERE longUserId1 = :userid1 AND oculto = 0 ORDER BY id DESC")
    LiveData<List<FavoriteCard>> getAllFavoriteCards(long userid1);

    @Query(" SELECT DISTINCT(longUserId2) FROM FavoriteCard WHERE oculto = 1 AND longUserId1  = :userid ")
    List<Long> getOcultos(long userid);

    @Query("UPDATE FavoriteCard SET oculto = 0 WHERE longUserId2 = :item ")
    void mostrar(Long item);

    @Query("UPDATE FavoriteCard SET oculto = 1 WHERE longUserId2 IN(:items) ")
    void ocultar(ArrayList<Long> items);
}
