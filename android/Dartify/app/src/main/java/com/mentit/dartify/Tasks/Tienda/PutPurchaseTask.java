package com.mentit.dartify.Tasks.Tienda;

import android.content.Context;
import android.os.AsyncTask;

import com.mentit.dartify.Network.NetworkResponse;
import com.mentit.dartify.Network.NetworkService;
import com.mentit.dartify.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PutPurchaseTask extends AsyncTask<String, Void, String> {
    private Context context;
    private PutPurchaseTask.OnTaskCompleted taskCompleted;

    private long userid;
    private String token;
    private String order;
    private String sku;
    private long fecha;

    public PutPurchaseTask(Context activityContext, long userid, String token, String order, long fecha, String sku) {
        context = activityContext;

        this.userid = userid;
        this.token = token;
        this.order = order;
        this.fecha = fecha;
        this.sku = sku;

        taskCompleted = (PutPurchaseTask.OnTaskCompleted) activityContext;
    }

    @Override
    protected String doInBackground(String... strings) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("userid", userid);
            jsonObject.put("token", token);
            jsonObject.put("orderid", order);
            jsonObject.put("date", fecha);
            jsonObject.put("sku", sku);

        } catch (Exception e) {
        }

        ejecutarConsulta(context.getString(R.string.tienda_compra), jsonObject);
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
                    boolean status = respuesta.getDatos().getBoolean("valid");
                    taskCompleted.OnTaskCompletedPutCompra(status);
                } catch (Exception w) {
                }
            }
        }).start();
    }

    public interface OnTaskCompleted {
        void OnTaskCompletedPutCompra(Boolean valid);
    }
}
