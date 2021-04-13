package com.mentit.dartify.Tasks.Match;

import android.content.Context;
import android.os.AsyncTask;

import com.mentit.dartify.Network.NetworkResponse;
import com.mentit.dartify.Network.NetworkService;
import com.mentit.dartify.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PutUserFavoriteStatusTask extends AsyncTask<String, Void, String> {
    private Context context;
    long userid1;
    long userid2;
    int status;

    public PutUserFavoriteStatusTask(Context activityContext, long userid1, long userid2, int status) {
        context = activityContext;
        this.userid1 = userid1;
        this.userid2 = userid2;
        this.status = status;
    }

    @Override
    protected String doInBackground(String... strings) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("userid1", userid1);
            jsonObject.put("userid2", userid2);
            jsonObject.put("status", status);
        } catch (JSONException e) {
        }

        ejecutarConsulta(context.getString(R.string.match_favoritestatus), jsonObject);
        return "";
    }

    private void ejecutarConsulta(final String query, final JSONObject bodydata) {
        new Thread(() -> {
            final List<String> credentials = new ArrayList<>();
            credentials.add("DARTIFY-API-KEY");
            credentials.add("REGISTRO");

            final NetworkResponse respuesta = NetworkService.INSTANCE.put(query, bodydata, credentials);

            if (!respuesta.error()) {
                try {
                    int status = respuesta.getDatos().getInt("datos");
                    //Log.d("FAVORITOR", status + "");
                    //taskCompleted.OnTaskCompletedPutFavoriteStatus(status);
                } catch (Exception w) {
                }
            }
        }).start();
    }
}