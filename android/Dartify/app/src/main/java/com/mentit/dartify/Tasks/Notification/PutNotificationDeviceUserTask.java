package com.mentit.dartify.Tasks.Notification;

import android.content.Context;
import android.os.AsyncTask;

import com.mentit.dartify.Network.NetworkResponse;
import com.mentit.dartify.Network.NetworkService;
import com.mentit.dartify.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PutNotificationDeviceUserTask extends AsyncTask<String, Void, String> {
    private Context context;
    long userid;
    String strdevice;
    int numplataforma;

    public PutNotificationDeviceUserTask(Context activityContext, long userid, String strdevice, int plataforma) {
        context = activityContext;
        this.userid = userid;
        this.strdevice = strdevice;
        this.numplataforma = plataforma;
    }

    @Override
    protected String doInBackground(String... strings) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("userid", userid);
            jsonObject.put("deviceid", strdevice);
            jsonObject.put("numplataforma", numplataforma);
        } catch (JSONException e) {
            //Log.d("postbody", e.getMessage());
        }

        ejecutarConsulta(context.getString(R.string.notificacion_saveuserdevice), jsonObject);
        return "";
    }

    private void ejecutarConsulta(final String query, final JSONObject bodydata) {
        new Thread(() -> {
            final List<String> credentials = new ArrayList<>();
            credentials.add("DARTIFY-API-KEY");
            credentials.add("REGISTRO");

            final NetworkResponse respuesta = NetworkService.INSTANCE.put(query, bodydata, credentials);

            if (respuesta == null) {
                return;
            }

            if (!respuesta.error()) {
                try {
                    boolean status = respuesta.getDatos().getBoolean("datos");
                } catch (Exception w) {
                }
            }
        }).start();
    }
}