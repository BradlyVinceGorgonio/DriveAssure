package com.example.driveassure;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoomActivity extends AppCompatActivity {

    private ListView messageListView;
    private EditText messageEditText;
    private ImageButton sendButton;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private Handler messageHandler;
    private Runnable messageRunnable;
    private Set<String> receivedMessageIds;
    CircleImageView carOwnerProfile;
    TextView ownerContactNumber;
    TextView carOwnerName;
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
        carOwnerProfile = findViewById(R.id.carOwnerProfile);

        // Initialize messageList with an empty list
        messageList = new ArrayList<>();

        // Initialize receivedMessageIds with a set to keep track of received messages
        receivedMessageIds = new HashSet<>();

        // Retrieve saved state if available
        if (savedInstanceState != null) {
            messageList = savedInstanceState.getParcelableArrayList("messageList");
            receivedMessageIds = new HashSet<>(savedInstanceState.getStringArrayList("receivedMessageIds"));
        }

        Intent intent = getIntent();
        if (intent != null) {
            currentUserUid = intent.getStringExtra("currentUserUid");
            postOwnerUid = intent.getStringExtra("postOwnerUid");

            Log.d("ChatRoomActivity", "Received currentUserUid: " + currentUserUid);
            Log.d("ChatRoomActivity", "Received postOwnerUid: " + postOwnerUid);

            if (currentUserUid != null && postOwnerUid != null) {
                String chatRoomId = generateChatRoomId(currentUserUid, postOwnerUid);

                firestore = FirebaseFirestore.getInstance();
                messagesCollection = firestore.collection("messages").document(chatRoomId).collection("messages");

                // Initialize messageAdapter with the updated constructor
                messageAdapter = new MessageAdapter(this, R.layout.item_message_sent, R.layout.item_message_received, messageList, currentUserUid);
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

                messageHandler = new Handler();
                messageRunnable = new Runnable() {
                    @Override
                    public void run() {
                        receiveMessages();
                        messageHandler.postDelayed(this, 3000);
                    }
                };

                fetchDataFromFirestore1(currentUserUid);

            } else {
                Log.e("ChatRoomActivity", "currentUserUid or postOwnerUid is null");
                finish();
            }
        } else {
            Log.e("ChatRoomActivity", "Intent is null");
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save relevant data to outState bundle
        outState.putParcelableArrayList("messageList", new ArrayList<>(messageList));
        outState.putStringArrayList("receivedMessageIds", new ArrayList<>(receivedMessageIds));
    }

    private String generateChatRoomId(String uid1, String uid2) {
        String[] uids = {uid1, uid2};
        Arrays.sort(uids);
        return uids[0] + "_" + uids[1];
    }


    private void sendMessage(String messageText) {
        if (currentUserUid != null && postOwnerUid != null) {
            Message message = new Message(currentUserUid, postOwnerUid, messageText, new Date());

            messagesCollection.add(message)
                    .addOnSuccessListener(documentReference -> {
                        Log.d("ChatRoomActivity", "Message sent successfully");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("ChatRoomActivity", "Error sending message", e);
                    });
        }
    }

    private void receiveMessages() {
        messagesCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Message> receivedMessages = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Message receivedMessage = documentSnapshot.toObject(Message.class);
                if (receivedMessage != null && !receivedMessageIds.contains(documentSnapshot.getId())) {
                    receivedMessages.add(receivedMessage);
                    receivedMessageIds.add(documentSnapshot.getId());
                }
            }

            messageList.addAll(receivedMessages);

            Collections.sort(messageList, (message1, message2) -> {
                if (message1.getTimestamp() == null || message2.getTimestamp() == null) {
                    return 0;
                }
                return message1.getTimestamp().compareTo(message2.getTimestamp());
            });

            runOnUiThread(() -> {
                messageAdapter.notifyDataSetChanged();
                scrollToBottom();
            });
        }).addOnFailureListener(e -> {
            Log.e("ChatRoomActivity", "Error receiving messages", e);
        });
    }
    private void fetchDataFromFirestore1(String UID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(UID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String OwnerName = document.getString("name");
                            String OwnerNumber = document.getString("contact number");
                            String OwnerEmail = document.getString("email");

                            carOwnerName = findViewById(R.id.carOwnerName);
                            ownerContactNumber = findViewById(R.id.ownerContactNumber);

                            carOwnerName.setText(OwnerName);
                            ownerContactNumber.setText(OwnerNumber);

                            // Fetch the profile picture URL from Firebase Storage
                            String imagePath = "users/" + UID + "/" + "face.jpg";
                            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(imagePath);

                            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();
                                Glide.with(this).load(imageUrl).into(carOwnerProfile);
                                // Start receiving messages after fetching user data
                                messageHandler.post(messageRunnable);
                            }).addOnFailureListener(exception -> {
                                // Handle the failure scenario
                                Log.e("ChatRoomActivity", "Error fetching profile picture", exception);
                            });

                        } else {
                            Log.e("ChatRoomActivity", "Document does not exist");
                        }
                    } else {
                        Log.e("ChatRoomActivity", "Error fetching user data", task.getException());
                    }
                });
    }

    private String getCurrentUserUid() {
        return currentUserUid;
    }

    private void scrollToBottom() {
        messageListView.setSelection(messageAdapter.getCount() - 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the message handler callbacks to prevent memory leaks
        if (messageHandler != null) {
            messageHandler.removeCallbacks(messageRunnable);
        }
    }
}
