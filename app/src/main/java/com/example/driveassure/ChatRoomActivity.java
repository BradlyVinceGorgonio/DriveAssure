package com.example.driveassure;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    private ListView messageListView;
    private EditText messageEditText;
    private ImageButton sendButton;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private Handler messageHandler;
    private Runnable messageRunnable;

    private String currentUserUid;
    private String postOwnerUid;

    private FirebaseFirestore firestore;
    private CollectionReference messagesCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Intent intent = getIntent();
        String currentUserUid = intent.getStringExtra("currentUserUid");
        Log.d("ChatRoomActivity", "Received currentUserUid: " + currentUserUid);
        messageListView = findViewById(R.id.messageListView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList, currentUserUid);
        messageListView.setAdapter(messageAdapter);



        firestore = FirebaseFirestore.getInstance();
        messagesCollection = firestore.collection("messages");

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

        messageHandler = new Handler();
        messageRunnable = new Runnable() {
            @Override
            public void run() {
                // Simulate receiving new messages
                receiveMessages();
                messageHandler.postDelayed(this, 3000);
            }
        };

    }

    // Simulate receiving new messages
    private void receiveMessages() {
        if (currentUserUid != null && postOwnerUid != null) {
            // Replace this with actual logic to retrieve messages from Firestore
            List<Message> receivedMessages = getMessagesFromFirestore();

            // Add the received messages to the list if not empty
            if (receivedMessages != null && !receivedMessages.isEmpty()) {
                messageList.addAll(receivedMessages);

                // Notify the adapter that the data has changed
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messageAdapter.notifyDataSetChanged();
                        scrollToBottom();
                    }
                });
            }
        }
    }

    // Replace this with actual logic to retrieve messages from Firestore
    private List<Message> getMessagesFromFirestore() {
        // Query Firestore to get messages between currentUserUid and postOwnerUid
        // Sample code (replace it with actual Firestore query):
        // messagesCollection.whereEqualTo("senderUid", postOwnerUid)
        //                   .whereEqualTo("receiverUid", currentUserUid)
        //                   .get()
        //                   .addOnSuccessListener(queryDocumentSnapshots -> {
        //                       // Process the query results and create a list of messages
        //                   });

        // For simulation purposes, let's return an empty list
        return new ArrayList<>();
    }

    // Send a message
    private void sendMessage(String messageText) {
        if (currentUserUid != null && postOwnerUid != null) {
            Message message = new Message(currentUserUid, postOwnerUid, messageText);

            // Save the message to Firestore
            messagesCollection.add(message)
                    .addOnSuccessListener(documentReference -> {
                        // Message sent successfully
                        Log.d("ChatRoomActivity", "Message sent successfully");

                        // Add the sent message to the local list
                        messageList.add(message);

                        // Notify the adapter that the data has changed
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messageAdapter.notifyDataSetChanged();
                                scrollToBottom();
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Error sending message
                        Log.e("ChatRoomActivity", "Error sending message", e);
                    });
        }
    }

    private void scrollToBottom() {
        if (messageAdapter != null) {
            int count = messageAdapter.getCount();

            if (count > 0) {
                messageListView.setSelection(count - 1);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        messageHandler.removeCallbacks(messageRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        messageHandler.postDelayed(messageRunnable, 3000);
        receiveMessages(); // Start listening for new messages
    }
}
