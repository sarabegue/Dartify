package com.mentit.dartify.Tasks.Chat;

import android.content.Context;
import android.os.AsyncTask;
//import android.util.Log;

import com.mentit.dartify.BuildConfig;
import com.mentit.dartify.HelperClasses.SharedPreferenceUtils;
import com.mentit.dartify.Models.MensajeChat;
import com.mentit.dartify.Models.PersonaOculta;
import com.mentit.dartify.Models.room.MensajeChatDao;
import com.mentit.dartify.Models.room.MensajeDatabase;
import com.mentit.dartify.Models.room.PersonaOcultaDao;
import com.mentit.dartify.Models.room.PersonaOcultaDatabase;
import com.mentit.dartify.Network.NetworkResponse;
import com.mentit.dartify.Network.NetworkService;
import com.mentit.dartify.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetMessagesTask extends AsyncTask<String, Void, String> {
    private MensajeChatDao dao;
    private PersonaOcultaDao daopo;
    private String query;
    private String strpath = BuildConfig.BASE_URL;
    private Context c;
    long userid;
    private String mensajesTS;

    public GetMessagesTask(Context context) {
        c = context;

        userid = SharedPreferenceUtils.getInstance(c).getLongValue(c.getString(R.string.user_id), 0);
        mensajesTS = SharedPreferenceUtils.getInstance(c).getStringValue(c.getString(R.string.download_chat), "-");
        mensajesTS = "-";

        this.query = getQueryString();
        dao = MensajeDatabase.getInstance(context).mensajechatDAO();
        daopo = PersonaOcultaDatabase.getInstance(context).personaOcultaDao();
    }

    private String getQueryString() {
        return c.getString(R.string.chat_messages) + mensajesTS + "/" + userid;
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

            try {
                final NetworkResponse respuesta = NetworkService.INSTANCE.get(query, credentials);

                if (respuesta == null) {
                    return;
                }

                JSONArray json = ((JSONArray) (new JSONObject(respuesta.getDatos().toString()).get("datos")));
                String timestamp = (new JSONObject(respuesta.getDatos().toString()).get("ts")).toString();

                for (int index = 0; index < json.length(); index++) {
                    JSONObject jo = (JSONObject) json.get(index);
                    boolean fromme = false;
                    boolean unread = false;

                    long sender = jo.getLong("userid1");
                    long rece = jo.getLong("userid2");

                    if (sender == userid) {
                        fromme = true;
                    }

                    MensajeChat m = new MensajeChat();
                    m.setRemoteId(jo.getString("id"));
                    m.setIdRemitente(sender);
                    m.setIdDestinatario(rece);
                    m.setStrChatroom(jo.getString("strchatroom"));
                    m.setStrMensaje(jo.getString("strmensaje"));
                    m.setStrFirstname1(jo.getString("strfirstname1"));
                    m.setStrFirstname2(jo.getString("strfirstname2"));
                    m.setStrResource1(strpath + jo.getString("strsource1"));
                    m.setStrResource2(strpath + jo.getString("strsource2"));
                    m.setStrFecha(jo.get("datetimestamp").toString().replace(" ", "T"));
                    m.setFromme(fromme);
                    m.setTipo(jo.getInt("numtipomensaje"));

                    try {
                        MensajeChat tmp = dao.getMensaje(jo.getString("id"));
                        if (tmp == null) {
                            dao.insert(m);
                        } else {
                            tmp.setStrFecha(m.getStrFecha());
                            tmp.setStrResource1(m.getStrResource1());
                            tmp.setStrResource2(m.getStrResource2());
                            tmp.setStrFirstname1(m.getStrFirstname1());
                            tmp.setStrFirstname2(m.getStrFirstname2());
                            tmp.setFromme(fromme);
                            tmp.setTipo(m.getTipo());
                            tmp.setOculto(false);
                            dao.update(tmp);
                        }

                        /*
                         * Si los items ocultos no están en la lista nueva, mostrarlos
                         * Después ocultar todos los items de la lista nueva
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
                    } catch (Exception je) {
                    }
                }

                SharedPreferenceUtils.getInstance(c).setValue(c.getString(R.string.download_chat), timestamp);
            } catch (Exception je) {
                //Log.d("EXCEPCION", je.getMessage() + je.getStackTrace());
            }
        }).start();
    }
}