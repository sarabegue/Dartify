package com.mentit.dartify.Chat;

import java.net.URISyntaxException;

public class RemoteDataSource implements DataSource {

    private static RemoteDataSource INSTANCE;
    private static EventService mEventService = EventServiceImpl.getInstance();
    private EventListener mRepoEventListener;

    private RemoteDataSource() {
        mEventService.setEventListener(this);
    }

    public static RemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource();
        }

        return INSTANCE;
    }

    @Override
    public void connect(String username) throws URISyntaxException {
        mEventService.connect(username);
    }

    @Override
    public void disconnect() {
        mEventService.disconnect();
    }

    @Override
    public void onConnect(Object... args) {
        if (mRepoEventListener != null)
            mRepoEventListener.onConnect(args);
    }

    @Override
    public void onDisconnect(Object... args) {
        if (mRepoEventListener != null)
            mRepoEventListener.onDisconnect(args);
    }

    @Override
    public void onNewMessage(Object... args) {
        if (mRepoEventListener != null)
            mRepoEventListener.onNewMessage(args);
    }

    @Override
    public void onJoinRoom(Object... args) {
        if (mRepoEventListener != null)
            mRepoEventListener.onJoinRoom(args);
    }

    @Override
    public String sendMessage(String room, String uuid, String userid2, String chatMessage, int tipo) {
        return mEventService.sendMessage(room, uuid, userid2, chatMessage, tipo);
    }

    @Override
    public void setEventListener(com.mentit.dartify.Chat.EventListener eventListener) {

    }
}
