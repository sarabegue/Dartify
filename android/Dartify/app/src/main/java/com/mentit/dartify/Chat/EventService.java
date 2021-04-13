package com.mentit.dartify.Chat;

import java.net.URISyntaxException;

/**
 * Service layer that connects/disconnects to the server and
 * sends and receives events too.
 */
public interface EventService {

    void connect(String username) throws URISyntaxException;

    void joinRoom(String room);

    void disconnect();

    String sendMessage(String chatRoom, String uuid, String userid2, String chatMessage, int tipo);

    void setEventListener(com.mentit.dartify.Chat.EventListener eventListener);
}
