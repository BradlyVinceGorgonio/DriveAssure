package com.example.driveassure;

// ChatRoomOwner.java

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomOwner extends AppCompatActivity {

    private ListView messageListView;
    private EditText messageEditText;
    private Button sendButton;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_owner);

        String receiverUid = getIntent().getStringExtra("receiverUid");

        messageListView = findViewById(R.id.messageListViewOwner);
        messageEditText = findViewById(R.id.messageEditTextOwner);
        sendButton = findViewById(R.id.sendButtonOwner);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList);
        messageListView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageEditText.getText().toString();
                if (!messageText.isEmpty()) {
                    sendMessage(messageText);
                    messageEditText.setText("");
                }
            }
        });

        // Implement Firebase Realtime Database or Firestore integration for messaging
        // You need to set up a listener to receive messages and update the messageList
        // You can use Firebase Realtime Database or Firestore for this purpose
    }

    private void sendMessage(String messageText) {
        // Implement sending messages to the receiver identified by receiverUid
        // Update the messageList and notify the adapter
    }
}
