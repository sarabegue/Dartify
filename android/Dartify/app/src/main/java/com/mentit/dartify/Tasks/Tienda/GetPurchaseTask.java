package com.mentit.dartify.Tasks.Tienda;

import android.content.Context;
import android.os.AsyncTask;

import com.mentit.dartify.Network.NetworkResponse;
import com.mentit.dartify.Network.NetworkService;
import com.mentit.dartify.R;

import java.util.ArrayList;
import java.util.List;

public class GetPurchaseTask extends AsyncTask<String, Void, String> {
    private GetPurchaseTask.OnTaskCompleted taskCompleted;

    private long userid;
    private String query;
    private Context c;

    public GetPurchaseTask(Context activityContext, long userid) {
        this.c = activityContext;
        this.userid = userid;
        this.query = getQueryString();
        taskCompleted = (GetPurchaseTask.OnTaskCompleted) activityContext;
    }

    private String getQueryString() {
        return c.getString(R.string.tienda_compra) + userid;
    }

    @Override
    protected String doInBackground(String... strings) {
        ejecutarConsulta(query);
        return "";
    }

    private void ejecutarConsulta(final String query) {
        new Thread(() -> {
            final List<String> credentials = new ArrayList<>();
            credentials.add("DARTIFY-API-KEY");
            credentials.add("REGISTRO");

            try {
                final NetworkResponse respuesta = NetworkService.INSTANCE.get(query, credentials);
                taskCompleted.OnTaskCompletedGetCompra(respuesta.getDatos().getInt("membresia"), respuesta.getDatos().getString("vencimiento"));
            } catch (Exception w) {
                taskCompleted.OnTaskCompletedGetCompra(1, "");
            }
        }).start();
    }

    public interface OnTaskCompleted {
        void OnTaskCompletedGetCompra(int membresia, String vencimiento);
    }
}