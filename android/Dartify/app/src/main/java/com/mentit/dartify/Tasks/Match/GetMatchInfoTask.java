package com.mentit.dartify.Tasks.Match;

import android.content.Context;
import android.os.AsyncTask;

import com.mentit.dartify.BuildConfig;
import com.mentit.dartify.HelperClasses.SharedPreferenceUtils;
import com.mentit.dartify.Models.MatchCard;
import com.mentit.dartify.Models.room.MatchCardDao;
import com.mentit.dartify.Models.room.MatchDatabase;
import com.mentit.dartify.Network.NetworkResponse;
import com.mentit.dartify.Network.NetworkService;
import com.mentit.dartify.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetMatchInfoTask extends AsyncTask<String, Void, String> {
    private MatchCardDao dao;
    private String query;
    private String strpath = BuildConfig.BASE_URL;
    private Context c;
    private long userid;
    private long userid2;

    public GetMatchInfoTask(Context context, MatchDatabase db, long userid2) {
        this.c = context;
        this.userid2 = userid2;
        this.query = getQueryString();
        dao = db.datacardDAO();
    }

    private String getQueryString() {
        userid = SharedPreferenceUtils.getInstance(c).getLongValue(c.getString(R.string.user_id), 0);
        return c.getString(R.string.match_matchinfo) + userid + "/" + userid2;
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

            if (respuesta == null) {
                return;
            }

            if (respuesta != null || !respuesta.error()) {
                try {
                    JSONObject datos = respuesta.getDatos().getJSONObject("datos");

                    for (int index = 0; index < datos.length(); index++) {
                        try {
                            long userid2;
                            String strtexto1, strtexto2, strres1;

                            JSONObject p = datos.getJSONObject(datos.names().get(index).toString()).getJSONObject("perfil");
                            JSONArray f = datos.getJSONObject(datos.names().get(index).toString()).getJSONArray("fotosprincipales");

                            userid2 = p.getLong("id");
                            strtexto1 = p.getString("strfirstname");
                            strtexto2 = p.getString("datefechanacimiento");
                            strres1 = strpath + ((JSONObject) f.get(0)).getString("strsource");

                            dao.insert(new MatchCard(userid2 + "", userid, userid2, strtexto1, strtexto2, "", strres1, "", 0));
                        } catch (Exception x) {
                            //Log.d("EXCEPCION MATCH", x.getMessage() + x.getLocalizedMessage() + x.getStackTrace());
                        }
                    }
                } catch (Exception e) {
                    //Log.d("EXCEPTION", "-");
                }
            }
        }).start();
    }
}
