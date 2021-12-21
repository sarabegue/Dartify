package com.mentit.dartify.Tasks.User;

import android.content.Context;
import android.os.AsyncTask;

import com.mentit.dartify.BuildConfig;
import com.mentit.dartify.Models.FotoUsuario;
import com.mentit.dartify.Models.room.FotoUsuarioDao;
import com.mentit.dartify.Models.room.FotoUsuarioDatabase;
import com.mentit.dartify.Network.NetworkResponse;
import com.mentit.dartify.Network.NetworkService;
import com.mentit.dartify.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetUserPhotosTask extends AsyncTask<String, Void, String> {
    private FotoUsuarioDao dao;
    private String query;
    private String strpath = BuildConfig.BASE_URL;
    private Context c;
    private long userid;

    public GetUserPhotosTask(Context context, FotoUsuarioDatabase db, long userid) {
        c = context;
        this.userid = userid;
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
        ejecutarConsulta(c.getString(R.string.usuario_fotografias) + userid);
        return "";
    }

    private void ejecutarConsulta(final String query) {
        new Thread(() -> {
            final List<String> credentials = new ArrayList<>();
            credentials.add("DARTIFY-API-KEY");
            credentials.add("REGISTRO");

            final NetworkResponse respuesta = NetworkService.INSTANCE.get(query, credentials);

            if (respuesta != null && !respuesta.error()) {
                try {
                    JSONArray datos = respuesta.getDatos().getJSONArray("fotosprincipales");

                    for (int index = 0; index < datos.length(); index++) {
                        JSONObject o = (JSONObject) datos.get(index);
                        String id;
                        long userid;
                        int numorden;
                        String strsource;

                        id = o.getString("id");
                        userid = o.getLong("userid");
                        numorden = o.getInt("numorden");
                        strsource = strpath + o.getString("strsource");

                        try {
                            dao.insert(new FotoUsuario(userid, id, strsource, "", numorden, 0, "", "", ""));
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