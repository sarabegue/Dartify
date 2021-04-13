package com.mentit.dartify.Models.room;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.mentit.dartify.Chat.EventListener;
import com.mentit.dartify.Chat.EventServiceImpl;
import com.mentit.dartify.HelperClasses.SharedPreferenceUtils;
import com.mentit.dartify.Models.MensajeChat;
import com.mentit.dartify.R;
import com.mentit.dartify.Tasks.Chat.GetMessagesTask;
import com.mentit.dartify.Tasks.Chat.PutMessageTask;

import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class MensajeDatabaseRepository {
    private MensajeChatDao dao;
    private LiveData<List<MensajeChat>> data;
    private LiveData<List<MensajeChat>> dataChatList;
    private static Context context;
    private long userid;
    private MensajeDatabase appdb;
    private EventServiceImpl chatService;
    private MediaPlayer mediaPlayer;

    public MensajeDatabaseRepository(Application app) {
        context = app;
        appdb = MensajeDatabase.getInstance(app);
        dao = appdb.mensajechatDAO();

        mediaPlayer = MediaPlayer.create(context, R.raw.sound_chatmessage);
        userid = SharedPreferenceUtils.getInstance(context).getLongValue(context.getString(R.string.user_id), 0);

        try {
            chatService = (EventServiceImpl) EventServiceImpl.getInstance();
            chatService.connect(userid + "");
        } catch (Exception w) {
            Log.d("MENSAJES", w.getMessage());
        }

        chatService.setEventListener(listener);

        descargarMensajes();
    }

    public void joinRoom(String room) {
        chatService.joinRoom(room);
    }

    public void descargarMensajes() {
        new GetMessagesTask(context).execute();
    }

    public void guardarMensaje(String room, long userid1, long userid2, String msg, int numtipo) {
        String uuid = UUID.randomUUID().toString().toUpperCase().replace("-", "");
        new PutMessageTask(context, appdb, uuid, userid1, userid2, msg, numtipo).execute("");
        chatService.sendMessage(room, uuid, userid2 + "", msg, numtipo);
    }

    private EventListener listener = new EventListener() {
        @Override
        public void onConnect(Object... args) {
            if (args.length > 0) {
                Log.d("MENSAJES", args[0].toString());
            }
        }

        @Override
        public void onDisconnect(Object... args) {
            if (args.length > 0) {
                Log.d("MENSAJES", args[0].toString());
            }
        }

        @Override
        public void onNewMessage(Object... args) {
            if (args.length > 0) {
                try {
                    Log.d("MENSAJES", args[0].toString());
                    JSONObject jo = new JSONObject(args[0].toString());

                    long sender = jo.getLong("userid1");
                    long rece = jo.getLong("userid2");
                    String chatRoom = jo.getString("chatroom");
                    String uuid = jo.getString("uuid");
                    String message = jo.getString("message");
                    int tipo = jo.getInt("tipo");

                    MensajeChat m = new MensajeChat();
                    m.setRemoteId(uuid);
                    m.setIdRemitente(sender);
                    m.setIdDestinatario(rece);
                    m.setStrChatroom(chatRoom);
                    m.setStrMensaje(message);
                    m.setTipo(tipo);

                    m.setFromme(false);

                    try {
                        MensajeChat tmp = dao.getMensaje(uuid);
                        if (tmp == null) {
                            dao.insert(m);
                            if (!mediaPlayer.isPlaying()) {
                                mediaPlayer.start();
                            }
                        }
                    } catch (Exception je) {
                    }

                    new GetMessagesTask(context).execute();
                } catch (Exception je) {
                }
            }
        }


        @Override
        public void onJoinRoom(Object... args) {
            if (args.length > 0) {
                Log.d("MENSAJES", args[0].toString());
            }
        }
    };

    public void insert(MensajeChat d) {
        new InsertDataAsyncTask(dao).execute(d);
    }

    public void deleteAllData() {
        new DeleteAllDataAsyncTask(dao).execute();
    }

    public LiveData<List<MensajeChat>> getAllMensajes(String chatRoom) {
        data = dao.getAllMensajes(chatRoom);
        return data;
    }

    public LiveData<List<MensajeChat>> getChatList(long userid) {
        dataChatList = dao.getChatList(userid);
        return dataChatList;
    }

    private static class InsertDataAsyncTask extends AsyncTask<MensajeChat, Void, Void> {
        private MensajeChatDao dc;

        private InsertDataAsyncTask(MensajeChatDao d) {
            this.dc = d;
        }

        @Override
        protected Void doInBackground(MensajeChat... data) {
            dc.insert(data[0]);
            return null;
        }
    }

    private static class DeleteAllDataAsyncTask extends AsyncTask<MensajeChat, Void, Void> {
        private MensajeChatDao dc;

        private DeleteAllDataAsyncTask(MensajeChatDao d) {
            this.dc = d;
        }

        @Override
        protected Void doInBackground(MensajeChat... d) {
            try {
                dc.deleteAll();
            } catch (Exception w) {
                Log.d("Exception", w.getMessage());
            }
            return null;
        }
    }
}