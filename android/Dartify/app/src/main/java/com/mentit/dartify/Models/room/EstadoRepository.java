package com.mentit.dartify.Models.room;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.mentit.dartify.Models.EstadoCard;
import com.mentit.dartify.Tasks.User.GetUserEstadosTask;

import java.util.List;

public class EstadoRepository {
    private EstadoCardDao dao;
    private LiveData<List<EstadoCard>> data;
    private static Context context;
    private EstadoDatabase appdb;

    public EstadoRepository(Application app) {
        context = app;
        appdb = EstadoDatabase.getInstance(app);
        dao = appdb.datacardDAO();
    }

    public void insert(EstadoCard d) {
        new InsertDataCardAsyncTask(dao).execute(d);
    }

    public void update(EstadoCard d) {
        new UpdateDataCardAsyncTask(dao).execute(d);
    }

    public void delete(EstadoCard d) {
        new UpdateDataCardAsyncTask(dao).execute(d);
    }

    public void deleteAllData() {
        new DeleteAllDataAsyncTask(dao).execute();
    }

    public LiveData<List<EstadoCard>> getAllDataCards(long userid) {
        new GetUserEstadosTask(context, appdb, userid).execute("");
        data = dao.getAllCards(userid);
        return data;
    }

    private static class InsertDataCardAsyncTask extends AsyncTask<EstadoCard, Void, Void> {
        private EstadoCardDao dc;

        private InsertDataCardAsyncTask(EstadoCardDao d) {
            this.dc = d;
        }

        @Override
        protected Void doInBackground(EstadoCard... dataCards) {
            dc.insert(dataCards[0]);
            return null;
        }
    }

    private static class UpdateDataCardAsyncTask extends AsyncTask<EstadoCard, Void, Void> {
        private EstadoCardDao dc;

        private UpdateDataCardAsyncTask(EstadoCardDao d) {
            this.dc = d;
        }

        @Override
        protected Void doInBackground(EstadoCard... dataCards) {
            dc.update(dataCards[0]);
            return null;
        }
    }

    private static class DeleteAllDataAsyncTask extends AsyncTask<EstadoCard, Void, Void> {
        private EstadoCardDao dc;

        private DeleteAllDataAsyncTask(EstadoCardDao d) {
            this.dc = d;
        }

        @Override
        protected Void doInBackground(EstadoCard... d) {
            try {
                dc.delete(d[0]);
            } catch (Exception w) {
                //Log.d("Exception", w.getMessage());
            }
            return null;
        }
    }
}
