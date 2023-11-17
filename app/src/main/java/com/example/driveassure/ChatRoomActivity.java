package com.example.driveassure;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
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

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList, currentUserUid);
        messageListView.setAdapter(messageAdapter);
        receivedMessageIds = new HashSet<>();

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

    private String generateChatRoomId(String uid1, String uid2) {
        String[] uids = {uid1, uid2};
        java.util.Arrays.sort(uids);
        return uids[0] + "_" + uids[1];
    }

    private void sendMessage(String messageText) {
        if (currentUserUid != null && postOwnerUid != null) {
            Message message = new Message(currentUserUid, postOwnerUid, messageText);

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

    private void scrollToBottom() {
        if (messageAdapter != null && messageList.size() > 0) {
            int lastItemIndex = messageList.size() - 1;
            messageListView.smoothScrollToPosition(lastItemIndex);
        }
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
                            String ownerName = document.getString("name");
                            String ownerNumber = document.getString("contact number");

                            carOwnerName = findViewById(R.id.carOwnerName);
                            ownerContactNumber = findViewById(R.id.ownerContactNumber);

                            carOwnerName.setText(ownerName);
                            ownerContactNumber.setText(ownerNumber);

                            String imagePath = "users/" + UID + "/" + "face.jpg";
                            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(imagePath);

                            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();

                                carOwnerProfile = findViewById(R.id.carOwnerProfile);
                                Glide.with(this).load(imageUrl).into(carOwnerProfile);

                            }).addOnFailureListener(exception -> {
                                // Handle the failure scenario
                            });
                        } else {
                            // Handle the case where the document does not exist
                        }
                    } else {
                        // Handle the failure scenario
                    }
                });
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
        receiveMessages();
    }
}
