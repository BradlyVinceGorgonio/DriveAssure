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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
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
        Log.d("HADUKEN", "hello");
        Log.d("HADUKEN", "Received currentUserUid: " + currentUserUid);
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

                        // Add the sent message to the local list
                        messageList.add(message);

                        // Notify the adapter that the data has changed
                        runOnUiThread(() -> {
                            messageAdapter.notifyDataSetChanged();
                            scrollToBottom();
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
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        Message receivedMessage = dc.getDocument().toObject(Message.class);

                        // Add the received message to the list if not empty
                        if (receivedMessage != null) {
                            messageList.add(receivedMessage);

                            // Notify the adapter that the data has changed
                            runOnUiThread(() -> {
                                messageAdapter.notifyDataSetChanged();
                                scrollToBottom();
                            });
                        }
                    }
                }
            }
        });
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

    // Retrieve post owner UID directly from the intent
    private String getPostOwnerUid() {
        Intent intent = getIntent();
        if (intent != null) {
            return intent.getStringExtra("postOwnerUid");
        } else {
            // Handle the case where the intent is null
            return "";
        }
    }
}
