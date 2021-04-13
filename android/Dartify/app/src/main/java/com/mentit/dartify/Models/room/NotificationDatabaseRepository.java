package com.mentit.dartify.Models.room;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.mentit.dartify.Models.NotificationCard;
import com.mentit.dartify.Tasks.Notification.GetNotificationsTask;

import java.util.List;

public class NotificationDatabaseRepository {
    private NotificationCardDao dao;
    private LiveData<List<NotificationCard>> data;
    private static Context context;

    public NotificationDatabaseRepository(Application app) {
        context = app;
        NotificationDatabase appdb = NotificationDatabase.getInstance(app);
        dao = appdb.datacardDAO();

        new GetNotificationsTask(context).execute();
    }

    public void insert(NotificationCard d) {
        new InsertDataCardAsyncTask(dao).execute(d);
    }

    public void update(NotificationCard d) {
        new UpdateDataCardAsyncTask(dao).execute(d);
    }

    public void delete(NotificationCard d) {
        new UpdateDataCardAsyncTask(dao).execute(d);
    }

    public void deleteAllData() {
        new DeleteAllDataAsyncTask(dao).execute();
    }

    public void readAllData(long userid) {
        new ReadAllDataAsyncTask(dao, userid).execute();
    }

    public LiveData<List<NotificationCard>> getAllDataCards(long userid) {
        data = dao.getAllNotificationCards(userid);
        return data;
    }

    private static class InsertDataCardAsyncTask extends AsyncTask<NotificationCard, Void, Void> {
        private NotificationCardDao dc;

        private InsertDataCardAsyncTask(NotificationCardDao d) {
            this.dc = d;
        }

        @Override
        protected Void doInBackground(NotificationCard... dataCards) {
            dc.insert(dataCards[0]);
            return null;
        }
    }

    private static class UpdateDataCardAsyncTask extends AsyncTask<NotificationCard, Void, Void> {
        private NotificationCardDao dc;

        private UpdateDataCardAsyncTask(NotificationCardDao d) {
            this.dc = d;
        }

        @Override
        protected Void doInBackground(NotificationCard... dataCards) {
            dc.update(dataCards[0]);
            return null;
        }
    }

    private static class DeleteAllDataAsyncTask extends AsyncTask<NotificationCard, Void, Void> {
        private NotificationCardDao dc;

        private DeleteAllDataAsyncTask(NotificationCardDao d) {
            this.dc = d;
        }

        @Override
        protected Void doInBackground(NotificationCard... d) {
            try {
                dc.deleteAll();
            } catch (Exception w) {
                Log.d("Exception", w.getMessage());
            }
            return null;
        }
    }

    private static class ReadAllDataAsyncTask extends AsyncTask<Void, Void, Void> {
        private NotificationCardDao dc;
        private long userid;

        private ReadAllDataAsyncTask(NotificationCardDao d, long userid) {
            this.dc = d;
            this.userid = userid;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dc.readAll(userid);
            return null;
        }
    }
}