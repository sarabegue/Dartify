package com.mentit.dartify.Tasks.Match;

import android.content.Context;
import android.os.AsyncTask;

import com.mentit.dartify.BuildConfig;
import com.mentit.dartify.HelperClasses.SharedPreferenceUtils;
import com.mentit.dartify.Models.FavoriteCard;
import com.mentit.dartify.Models.PersonaOculta;
import com.mentit.dartify.Models.room.FavoriteCardDao;
import com.mentit.dartify.Models.room.FavoriteDatabase;
import com.mentit.dartify.Models.room.PersonaOcultaDao;
import com.mentit.dartify.Models.room.PersonaOcultaDatabase;
import com.mentit.dartify.Network.NetworkResponse;
import com.mentit.dartify.Network.NetworkService;
import com.mentit.dartify.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetUserFavoritesTask extends AsyncTask<String, Void, String> {
    private FavoriteCardDao dao;
    private PersonaOcultaDao daopo;
    private String query;
    private String strpath = BuildConfig.BASE_URL;
    private Context c;
    long userid;

    public GetUserFavoritesTask(Context context) {
        this.c = context;
        this.query = getQueryString();
        dao = FavoriteDatabase.getInstance(context).datacardDAO();
        daopo = PersonaOcultaDatabase.getInstance(context).personaOcultaDao();
    }

    private String getQueryString() {
        userid = SharedPreferenceUtils.getInstance(c).getLongValue(c.getString(R.string.user_id), 0);
        return c.getString(R.string.match_favorites) + userid;
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
                        long userid2;
                        String strsource, strfirstname, strfecha, strres1, strres2;

                        id = o.getString("id");
                        userid2 = o.getLong("userid");
                        strsource = strpath + o.getString("strsource");
                        strfirstname = o.getString("strfirstname");
                        strfecha = o.getString("datefechanacimiento");

                        try {
                            dao.insert(new FavoriteCard(id, strsource, strfirstname, strfecha, userid, userid2));
                        } catch (Exception x) {
                        }
                    }

                    /*
                     * Si los items ocultos no están en la lista nueva, mostrarlos
                     * Después ocultar todos los items de la lista nueva
                     * */

                    ArrayList<Long> listanuevos = new ArrayList<>();
                    List<Long> listabloqueados = dao.getOcultos(userid);

                    for (PersonaOculta p : daopo.getData(userid)) {
                        if (p.getBloqueado() || p.getDescartado()) {
                            listanuevos.add(p.getUserid2());
                        }
                    }

                    for (Long l : listabloqueados) {
                        if (!listanuevos.contains(l)) {
                            dao.mostrar(l);
                        }
                    }
                    dao.ocultar(listanuevos);
                } catch (Exception e) {

                }
            }
        }).start();
    }
}
