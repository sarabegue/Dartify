package com.mentit.dartify.Models.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mentit.dartify.Models.MatchCard;
import com.mentit.dartify.Models.room.MatchRepository;

import java.util.List;

public class MatchCardViewModel extends AndroidViewModel {
    private MatchRepository repository;
    private LiveData<List<MatchCard>> data;

    public MatchCardViewModel(@NonNull Application application) {
        super(application);
        repository = new MatchRepository(application);
    }

    public void insert(MatchCard c) {
        repository.insert(c);
    }

    public void deleteAll() {
        repository.deleteAllData();
    }

    public void visto(long userid2) {
        repository.visto(userid2);
    }

    public void getMatchData(long userid2) {
        repository.getMatchData(userid2);
    }

    public MatchCard getMatch(long userid) {
        return repository.getMatch(userid);
    }

    public LiveData<List<MatchCard>> getData(long userid) {
        data = repository.getAllDataCards(userid);
        return data;
    }

    public LiveData<List<MatchCard>> getDataCard(long userid2) {
        data = repository.getCard(userid2);
        return data;
    }
}
