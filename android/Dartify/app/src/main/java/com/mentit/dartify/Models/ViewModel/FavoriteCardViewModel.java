package com.mentit.dartify.Models.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mentit.dartify.Models.FavoriteCard;
import com.mentit.dartify.Models.room.FavoriteDatabaseRepository;

import java.util.List;

public class FavoriteCardViewModel extends AndroidViewModel {
    private FavoriteDatabaseRepository repository;
    private LiveData<List<FavoriteCard>> data;

    public FavoriteCardViewModel(@NonNull Application application) {
        super(application);
        repository = new FavoriteDatabaseRepository(application);
    }

    public void insert(FavoriteCard c) {
        repository.insert(c);
    }

    public void deleteAll() {
        repository.deleteAllData();
    }

    public void deleteFavorite(long userid1, long userid2) {
        repository.deleteFavorite(userid1, userid2);
    }

    public void addFavorite(long userid1, long userid2) {
        repository.addFavorite(userid1, userid2);
    }

    public LiveData<List<FavoriteCard>> getData(long userid1) {
        data = repository.getAllDataCards(userid1);
        return data;
    }
}
