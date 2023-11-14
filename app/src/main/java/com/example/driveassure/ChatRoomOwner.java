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

public class ChatRoomOwner extends AppCompatActivity {

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
        setContentView(R.layout.activity_chat_room_owner);

        messageListView = findViewById(R.id.messageListView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList, currentUserUid);
        messageListView.setAdapter(messageAdapter);

        Intent intent = getIntent();
        if (intent != null) {
            currentUserUid = intent.getStringExtra("currentUserUid");
            postOwnerUid = intent.getStringExtra("postOwnerUid");

            Log.d("ChatRoomOwner", "Received currentUserUid: " + currentUserUid);
            Log.d("ChatRoomOwner", "Received postOwnerUid: " + postOwnerUid);

            if (currentUserUid != null && postOwnerUid != null) {
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
            } else {
                Log.e("ChatRoomOwner", "currentUserUid or postOwnerUid is null");
                // Handle the case where UIDs are null (e.g., show an error message, log, etc.)
                finish(); // Finish the activity if UIDs are null
            }
        } else {
            Log.e("ChatRoomOwner", "Intent is null");
            // Handle the case where the intent is null (e.g., show an error message, log, etc.)
            finish(); // Finish the activity if the intent is null
        }
    }

    // Simulate receiving new messages
    private void receiveMessages() {
        if (currentUserUid != null && postOwnerUid != null) {
            // Replace this with actual logic to retrieve messages from Firestore
            List<Message> receivedMessages = getMessagesFromFirestore();

            // Add the received messages to the list
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
        // messagesCollection.whereEqualTo("sender", postOwnerUid)
        //                   .whereEqualTo("receiver", currentUserUid)
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
                        Log.d("ChatRoomOwner", "Message sent successfully");

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
                        Log.e("ChatRoomOwner", "Error sending message", e);
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
