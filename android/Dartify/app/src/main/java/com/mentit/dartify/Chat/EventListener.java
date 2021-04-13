package com.mentit.dartify.Chat;

/**
 * Main interface to listen to server events.
 *
 */
public interface EventListener {

    void onConnect(Object... args);

    void onDisconnect(Object... args);

    void onNewMessage(Object... args);

    void onJoinRoom(Object... args);
}