package com.example.driveassure;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;

public class MessageAdapter extends BaseAdapter {
    private Context mContext;
    private List<Message> mMessageList;

    public MessageAdapter(Context context, List<Message> messageList) {
        mContext = context;
        mMessageList = messageList;
    }

    @Override
    public int getCount() {
        return mMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMessageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.message_bubble, parent, false);
        }

        TextView senderTextView = convertView.findViewById(R.id.senderTextView);
        TextView messageTextView = convertView.findViewById(R.id.messageTextView);

        Message message = mMessageList.get(position);
        senderTextView.setText(message.getSenderName());
        messageTextView.setText(message.getMessageText());

        // Change the alignment of the message bubble based on sender
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) messageTextView.getLayoutParams();
        if (message.isSentByMe()) {
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            messageTextView.setBackgroundResource(R.drawable.message_bubble_sent);
        } else {
            params.addRule(RelativeLayout.ALIGN_PARENT_START);
            messageTextView.setBackgroundResource(R.drawable.message_bubble_received);
        }
        messageTextView.setLayoutParams(params);

        return convertView;
    }

    public static class Message {
        private String senderName;
        private String messageText;
        private boolean sentByMe;

        public Message(String senderName, String messageText, String messageContent, boolean sentByMe) {
            this.senderName = senderName;
            this.messageText = messageText;
            this.sentByMe = sentByMe;
        }

        public String getSenderName() {
            return senderName;
        }

        public String getMessageText() {
            return messageText;
        }

        public boolean isSentByMe() {
            return sentByMe;
        }
    }
}
