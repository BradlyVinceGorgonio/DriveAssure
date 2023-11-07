package com.example.driveassure;

public class ChatItem {
    private String message;
    private String sender;

    public ChatItem(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }
}


