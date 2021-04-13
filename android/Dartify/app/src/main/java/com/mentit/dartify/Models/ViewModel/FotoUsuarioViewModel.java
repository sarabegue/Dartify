package com.mentit.dartify.Models.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mentit.dartify.Models.FotoUsuario;
import com.mentit.dartify.Models.room.FotoUsuarioRepository;

import java.util.List;

public class FotoUsuarioViewModel extends AndroidViewModel {
    private FotoUsuarioRepository repository;
    private LiveData<List<FotoUsuario>> data;

    public FotoUsuarioViewModel(@NonNull Application application) {
        super(application);
        repository = new FotoUsuarioRepository(application);
    }

    public void insert(FotoUsuario c) {
        repository.insert(c);
    }

    public void deleteAll() {
        repository.deleteAllData();
    }

    public void updatePosition(String remoteid, String numorden) {
        repository.updatePosition(remoteid, numorden);
    }

    public LiveData<List<FotoUsuario>> getData(long userid) {
        data = repository.getAllDataCards(userid);
        return data;
    }
}
