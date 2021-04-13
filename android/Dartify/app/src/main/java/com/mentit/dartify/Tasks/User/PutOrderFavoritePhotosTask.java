package com.mentit.dartify.Tasks.User;

import android.content.Context;
import android.os.AsyncTask;

import com.mentit.dartify.Models.POJO.User.Foto;
import com.mentit.dartify.Network.NetworkResponse;
import com.mentit.dartify.Network.NetworkService;
import com.mentit.dartify.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PutOrderFavoritePhotosTask extends AsyncTask<String, Void, String> {
    private Context context;
    private OnTaskCompleted taskCompleted;
    long userid;
    ArrayList<Foto> listaOrdenada;

    public PutOrderFavoritePhotosTask(Context activityContext, long userid, ArrayList lista) {
        context = activityContext;
        taskCompleted = (OnTaskCompleted) activityContext;
        this.userid = userid;
        this.listaOrdenada = lista;
    }

    @Override
    protected String doInBackground(String... strings) {
        ArrayList<Long> lista = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();

        for (Foto f : listaOrdenada) {
            lista.add(f.getID());
        }

        try {
            jsonObject.put("userid", userid);
            jsonObject.put("listaids", lista);
        } catch (Exception e) {

        }

        ejecutarConsulta(context.getString(R.string.usuario_ordenarfotosfavoritas), jsonObject);
        return "";
    }

    private void ejecutarConsulta(final String query, final JSONObject bodydata) {
        new Thread(() -> {
            final List<String> credentials = new ArrayList<>();
            credentials.add("DARTIFY-API-KEY");
            credentials.add("REGISTRO");

            final NetworkResponse respuesta = NetworkService.INSTANCE.put(query, bodydata, credentials);
            if (!respuesta.error()) {
                taskCompleted.OnTaskCompletedOrderFavoritePhotos();
            }
        }).start();
    }

    public interface OnTaskCompleted {
        void OnTaskCompletedOrderFavoritePhotos();
    }
}