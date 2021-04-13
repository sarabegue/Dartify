package com.mentit.dartify.Tasks.Chat;

import android.content.Context;
import android.os.AsyncTask;

import com.mentit.dartify.HelperClasses.SharedPreferenceUtils;
import com.mentit.dartify.Network.NetworkResponse;
import com.mentit.dartify.Network.NetworkService;
import com.mentit.dartify.R;

import java.util.ArrayList;
import java.util.List;

public class GetChatListTask extends AsyncTask<String, Void, String> {
    private Context context;
    long userid;

    public GetChatListTask(Context activityContext, long userid) {
        context = activityContext;
        this.userid = userid;
    }

    @Override
    protected String doInBackground(String... strings) {
        ejecutarConsulta(context.getString(R.string.chat_list) + userid);
        return "";
    }

    private void ejecutarConsulta(final String query) {
        new Thread(() -> {
            final List<String> credentials = new ArrayList<>();
            credentials.add("DARTIFY-API-KEY");
            credentials.add("REGISTRO");

            final NetworkResponse respuesta = NetworkService.INSTANCE.get(query, credentials);

            if (respuesta != null || !respuesta.error()) {
                SharedPreferenceUtils.getInstance(context).setValue(context.getString(R.string.chatlist_database), respuesta.getDatos().toString());
            }
        }).start();
    }
}