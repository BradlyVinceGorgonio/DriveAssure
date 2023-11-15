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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

            Log.d("ChatRoomActivity", "Received currentUserUid: " + currentUserUid);
            Log.d("ChatRoomActivity", "Received postOwnerUid: " + postOwnerUid);

            // Retrieve the current user UID from the intent
            currentUserUid = intent.getStringExtra("currentUserUid");
            Log.d("HADUKEN", "Received currentUserUid: " + currentUserUid);

            if (currentUserUid != null && postOwnerUid != null) {
                // Generate a unique chat room ID based on user UIDs
                String chatRoomId = generateChatRoomId(currentUserUid, postOwnerUid);

                // Use the generated chat room ID for Firestore collection
                firestore = FirebaseFirestore.getInstance();
                messagesCollection = firestore.collection("messages").document(chatRoomId).collection("messages");

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
                Log.e("ChatRoomActivity", "currentUserUid or postOwnerUid is null");
                // Handle the case where UIDs are null (e.g., show an error message, log, etc.)
                finish(); // Finish the activity if UIDs are null
            }
        } else {
            Log.e("ChatRoomActivity", "Intent is null");
            // Handle the case where the intent is null (e.g., show an error message, log, etc.)
            finish(); // Finish the activity if the intent is null
        }
    }

    // Generate a unique chat room ID based on user UIDs
    private String generateChatRoomId(String uid1, String uid2) {
        // Sort the UIDs to ensure consistency
        String[] uids = {uid1, uid2};
        Arrays.sort(uids);

        // Concatenate sorted UIDs to create a unique ID
        return uids[0] + "_" + uids[1];
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

                        // No need to add the sent message to the local list, it will be received through snapshots

                        // Notify the adapter that the data has changed
                        runOnUiThread(() -> {
                            // Clear the current list and add all messages from the snapshot
                            messageList.clear();
                            receiveMessages();
                            scrollToTop();
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Error sending message
                        Log.e("ChatRoomActivity", "Error sending message", e);
                    });
        }
    }

    // Simulate receiving new messages
    private void receiveMessages() {
        messagesCollection.addSnapshotListener((value, error) -> {
            if (value != null) {
                List<Message> receivedMessages = new ArrayList<>();
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        Message receivedMessage = dc.getDocument().toObject(Message.class);

                        // Add the received message to the list if not empty
                        if (receivedMessage != null) {
                            receivedMessages.add(receivedMessage);
                        }
                    }
                }

                // Reverse the order of received messages to display the newest at the bottom
                Collections.reverse(receivedMessages);

                // Add all received messages to the local list
                messageList.addAll(receivedMessages);

                // Notify the adapter that the data has changed
                runOnUiThread(() -> {
                    messageAdapter.notifyDataSetChanged();
                    scrollToTop();
                });
            }
        });
    }

    private void scrollToTop() {
        if (messageAdapter != null && messageList.size() > 0) {
            messageListView.smoothScrollToPosition(0);
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
