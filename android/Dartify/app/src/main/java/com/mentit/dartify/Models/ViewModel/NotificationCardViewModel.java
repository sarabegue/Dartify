package com.mentit.dartify.Models.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mentit.dartify.Models.NotificationCard;
import com.mentit.dartify.Models.room.NotificationDatabaseRepository;

import java.util.List;

public class NotificationCardViewModel extends AndroidViewModel {
    private NotificationDatabaseRepository repository;
    private LiveData<List<NotificationCard>> data;

    public NotificationCardViewModel(@NonNull Application application) {
        super(application);
        repository = new NotificationDatabaseRepository(application);
    }

    public void insert(NotificationCard c) {
        repository.insert(c);
    }

    public void deleteAll() {
        repository.deleteAllData();
    }

    public void readAll(long userid) {
        repository.readAllData(userid);
    }

    public LiveData<List<NotificationCard>> getData(long userid) {
        data = repository.getAllDataCards(userid);
        return data;
    }
}
