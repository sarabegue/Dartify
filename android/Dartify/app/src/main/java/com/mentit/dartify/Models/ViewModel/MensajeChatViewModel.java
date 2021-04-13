package com.mentit.dartify.Models.ViewModel;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mentit.dartify.Models.MensajeChat;
import com.mentit.dartify.Models.room.MensajeDatabaseRepository;

import java.util.List;

public class MensajeChatViewModel extends AndroidViewModel {
    private MensajeDatabaseRepository repository;
    private LiveData<List<MensajeChat>> data;
    private LiveData<List<MensajeChat>> dataChatList;

    public MensajeChatViewModel(@NonNull Application application) {
        super(application);
        repository = new MensajeDatabaseRepository(application);
    }

    public void guardarMensaje(String room, long userid1, long userid2, String msg, int numtipo) {
        repository.guardarMensaje(room, userid1, userid2, msg, numtipo);
    }

    public void descargarMensajes() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                repository.descargarMensajes();
            }
        }, 500);
    }

    public void joinRoom(String room) {
        repository.joinRoom(room);
    }

    public void insert(MensajeChat c) {
        repository.insert(c);
    }

    public void deleteAll() {
        repository.deleteAllData();
    }

    public LiveData<List<MensajeChat>> getData(String chatRoom) {
        data = repository.getAllMensajes(chatRoom);
        return data;
    }

    public LiveData<List<MensajeChat>> getChatList(long userid) {
        dataChatList = repository.getChatList(userid);
        return dataChatList;
    }
}