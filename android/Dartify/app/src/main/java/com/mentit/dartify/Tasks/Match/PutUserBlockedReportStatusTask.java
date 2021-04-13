package com.mentit.dartify.Tasks.Match;

import android.content.Context;
import android.os.AsyncTask;

import com.mentit.dartify.Models.POJO.User.Foto;
import com.mentit.dartify.Network.NetworkResponse;
import com.mentit.dartify.Network.NetworkService;
import com.mentit.dartify.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PutUserBlockedReportStatusTask extends AsyncTask<String, Void, String> {
    private Context context;
    private OnTaskCompleted taskCompleted;
    long userid1;
    long userid2;
    int status;
    String reporte;

    public PutUserBlockedReportStatusTask(Context activityContext, long userid1, long userid2, int status, String reporte) {
        context = activityContext;
        taskCompleted = (OnTaskCompleted) activityContext;
        this.userid1 = userid1;
        this.userid2 = userid2;
        this.status = status;
        this.reporte = reporte;
    }

    @Override
    protected String doInBackground(String... strings) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("userid1", userid1);
            jsonObject.put("userid2", userid2);
            jsonObject.put("reporte", reporte);
            jsonObject.put("status", status);

        } catch (JSONException e) {
            //Log.d("postbody", e.getMessage());
        }

        ejecutarConsulta(context.getString(R.string.match_blockuser), jsonObject);
        return "";
    }

    private void ejecutarConsulta(final String query, final JSONObject bodydata) {
        new Thread(() -> {
            final List<String> credentials = new ArrayList<>();
            credentials.add("DARTIFY-API-KEY");
            credentials.add("REGISTRO");

            final NetworkResponse respuesta = NetworkService.INSTANCE.put(query, bodydata, credentials);

            if (!respuesta.error()) {
                taskCompleted.OnTaskCompletedPutBlockedReportStatus();
            }
        }).start();
    }

    public interface OnTaskCompleted {
        void OnTaskCompletedPutBlockedReportStatus();
    }
}