package com.example.driveassure;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends ArrayAdapter<Message> {

    private String currentUserUid;

    public MessageAdapter(@NonNull Context context, @NonNull List<Message> messages, String currentUserUid) {
        super(context, 0, messages);
        this.currentUserUid = currentUserUid;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.item_message, parent, false);
        }

        Message message = getItem(position);

        TextView messageTextView = view.findViewById(R.id.message_text);
        TextView timestampTextView = view.findViewById(R.id.timestamp);

        if (message != null) {
            messageTextView.setText(message.getMessageText());
            timestampTextView.setText(formatTimestamp(message.getTimestamp()));

            if (message.getSenderUid().equals(currentUserUid)) {
                // Sent message, align to the right
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.addRule(RelativeLayout.ALIGN_PARENT_END);
                messageTextView.setLayoutParams(params);

                // Change the background color for sent messages (blue)
                messageTextView.setBackgroundResource(R.drawable.bubble_right);
            } else {
                // Received message, align to the left
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.addRule(RelativeLayout.ALIGN_PARENT_START);
                messageTextView.setLayoutParams(params);

                // Change the background color for received messages (grey)
                messageTextView.setBackgroundResource(R.drawable.bubble_left);
            }
        }

        return view;
    }

    private String formatTimestamp(Date timestamp) {
        if (timestamp != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault());
            return dateFormat.format(timestamp);
        } else {
            return "";
        }
    }
}
