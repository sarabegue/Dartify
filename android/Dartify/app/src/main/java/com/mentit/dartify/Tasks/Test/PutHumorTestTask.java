package com.mentit.dartify.Tasks.Test;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mentit.dartify.Network.NetworkResponse;
import com.mentit.dartify.Network.NetworkService;
import com.mentit.dartify.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PutHumorTestTask extends AsyncTask<String, Void, String> {
    private Context context;
    private OnTaskCompleted taskCompleted;
    long userid;
    ArrayList<String> lista;

    public PutHumorTestTask(Context activityContext, long userid, ArrayList lista) {
        context = activityContext;
        taskCompleted = (OnTaskCompleted) activityContext;
        this.userid = userid;
        this.lista = lista;
    }

    @Override
    protected String doInBackground(String... strings) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("userid", userid);
            jsonObject.put("lista", lista);

        } catch (JSONException e) {
            Log.d("postbody", e.getMessage());
        }

        ejecutarConsulta(context.getString(R.string.quiz_chistes), jsonObject);
        return "";
    }

    private void ejecutarConsulta(final String query, final JSONObject bodydata) {
        new Thread(() -> {
            final List<String> credentials = new ArrayList<>();
            credentials.add("DARTIFY-API-KEY");
            credentials.add("REGISTRO");

            final NetworkResponse respuesta = NetworkService.INSTANCE.put(query, bodydata, credentials);
            if (!respuesta.error()) {
                taskCompleted.OnTaskCompletedPutHumor();
            }
        }).start();
    }

    public interface OnTaskCompleted {
        void OnTaskCompletedPutHumor();
    }
}