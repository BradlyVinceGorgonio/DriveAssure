package com.example.driveassure;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    private ListView messageListView;
    private EditText messageEditText;
    private ImageButton sendButton;
    private ImageButton backButton;
    private MessageAdapter messageAdapter;
    private List<MessageAdapter.Message> messageList;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private String receiverUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            finish();
        }

        messageListView = findViewById(R.id.messageListView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        backButton = findViewById(R.id.backButton);

        messageList = new ArrayList<>();

        messageAdapter = new MessageAdapter(this, messageList);
        messageListView.setAdapter(messageAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        receiverUsername = getIntent().getStringExtra("receiverUsername");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // You can implement custom navigation here or stay in the chat room.
            }
        });

        listenForMessages();
    }

    private void sendMessage() {
        String userId = currentUser.getUid();
        String messageContent = messageEditText.getText().toString();
        if (!messageContent.isEmpty()) {
            String chatRoomId = calculateChatRoomId(userId, receiverUsername);

            MessageAdapter.Message message = new MessageAdapter.Message(userId, receiverUsername, messageContent, true);

            messageList.add(message);
            messageAdapter.notifyDataSetChanged();

            messageEditText.setText("");

            DatabaseReference chatRoomRef = databaseReference.child("chat_rooms").child(chatRoomId);
            DatabaseReference newMessageRef = chatRoomRef.push();
            newMessageRef.setValue(message);
        }
    }

    private void listenForMessages() {
        String chatRoomId = calculateChatRoomId(currentUser.getUid(), receiverUsername);
        DatabaseReference chatRoomRef = databaseReference.child("chat_rooms").child(chatRoomId);

        chatRoomRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                MessageAdapter.Message message = dataSnapshot.getValue(MessageAdapter.Message.class);
                messageList.add(message);
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private String calculateChatRoomId(String user1Id, String user2Username) {
        return user1Id.compareTo(user2Username) < 0 ? user1Id + user2Username : user2Username + user1Id;
    }
}
