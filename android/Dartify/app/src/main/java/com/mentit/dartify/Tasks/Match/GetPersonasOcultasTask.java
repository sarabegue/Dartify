package com.mentit.dartify.Tasks.Match;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mentit.dartify.BuildConfig;
import com.mentit.dartify.HelperClasses.SharedPreferenceUtils;
import com.mentit.dartify.Models.PersonaOculta;
import com.mentit.dartify.Models.room.PersonaOcultaDao;
import com.mentit.dartify.Models.room.PersonaOcultaDatabase;
import com.mentit.dartify.Network.NetworkResponse;
import com.mentit.dartify.Network.NetworkService;
import com.mentit.dartify.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetPersonasOcultasTask extends AsyncTask<String, Void, String> {
    private PersonaOcultaDao dao;
    private String query;
    private String strpath = BuildConfig.BASE_URL;
    private Context c;
    long userid;

    public GetPersonasOcultasTask(Context context, PersonaOcultaDatabase db) {
        this.c = context;
        this.query = getQueryString();
        dao = db.personaOcultaDao();
    }

    private String getQueryString() {
        userid = SharedPreferenceUtils.getInstance(c).getLongValue(c.getString(R.string.user_id), 0);
        return c.getString(R.string.match_hidden) + userid;
    }

    @Override
    protected String doInBackground(String... strings) {
        ejecutarConsulta(query);
        return null;
    }

    private void ejecutarConsulta(final String query) {
        new Thread(() -> {
            final List<String> credentials = new ArrayList<>();
            credentials.add("DARTIFY-API-KEY");
            credentials.add("REGISTRO");

            final NetworkResponse respuesta = NetworkService.INSTANCE.get(query, credentials);

            if (respuesta != null && !respuesta.error()) {
                try {
                    dao.deleteAll();
                    JSONArray datos = respuesta.getDatos().getJSONArray("datos");
                    for (int index = 0; index < datos.length(); index++) {
                        JSONObject o = (JSONObject) datos.get(index);

                        long userid1, userid2;
                        Boolean bloqueado, favorito, descartado;

                        userid1 = o.getLong("userid1");
                        userid2 = o.getLong("userid2");
                        bloqueado = o.getInt("numbloqueado") == 1 ? true : false;
                        favorito = o.getInt("numfavorito") == 1 ? true : false;
                        descartado = o.getInt("numdescartado") == 1 ? true : false;

                        try {
                            dao.insert(new PersonaOculta(userid1, userid2, bloqueado, favorito, descartado));
                        } catch (Exception x) {
                        }
                    }
                } catch (Exception e) {
                    Log.d("error", e.getMessage());
                }
            }
        }).start();
    }
}