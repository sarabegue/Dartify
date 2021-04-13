package com.mentit.dartify.Chat;

//import android.util.Log;

import androidx.annotation.NonNull;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.mentit.dartify.BuildConfig;

import java.net.URISyntaxException;

/**
 * Implementation of {@link EventService} which connects and disconnects to the server.
 * It also sends and receives events from the server.
 */
public class EventServiceImpl implements EventService {

    private static final String TAG = EventServiceImpl.class.getSimpleName();
    private static final String SOCKET_URL = BuildConfig.SOCKET_URL;
    private static final String EVENT_CONNECT = Socket.EVENT_CONNECT;
    private static final String EVENT_DISCONNECT = Socket.EVENT_DISCONNECT;
    private static final String EVENT_NEW_MESSAGE = Socket.EVENT_MESSAGE;
    private static EventService INSTANCE;
    private static com.mentit.dartify.Chat.EventListener mEventListener;
    private static Socket mSocket;
    private String mUsername;

    // Prevent direct instantiation
    private EventServiceImpl() {
    }

    /**
     * Returns single instance of this class, creating it if necessary.
     *
     * @return
     */
    public static EventService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EventServiceImpl();
        }

        return INSTANCE;
    }

    /**
     * Connect to the server.
     *
     * @param username
     * @throws URISyntaxException
     */
    @Override
    public void connect(String username) throws URISyntaxException {
        //Log.d("MENSAJES", "CONECTANDO " + username);
        mUsername = username;
        mSocket = IO.socket(SOCKET_URL);

        // Register the incoming events and their listeners on the socket.
        mSocket.on(EVENT_CONNECT, onConnect);
        mSocket.on(EVENT_DISCONNECT, onDisconnect);
        mSocket.on(EVENT_NEW_MESSAGE, onNewMessage);

        mSocket.connect();
    }

    @Override
    public void joinRoom(String room) {
        if (mSocket.connected())
            mSocket.emit("join", room);
    }

    /**
     * Disconnect from the server.
     */
    @Override
    public void disconnect() {
        if (mSocket != null) mSocket.disconnect();
    }

    /**
     * Send chat message to the server.
     *
     * @param chatMessage
     * @return
     */
    @Override
    public String sendMessage(@NonNull final String room, @NonNull String uuid, @NonNull String userid2, @NonNull final String chatMessage, final int tipo) {
        mSocket.emit(EVENT_NEW_MESSAGE, mUsername, uuid, userid2, room, chatMessage, tipo);
        return chatMessage;
    }

    /**
     * Set eventListener.
     * <p>
     * When server sends events to the socket, those events are passed to the
     * RemoteDataSource -> Repository -> Presenter -> View using EventListener.
     *
     * @param eventListener
     */
    @Override
    public void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }

    // On connect listener
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            mSocket.emit("add user", mUsername);
            if (mEventListener != null) mEventListener.onConnect(args);
        }
    };

    // On disconnect listener
    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (mEventListener != null) mEventListener.onDisconnect(args);
        }
    };

    // On new message listener
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (mEventListener != null) mEventListener.onNewMessage(args);
        }
    };
}