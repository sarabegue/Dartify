package com.mentit.dartify.Models.room;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.mentit.dartify.Models.FavoriteCard;
import com.mentit.dartify.Tasks.Match.GetUserFavoritesTask;
import com.mentit.dartify.Tasks.Match.PutDescartadoTask;
import com.mentit.dartify.Tasks.Match.PutUserFavoriteStatusTask;

import java.util.List;

public class FavoriteDatabaseRepository {
    private FavoriteCardDao dao;
    private LiveData<List<FavoriteCard>> data;
    private static Context context;
    private FavoriteDatabase appdb;

    public FavoriteDatabaseRepository(Application app) {
        context = app;
        appdb = FavoriteDatabase.getInstance(app);
        dao = appdb.datacardDAO();

        new GetUserFavoritesTask(context).execute();
    }

    public void insert(FavoriteCard d) {
        new InsertDataCardAsyncTask(dao).execute(d);
    }

    public void update(FavoriteCard d) {
        new UpdateDataCardAsyncTask(dao).execute(d);
    }

    public void deleteFavorite(long userid1, long userid2) {
        new DeleteFavoriteAsyncTask(dao, userid1, userid2).execute();
        new PutUserFavoriteStatusTask(context, userid1, userid2, 0).execute("");
        new PutDescartadoTask(context, userid1, userid2, 1).execute();
    }

    public void addFavorite(long userid1, long userid2) {
        new PutUserFavoriteStatusTask(context, userid1, userid2, 1).execute("");
        new GetUserFavoritesTask(context).execute();
        new PutDescartadoTask(context, userid1, userid2, 0).execute();
    }

    public void deleteAllData() {
        new DeleteAllDataAsyncTask(dao).execute();
    }

    public LiveData<List<FavoriteCard>> getAllDataCards(long userid1) {
        data = dao.getAllFavoriteCards(userid1);
        return data;
    }

    private static class InsertDataCardAsyncTask extends AsyncTask<FavoriteCard, Void, Void> {
        private FavoriteCardDao dc;

        private InsertDataCardAsyncTask(FavoriteCardDao d) {
            this.dc = d;
        }

        @Override
        protected Void doInBackground(FavoriteCard... dataCards) {
            dc.insert(dataCards[0]);
            return null;
        }
    }

    private static class UpdateDataCardAsyncTask extends AsyncTask<FavoriteCard, Void, Void> {
        private FavoriteCardDao dc;

        private UpdateDataCardAsyncTask(FavoriteCardDao d) {
            this.dc = d;
        }

        @Override
        protected Void doInBackground(FavoriteCard... dataCards) {
            dc.update(dataCards[0]);
            return null;
        }
    }

    private static class DeleteAllDataAsyncTask extends AsyncTask<FavoriteCard, Void, Void> {
        private FavoriteCardDao dc;

        private DeleteAllDataAsyncTask(FavoriteCardDao d) {
            this.dc = d;
        }

        @Override
        protected Void doInBackground(FavoriteCard... d) {
            try {
                dc.deleteAll();
            } catch (Exception w) {
                Log.d("Exception", w.getMessage());
            }
            return null;
        }
    }

    private static class DeleteFavoriteAsyncTask extends AsyncTask<Void, Void, Void> {
        private FavoriteCardDao dc;
        private long userid1;
        private long userid2;

        private DeleteFavoriteAsyncTask(FavoriteCardDao d, long userid1, long userid2) {
            this.dc = d;
            this.userid1 = userid1;
            this.userid2 = userid2;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dc.deleteFavorite(this.userid1, this.userid2);
            return null;
        }
    }
}
