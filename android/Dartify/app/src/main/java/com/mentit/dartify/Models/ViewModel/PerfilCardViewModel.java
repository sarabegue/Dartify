package com.mentit.dartify.Models.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mentit.dartify.Models.PerfilCard;
import com.mentit.dartify.Models.room.PerfilCardRepository;

import java.util.List;

public class PerfilCardViewModel extends AndroidViewModel {
    private PerfilCardRepository repository;
    private LiveData<List<PerfilCard>> data;

    public PerfilCardViewModel(@NonNull Application application) {
        super(application);
        repository = new PerfilCardRepository(application);
    }

    public void insert(PerfilCard c) {
        repository.insert(c);
    }

    public void deleteAll() {
        repository.deleteAllData();
    }

    public void update(PerfilCard p) {
        repository.update(p);
    }

    public PerfilCard getPerfil(long userid) {
        return repository.getPerfil(userid);
    }

    public LiveData<List<PerfilCard>> getData(long userid) {
        data = repository.getAllDataCards(userid);
        return data;
    }
}
