package com.mentit.dartify.Tasks.Chat;

import android.content.Context;
import android.os.AsyncTask;

import com.mentit.dartify.Models.MensajeChat;
import com.mentit.dartify.Models.room.MensajeChatDao;
import com.mentit.dartify.Models.room.MensajeDatabase;
import com.mentit.dartify.Network.NetworkResponse;
import com.mentit.dartify.Network.NetworkService;
import com.mentit.dartify.R;
import com.mentit.dartify.util.FormatUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PutMessageTask extends AsyncTask<String, Void, String> {
    private MensajeChatDao dao;
    private Context context;
    private MensajeChat nuevomensaje;
    private long userid1;
    private long userid2;
    private String message;
    private String messageid;
    private int type;

    public PutMessageTask(Context activityContext, MensajeDatabase db, String uuid, long userid1, long userid2, String message, int type) {
        context = activityContext;
        nuevomensaje = new MensajeChat();

        nuevomensaje.setRemoteId(uuid);
        nuevomensaje.setIdRemitente(userid1);
        nuevomensaje.setIdDestinatario(userid2);
        nuevomensaje.setStrChatroom(FormatUtil.getChatRoom(userid1, userid2));
        nuevomensaje.setStrMensaje(message);
        nuevomensaje.setFromme(true);
        nuevomensaje.setTipo(type);

        this.userid1 = userid1;
        this.userid2 = userid2;
        this.message = message;
        this.messageid = uuid;
        this.type = type;
        this.dao = db.mensajechatDAO();
    }

    @Override
    protected String doInBackground(String... strings) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("userid1", userid1);
            jsonObject.put("userid2", userid2);
            jsonObject.put("tipo", type);
            jsonObject.put("mensaje", message);
            jsonObject.put("mensajeid", messageid);

        } catch (Exception e) {
        }

        ejecutarConsulta(context.getString(R.string.chat_sendmessage), jsonObject);
        return "";
    }

    private void ejecutarConsulta(final String query, final JSONObject bodydata) {
        dao.insert(nuevomensaje);

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