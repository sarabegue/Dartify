package com.mentit.dartify.Models.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mentit.dartify.Models.PersonaOculta;
import com.mentit.dartify.Models.room.PersonaOcultaRepository;

import java.util.List;

public class PersonaOcultaViewModel extends AndroidViewModel {
    private PersonaOcultaRepository repository;
    private LiveData<List<PersonaOculta>> data;

    public PersonaOcultaViewModel(@NonNull Application application) {
        super(application);
        repository = new PersonaOcultaRepository(application);
    }

    public void insert(PersonaOculta c) {
        repository.insert(c);
    }

    public void deleteAll() {
        repository.deleteAllData();
    }

    public void updatePosition(String remoteid, String numorden) {
        //repository.updatePosition(remoteid, numorden);
    }

    public LiveData<List<PersonaOculta>> getData() {
        data = repository.getData();
        return data;
    }
}
