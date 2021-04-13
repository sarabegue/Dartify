package com.mentit.dartify.Chat;

import java.net.URISyntaxException;

public interface DataSource extends com.mentit.dartify.Chat.EventListener {

    void connect(String username) throws URISyntaxException;

    void disconnect();

    String sendMessage(String chatroom, String uuid, String userid2, String chatMessage, int tipo);

    void setEventListener(EventListener eventListener);
}