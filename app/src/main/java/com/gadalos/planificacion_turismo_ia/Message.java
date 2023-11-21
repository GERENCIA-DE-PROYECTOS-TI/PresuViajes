package com.yeltsin.tourapp.model.chat;

public class Message {
    private String message;
    private MessageRole role;

    public Message() {
    }

    public Message(String message, MessageRole role) {
        this.message = message;
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public String getRole() {
        return role.getApiValue();
    }

}