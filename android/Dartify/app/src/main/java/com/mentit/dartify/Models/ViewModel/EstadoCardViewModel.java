package com.mentit.dartify.Models.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mentit.dartify.Models.EstadoCard;
import com.mentit.dartify.Models.room.EstadoRepository;

import java.util.List;

public class EstadoCardViewModel extends AndroidViewModel {
    private EstadoRepository repository;
    private LiveData<List<EstadoCard>> data;

    public EstadoCardViewModel(@NonNull Application application) {
        super(application);
        repository = new EstadoRepository(application);
    }

    public void insert(EstadoCard c) {
        repository.insert(c);
    }

    public void deleteAll() {
        repository.deleteAllData();
    }

    public LiveData<List<EstadoCard>> getData(long userid) {
        data = repository.getAllDataCards(userid);
        return data;

    }
}
