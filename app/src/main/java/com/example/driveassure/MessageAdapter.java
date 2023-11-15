package com.example.driveassure;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    private String currentUserUid;

    public MessageAdapter(@NonNull Context context, @NonNull List<Message> messages, String currentUserUid) {
        super(context, 0, messages);
        this.currentUserUid = currentUserUid;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.item_message, parent, false);
        }

        Message message = getItem(position);

        TextView messageTextView = view.findViewById(R.id.message_text);

        if (message != null) {
            messageTextView.setText(message.getMessageText());

            if (message.getSenderUid().equals(currentUserUid)) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.addRule(RelativeLayout.ALIGN_PARENT_END);
                messageTextView.setLayoutParams(params);

                messageTextView.setBackgroundResource(R.drawable.bubble_right);
            } else {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.addRule(RelativeLayout.ALIGN_PARENT_START);
                messageTextView.setLayoutParams(params);

                messageTextView.setBackgroundResource(R.drawable.bubble_left);
            }
        }

        return view;
    }
}
