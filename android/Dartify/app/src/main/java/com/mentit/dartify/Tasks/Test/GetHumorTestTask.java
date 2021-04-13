package com.mentit.dartify.Tasks.Test;

import android.content.Context;
import android.os.AsyncTask;

import com.mentit.dartify.Models.Chiste;
import com.mentit.dartify.Network.NetworkResponse;
import com.mentit.dartify.Network.NetworkService;
import com.mentit.dartify.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetHumorTestTask extends AsyncTask<String, Void, String> {
    private Context context;
    private OnTaskCompleted taskCompleted;
    long userid;

    public GetHumorTestTask(Context activityContext, long userid) {
        context = activityContext;
        taskCompleted = (OnTaskCompleted) activityContext;
        this.userid = userid;
    }

    @Override
    protected String doInBackground(String... strings) {
        ejecutarConsulta(context.getString(R.string.quiz_chistes));
        return "";
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
                ArrayList<Chiste> lista = new ArrayList();

                try {
                    JSONArray datos = (JSONArray) respuesta.getDatos().get("datos");

                    for (int index = 0; index < datos.length(); index++) {
                        JSONObject o = (JSONObject) datos.get(index);
                        int cat = Integer.parseInt(o.get("cat").toString());
                        String strtexto = o.get("strtexto").toString();

                        lista.add(new Chiste(cat, strtexto));
                    }
                } catch (JSONException w) {
                }
                taskCompleted.OnTaskCompletedGetHumor(lista);
            }
        }).start();
    }

    public interface OnTaskCompleted {
        void OnTaskCompletedGetHumor(ArrayList<Chiste> lista);
    }
}