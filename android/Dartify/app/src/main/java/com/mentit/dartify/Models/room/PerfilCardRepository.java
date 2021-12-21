package com.mentit.dartify.Models.room;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.mentit.dartify.Models.PerfilCard;

import java.util.List;

public class PerfilCardRepository {
    private PerfilCardDao dao;
    private LiveData<List<PerfilCard>> data;
    private static Context context;
    private long userid;

    public PerfilCardRepository(Application app) {
        context = app;
        PerfilCardDatabase appdb = PerfilCardDatabase.getInstance(app);
        dao = appdb.datacardDAO();
    }

    public void insert(PerfilCard d) {
        new InsertDataCardAsyncTask(dao).execute(d);
    }

    public void update(PerfilCard d) {
        new UpdateDataCardAsyncTask(dao).execute(d);
    }

    public void delete(PerfilCard d) {
        new UpdateDataCardAsyncTask(dao).execute(d);
    }

    public void deleteAllData() {
        new DeleteAllDataAsyncTask(dao).execute();
    }

    public PerfilCard getPerfil(long userid) {
        PerfilCard p = dao.getPerfil(userid);
        return p;
    }

    public LiveData<List<PerfilCard>> getAllDataCards(long userid) {
        data = dao.getAllCards(userid);
        return data;
    }

    private static class InsertDataCardAsyncTask extends AsyncTask<PerfilCard, Void, Void> {
        private PerfilCardDao dc;

        private InsertDataCardAsyncTask(PerfilCardDao d) {
            this.dc = d;
        }

        @Override
        protected Void doInBackground(PerfilCard... dataCards) {
            dc.insert(dataCards[0]);
            return null;
        }
    }

    private static class UpdateDataCardAsyncTask extends AsyncTask<PerfilCard, Void, Void> {
        private PerfilCardDao dc;

        private UpdateDataCardAsyncTask(PerfilCardDao d) {
            this.dc = d;
        }

        @Override
        protected Void doInBackground(PerfilCard... dataCards) {
            dc.update(dataCards[0]);
            return null;
        }
    }

    private static class DeleteAllDataAsyncTask extends AsyncTask<PerfilCard, Void, Void> {
        private PerfilCardDao dc;

        private DeleteAllDataAsyncTask(PerfilCardDao d) {
            this.dc = d;
        }

        @Override
        protected Void doInBackground(PerfilCard... d) {
            try {
                dc.deleteAll();
            } catch (Exception w) {
                //Log.d("Exception", w.getMessage());
            }
            return null;
        }
    }
}
