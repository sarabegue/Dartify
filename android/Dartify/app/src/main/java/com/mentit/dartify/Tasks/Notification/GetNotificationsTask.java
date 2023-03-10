package com.mentit.dartify.Tasks.Notification;

import android.content.Context;
import android.os.AsyncTask;

import com.mentit.dartify.BuildConfig;
import com.mentit.dartify.HelperClasses.SharedPreferenceUtils;
import com.mentit.dartify.Models.NotificationCard;
import com.mentit.dartify.Models.PersonaOculta;
import com.mentit.dartify.Models.room.NotificationCardDao;
import com.mentit.dartify.Models.room.NotificationDatabase;
import com.mentit.dartify.Models.room.PersonaOcultaDao;
import com.mentit.dartify.Models.room.PersonaOcultaDatabase;
import com.mentit.dartify.Network.NetworkResponse;
import com.mentit.dartify.Network.NetworkService;
import com.mentit.dartify.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetNotificationsTask extends AsyncTask<String, Void, String> {
    private NotificationCardDao dao;
    private PersonaOcultaDao daopo;
    private String query;
    private String strpath = BuildConfig.BASE_URL;
    private Context c;
    long userid;

    public GetNotificationsTask(Context context) {
        this.c = context;
        this.query = getQueryString();
        dao = NotificationDatabase.getInstance(context).datacardDAO();
        daopo = PersonaOcultaDatabase.getInstance(context).personaOcultaDao();
    }

    private String getQueryString() {
        userid = SharedPreferenceUtils.getInstance(c).getLongValue(c.getString(R.string.user_id), 0);
        return c.getString(R.string.notificacion_notificaciones) + userid;
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
                    JSONArray datos = respuesta.getDatos().getJSONArray("notificaciones");
                    for (int index = 0; index < datos.length(); index++) {
                        JSONObject o = (JSONObject) datos.get(index);
                        String id;
                        long userid1 = 0, userid2 = 0;
                        int tipo;
                        String strtitulo, strmensaje, strfecha, strres1, strres2;

                        id = o.getString("id");
                        tipo = o.getInt("numtipo");
                        userid1 = o.getLong("userid1");

                        try {
                            userid2 = o.getLong("userid2");
                        } catch (Exception e) {
                        }

                        strtitulo = o.getString("strtitulo");
                        strmensaje = o.getString("strmensaje");
                        strfecha = o.getString("datetimestamp");
                        strres1 = strpath + o.getString("resource1");
                        strres2 = strpath + o.getString("resource2");

                        try {
                            NotificationCard tmp = dao.selectCard(userid1, userid2, tipo);
                            if (tmp == null) {
                                dao.insert(new NotificationCard(id, userid1, userid2, strtitulo, strmensaje, strres1, strres2, strfecha, 0, tipo));
                            } else {
                                long n1 = Long.parseLong(tmp.getFecha().replace(" ", "").replace(":", "").replace("-", ""));
                                long n2 = Long.parseLong(strfecha.replace(" ", "").replace(":", "").replace("-", ""));

                                if (n1 < n2) {
                                    tmp.setFecha(strfecha);
                                    tmp.setTexto2(strmensaje);
                                    tmp.setLeido(0);

                                    dao.update(tmp);
                                }
                            }
                        } catch (Exception x) {
                        }

                        /*
                         * Si los items ocultos no est??n en la lista nueva, mostrarlos
                         * Despu??s ocultar todos los items de la lista nueva
                         * */

                        ArrayList<Long> listanuevos = new ArrayList<>();
                        List<Long> listabloqueados = dao.getOcultos(userid);

                        for (PersonaOculta p : daopo.getData(userid)) {
                            if(p.getBloqueado()) {
                                listanuevos.add(p.getUserid2());
                            }
                        }

                        for (Long l : listabloqueados) {
                            if (!listanuevos.contains(l)) {
                                dao.mostrar(l);
                            }
                        }
                        dao.ocultar(listanuevos);
                    }
                } catch (Exception e) {

                }
            }
        }).start();
    }
}