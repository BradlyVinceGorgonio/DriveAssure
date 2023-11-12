package com.example.driveassure;

public class Message {
    private String senderUid;
    private String receiverUid;
    private String messageText;

    // Default constructor needed for Firestore deserialization
    public Message() {
    }

    public Message(String senderUid, String receiverUid, String messageText) {
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.messageText = messageText;
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
}
