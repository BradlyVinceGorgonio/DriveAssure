package com.example.driveassure;

// Message.java
public class Message {
    private String sender;
    private String messageText;

    public Message(String sender, String messageText) {
        this.sender = sender;
        this.messageText = messageText;
    }

    public String getSender() {
        return sender;
    }

    public String getMessageText() {
        return messageText;
    }
}

