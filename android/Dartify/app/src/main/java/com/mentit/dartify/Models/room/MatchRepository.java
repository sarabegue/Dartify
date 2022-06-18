package com.mentit.dartify.Models.room;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.mentit.dartify.Models.MatchCard;
import com.mentit.dartify.Tasks.Match.GetMatchInfoTask;
import com.mentit.dartify.Tasks.Match.GetMatchesTask;

import java.util.List;

public class MatchRepository {
    private MatchCardDao dao;
    private LiveData<List<MatchCard>> data;
    private MatchDatabase appdb;
    private static Context context;
    private long userid;

    public MatchRepository(Application app) {
        context = app;
        appdb = MatchDatabase.getInstance(app);
        dao = appdb.datacardDAO();

        new GetMatchesTask(context).execute("");
    }

    public void insert(MatchCard d) {
        new InsertDataCardAsyncTask(dao).execute(d);
    }

    public void update(MatchCard d) {
        new UpdateDataCardAsyncTask(dao).execute(d);
    }

    public void delete(MatchCard d) {
        new UpdateDataCardAsyncTask(dao).execute(d);
    }

    public void deleteAllData() {
        new DeleteAllDataAsyncTask(dao).execute();
    }

    public void visto(long userid2) {
        new VistoAsyncTask(dao, userid2).execute();
    }

    public MatchCard getMatch(long userid2) {
        return dao.getMatch(userid2);
    }

    public LiveData<List<MatchCard>> getAllDataCards(long userid, int tipomatch) {
        data = dao.getAllCards(userid, tipomatch);
        return data;
    }

    public LiveData<List<MatchCard>> getCard(long userid2, int tipomatch) {
        data = dao.getAllCards(userid2, tipomatch);
        return data;
    }

    public void getMatchData(long userid2) {
        new GetMatchInfoTask(context, appdb, userid2).execute();
    }

    private static class InsertDataCardAsyncTask extends AsyncTask<MatchCard, Void, Void> {
        private MatchCardDao dc;

        private InsertDataCardAsyncTask(MatchCardDao d) {
            this.dc = d;
        }

        @Override
        protected Void doInBackground(MatchCard... dataCards) {
            dc.insert(dataCards[0]);
            return null;
        }
    }

    private static class UpdateDataCardAsyncTask extends AsyncTask<MatchCard, Void, Void> {
        private MatchCardDao dc;

        private UpdateDataCardAsyncTask(MatchCardDao d) {
            this.dc = d;
        }

        @Override
        protected Void doInBackground(MatchCard... dataCards) {
            dc.update(dataCards[0]);
            return null;
        }
    }

    private static class DeleteAllDataAsyncTask extends AsyncTask<MatchCard, Void, Void> {
        private MatchCardDao dc;

        private DeleteAllDataAsyncTask(MatchCardDao d) {
            this.dc = d;
        }

        @Override
        protected Void doInBackground(MatchCard... d) {
            try {
                dc.deleteAll();
            } catch (Exception w) {
                //Log.d("Exception", w.getMessage());
            }
            return null;
        }
    }

    private static class VistoAsyncTask extends AsyncTask<MatchCard, Void, Void> {
        private MatchCardDao dc;
        private long userid2;

        private VistoAsyncTask(MatchCardDao d, long userid2) {
            this.dc = d;
            this.userid2 = userid2;
        }

        @Override
        protected Void doInBackground(MatchCard... d) {
            try {
                dc.visto(this.userid2);
            } catch (Exception w) {
                //Log.d("Exception", w.getMessage());
            }
            return null;
        }
    }
}
