package com.yeltsin.tourapp.model.chat;

public interface MessageCallback {
    void onMessageReceived(Message message);

    void onMessageError(String error);
}