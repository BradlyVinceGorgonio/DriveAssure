package com.example.driveassure;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    private Context context;
    private int sentLayout;
    private int receivedLayout;
    private List<Message> messages;
    private String currentUserUid;

    public MessageAdapter(@NonNull Context context, int sentLayout, int receivedLayout, @NonNull List<Message> messages, String currentUserUid) {
        super(context, sentLayout, messages);
        this.context = context;
        this.sentLayout = sentLayout;
        this.receivedLayout = receivedLayout;
        this.messages = messages;
        this.currentUserUid = currentUserUid;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // Choose the layout based on the sender
            if (messages.get(position).getSenderUid().equals(currentUserUid)) {
                view = inflater.inflate(sentLayout, parent, false);
            } else {
                view = inflater.inflate(receivedLayout, parent, false);
            }
        }

        TextView messageText = view.findViewById(R.id.message_text);
        TextView timestampText = view.findViewById(R.id.timestamp);

        // Set the message text
        messageText.setText(messages.get(position).getMessageText());

        // Set the timestamp
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, HH:mm");
        String timestamp = dateFormat.format(messages.get(position).getTimestamp());
        timestampText.setText(timestamp);

        return view;
    }
}
