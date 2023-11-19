package com.example.driveassure;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Message implements Parcelable {
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

    protected Message(Parcel in) {
        senderUid = in.readString();
        receiverUid = in.readString();
        messageText = in.readString();
        timestamp = (Date) in.readSerializable();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(senderUid);
        dest.writeString(receiverUid);
        dest.writeString(messageText);
        dest.writeSerializable(timestamp);
    }
}
