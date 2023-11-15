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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Message message = (Message) obj;

        return senderUid.equals(message.senderUid) &&
                receiverUid.equals(message.receiverUid) &&
                messageText.equals(message.messageText);
    }

    @Override
    public int hashCode() {
        int result = senderUid.hashCode();
        result = 31 * result + receiverUid.hashCode();
        result = 31 * result + messageText.hashCode();
        return result;
    }
}
