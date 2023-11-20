package com.example.driveassure;

public class ChatItem {
    private String message;
    private String senderUid;  // Updated variable name
    private String currentUserUid;
    private String postOwnerUid;

    public ChatItem(String currentUserUid, String postOwnerUid, String message) {
        this.currentUserUid = currentUserUid;
        this.postOwnerUid = postOwnerUid;
        this.message = message;
        this.senderUid = currentUserUid; // Set sender UID to the current user for simplicity
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderUid() {  // Updated method name
        return senderUid;
    }

    public String getCurrentUserUid() {
        return currentUserUid;
    }

    public String getPostOwnerUid() {
        return postOwnerUid;
    }

    public boolean isReceiver() {
        // Replace this with the logic to determine if the current user is the receiver
        return currentUserUid.equals(postOwnerUid);
    }

    public boolean isUsername() {
        // Replace this with the logic to determine if it's a username
        // For example, you can check if the message starts with "@username"
        return message.startsWith("@username");
    }
}
