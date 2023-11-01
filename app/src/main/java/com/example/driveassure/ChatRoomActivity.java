package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
// ...

public class ChatRoomActivity extends AppCompatActivity {

    private ListView messageListView;
    private EditText messageEditText;
    private ImageButton sendButton;
    private ImageButton backButton; // Added line
    private MessageAdapter messageAdapter;
    private List<MessageAdapter.Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        // Find views
        messageListView = findViewById(R.id.messageListView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        backButton = findViewById(R.id.backButton); // Added line

        // Initialize message list
        messageList = new ArrayList<>();

        // Create and set adapter
        messageAdapter = new MessageAdapter(this, messageList);
        messageListView.setAdapter(messageAdapter);

        // Set click listener for send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageContent = messageEditText.getText().toString();
                if (!messageContent.isEmpty()) {
                    // Create a new message object
                    MessageAdapter.Message message = new MessageAdapter.Message("Me", messageContent, true);

                    // Add the message to the list
                    messageList.add(message);
                    messageAdapter.notifyDataSetChanged();

                    // Clear the message input
                    messageEditText.setText("");
                }
            }
        });

        // Set click listener for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and navigate back to the previous activity
                finish();
            }
        });
    }
}
