package com.example.driveassure;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    private String currentUserUid; // New variable

    public MessageAdapter(Context context, List<Message> messages, String currentUserUid) {
        super(context, 0, messages);
        this.currentUserUid = currentUserUid; // Initialize currentUserUid
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message, parent, false);
        }

        TextView messageText = convertView.findViewById(R.id.message_text);

        // Check if the message is from the current user or the other user
        if (message != null) {
            if (message.getSenderUid().equals(currentUserUid)) {
                // Message sent by the current user, align to the right
                messageText.setText(message.getMessageText());
                // Set layout parameters or background to indicate a message sent by the user
            } else {
                // Message received from the other user, align to the left
                messageText.setText(message.getMessageText());
                // Set layout parameters or background to indicate a received message
            }
        }

        return convertView;
    }
}
