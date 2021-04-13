package com.mentit.dartify.Models.room;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
//import android.util.Log;

import androidx.lifecycle.LiveData;

import com.mentit.dartify.Models.FotoUsuario;
import com.mentit.dartify.Tasks.User.GetUserPhotosTask;

import java.util.List;

public class FotoUsuarioRepository {
    private FotoUsuarioDao dao;
    private LiveData<List<FotoUsuario>> data;
    private static Context context;
    private FotoUsuarioDatabase appdb;

    public FotoUsuarioRepository(Application app) {
        context = app;
        appdb = FotoUsuarioDatabase.getInstance(app);
        dao = appdb.datacardDAO();
    }

    public void insert(FotoUsuario d) {
        new InsertDataCardAsyncTask(dao).execute(d);
    }

    public void update(FotoUsuario d) {
        new UpdateDataCardAsyncTask(dao).execute(d);
    }

    public void delete(FotoUsuario d) {
        new UpdateDataCardAsyncTask(dao).execute(d);
    }

    public void deleteAllData() {
        new DeleteAllDataAsyncTask(dao).execute();
    }

    public void updatePosition(String remoteid, String numorden) {
        new UpdatePositionAsyncTask(dao).execute(remoteid, numorden);
    }

    public LiveData<List<FotoUsuario>> getAllDataCards(long userid) {
        new GetUserPhotosTask(context, appdb, userid).execute("");
        data = dao.getAllCards(userid);
        return data;
    }

    private static class InsertDataCardAsyncTask extends AsyncTask<FotoUsuario, Void, Void> {
        private FotoUsuarioDao dc;

        private InsertDataCardAsyncTask(FotoUsuarioDao d) {
            this.dc = d;
        }

        @Override
        protected Void doInBackground(FotoUsuario... dataCards) {
            dc.insert(dataCards[0]);
            return null;
        }
    }

    private static class UpdateDataCardAsyncTask extends AsyncTask<FotoUsuario, Void, Void> {
        private FotoUsuarioDao dc;

        private UpdateDataCardAsyncTask(FotoUsuarioDao d) {
            this.dc = d;
        }

        @Override
        protected Void doInBackground(FotoUsuario... dataCards) {
            dc.update(dataCards[0]);
            return null;
        }
    }

    private static class DeleteAllDataAsyncTask extends AsyncTask<FotoUsuario, Void, Void> {
        private FotoUsuarioDao dc;

        private DeleteAllDataAsyncTask(FotoUsuarioDao d) {
            this.dc = d;
        }

        @Override
        protected Void doInBackground(FotoUsuario... d) {
            try {
                dc.delete(d[0]);
            } catch (Exception w) {
                //Log.d("Exception", w.getMessage());
            }
            return null;
        }
    }

    private static class UpdatePositionAsyncTask extends AsyncTask<String, Void, Void> {
        private FotoUsuarioDao dc;

        private UpdatePositionAsyncTask(FotoUsuarioDao d) {
            this.dc = d;
        }

        @Override
        protected Void doInBackground(String... params) {
            dc.updatePosition(Integer.parseInt(params[0]), Long.parseLong(params[1]));
            return null;
        }
    }
}
