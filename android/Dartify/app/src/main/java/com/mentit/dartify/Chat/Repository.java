package com.mentit.dartify.Chat;

import androidx.annotation.NonNull;

import java.net.URISyntaxException;

/**
 * Repository can send events to a remote data source and can listen
 * for incoming events from remote data source as well. This bidirectional
 * flow of events is what makes the app realtime.
 * <p>
 * Repository implements {@link DataSource} which can send and receive events.
 */
public class Repository implements com.mentit.dartify.Chat.DataSource {

    private static Repository INSTANCE = null;
    private final DataSource mRemoteDataSource;
    private final DataSource mLocalDataSource;
    private EventListener mPresenterEventListener;

    // Prevent direct instantiation
    private Repository(@NonNull DataSource remoteDataSource,
                       @NonNull DataSource localDataSource) {
        mLocalDataSource = localDataSource;
        mRemoteDataSource = remoteDataSource;
        mRemoteDataSource.setEventListener(this);
    }

    /**
     * Returns single instance of this class, creating it if necessary.
     *
     * @param remoteDataSource
     * @param localDataSource
     * @return
     */
    public static Repository getInstance(@NonNull DataSource remoteDataSource,
                                         @NonNull DataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new Repository(remoteDataSource, localDataSource);
        }

        return INSTANCE;
    }

    @Override
    public void setEventListener(EventListener eventListener) {
        mPresenterEventListener = eventListener;
    }

    /**
     * Connect to remote chat server.
     *
     * @param username
     * @throws URISyntaxException
     */
    @Override
    public void connect(String username) throws URISyntaxException {
        mRemoteDataSource.connect(username);
    }

    /**
     * Disconnect from remote chat server.
     */
    @Override
    public void disconnect() {
        mRemoteDataSource.disconnect();
    }

    /**
     * Send chat message.
     *
     * @param chatMessage
     * @return
     */
    @Override
    public String sendMessage(String chatroom, String uuid, String userid2, String chatMessage, int tipo) {
        return mRemoteDataSource.sendMessage(chatroom, uuid, userid2, chatMessage, tipo);
    }

    @Override
    public void onConnect(Object... args) {
        if (mPresenterEventListener != null)
            mPresenterEventListener.onConnect(args);
    }

    @Override
    public void onDisconnect(Object... args) {
        if (mPresenterEventListener != null)
            mPresenterEventListener.onDisconnect(args);
    }

    @Override
    public void onNewMessage(Object... args) {
        if (mPresenterEventListener != null)
            mPresenterEventListener.onNewMessage(args);
    }

    @Override
    public void onJoinRoom(Object... args) {

    }
}