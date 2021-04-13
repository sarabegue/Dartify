package com.mentit.dartify.Tasks.User;

import android.content.Context;
import android.os.AsyncTask;

import com.mentit.dartify.Network.NetworkResponse;
import com.mentit.dartify.Network.NetworkService;
import com.mentit.dartify.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetUserProfileTask extends AsyncTask<String, Void, String> {
    private Context context;
    private OnTaskCompleted taskCompleted;
    long userid;

    public GetUserProfileTask(Context activityContext, long userid) {
        context = activityContext;
        taskCompleted = (OnTaskCompleted) activityContext;
        this.userid = userid;
    }

    @Override
    protected String doInBackground(String... strings) {
        descargarPerfilUsuario(context.getString(R.string.usuario_perfil) + userid);
        return "";
    }

    private void descargarPerfilUsuario(final String query) {
        new Thread(() -> {
            final List<String> credentials = new ArrayList<>();
            credentials.add("DARTIFY-API-KEY");
            credentials.add("REGISTRO");

            final NetworkResponse respuesta = NetworkService.INSTANCE.get(query, credentials);

            if (respuesta != null && !respuesta.error()) {
                taskCompleted.OnTaskCompletedProfile(respuesta.getDatos());
            }
        }).start();
    }

    public interface OnTaskCompleted {
        void OnTaskCompletedProfile(JSONObject datos);
    }
}