package com.example.driveassure;

public class ChatItem {
    private String message;
    private String sender;
    private String currentUserUid;
    private String postOwnerUid;

    public ChatItem(String currentUserUid, String postOwnerUid, String message) {
        this.currentUserUid = currentUserUid;
        this.postOwnerUid = postOwnerUid;
        this.message = message;
        this.sender = currentUserUid; // Set sender to the current user for simplicity
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public String getCurrentUserUid() {
        return currentUserUid;
    }

    public String getPostOwnerUid() {
        return postOwnerUid;
    }

    public boolean isReceiver() {
        // Replace this with the logic to determine if the current user is the receiver
        return false;
    }

    public boolean isUsername() {
        // Replace this with the logic to determine if it's a username
        return false;
    }
}
