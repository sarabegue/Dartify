package com.mentit.dartify.Tasks.User;

import android.content.Context;
import android.os.AsyncTask;
//import android.util.Log;

import com.mentit.dartify.BuildConfig;
import com.mentit.dartify.Models.EstadoCard;
import com.mentit.dartify.Models.room.EstadoCardDao;
import com.mentit.dartify.Models.room.EstadoDatabase;
import com.mentit.dartify.Network.NetworkResponse;
import com.mentit.dartify.Network.NetworkService;
import com.mentit.dartify.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetUserEstadosTask extends AsyncTask<String, Void, String> {
    private EstadoCardDao dao;
    private String query;
    private String strpath = BuildConfig.BASE_URL;
    private Context c;
    private long userid;

    public GetUserEstadosTask(Context context, EstadoDatabase db, long userid) {
        this.c = context;
        this.userid = userid;
        this.query = getQueryString();
        dao = db.datacardDAO();
    }

    private String getQueryString() {
        return c.getString(R.string.usuario_muro) + userid;
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
                    JSONArray datos = respuesta.getDatos().getJSONArray("datos");

                    for (int index = 0; index < datos.length(); index++) {
                        JSONObject o = (JSONObject) datos.get(index);
                        String id;
                        long userid;
                        String strsource, strfecha, texto1, texto2;

                        id = o.getString("id");
                        userid = o.getLong("userid");
                        texto1 = o.getString("strfirstname");
                        texto2 = o.getString("strmensaje");
                        strsource = strpath + o.getString("resource1");
                        strfecha = o.getString("dateregistro");

                        try {
                            dao.insert(new EstadoCard(id, userid, texto1, texto2, "", strsource, strfecha, ""));
                        } catch (Exception x) {
                            //Log.d("E", x.toString());
                        }
                    }
                } catch (Exception e) {

                }
            }
        }).start();
    }
}