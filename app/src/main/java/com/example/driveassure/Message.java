package com.example.driveassure;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Message {
    private String senderUid;
    private String receiverUid;
    private String messageText;
    private @ServerTimestamp Date timestamp;

    // Default constructor needed for Firestore deserialization
    public Message() {
    }

    public Message(String senderUid, String receiverUid, String messageText) {
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.messageText = messageText;
        this.timestamp = new Date(); // Initialize timestamp with the current date
    }

    public String getSenderUid() {
        return senderUid;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public String getMessageText() {
        return messageText;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
