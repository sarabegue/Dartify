package com.mentit.dartify.Models.room;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.mentit.dartify.HelperClasses.SharedPreferenceUtils;
import com.mentit.dartify.Models.PersonaOculta;
import com.mentit.dartify.R;
import com.mentit.dartify.Tasks.Match.GetPersonasOcultasTask;

import java.util.List;

public class PersonaOcultaRepository {
    private PersonaOcultaDao dao;
    private LiveData<List<PersonaOculta>> data;
    private static Context context;
    private long userid;
    private PersonaOcultaDatabase appdb;

    public PersonaOcultaRepository(Application app) {
        context = app;
        appdb = PersonaOcultaDatabase.getInstance(app);
        dao = appdb.personaOcultaDao();
        userid = SharedPreferenceUtils.getInstance(context).getLongValue(context.getString(R.string.user_id), 0);

        descargarDatos();
    }

    public void descargarDatos() {
        new GetPersonasOcultasTask(context, appdb).execute();
    }

    public void insert(PersonaOculta d) {
        new InsertDataAsyncTask(dao).execute(d);
    }

    public void deleteAllData() {
        new DeleteAllDataAsyncTask(dao).execute();
    }

    public LiveData<List<PersonaOculta>> getData() {
        data = dao.getLiveData(userid);
        descargarDatos();
        return data;
    }

    private static class InsertDataAsyncTask extends AsyncTask<PersonaOculta, Void, Void> {
        private PersonaOcultaDao dc;

        private InsertDataAsyncTask(PersonaOcultaDao d) {
            this.dc = d;
        }

        @Override
        protected Void doInBackground(PersonaOculta... data) {
            dc.insert(data[0]);
            return null;
        }
    }

    private static class DeleteAllDataAsyncTask extends AsyncTask<PersonaOculta, Void, Void> {
        private PersonaOcultaDao dc;

        private DeleteAllDataAsyncTask(PersonaOcultaDao d) {
            this.dc = d;
        }

        @Override
        protected Void doInBackground(PersonaOculta... d) {
            try {
                dc.deleteAll();
            } catch (Exception w) {
                Log.d("Exception", w.getMessage());
            }
            return null;
        }
    }
}
