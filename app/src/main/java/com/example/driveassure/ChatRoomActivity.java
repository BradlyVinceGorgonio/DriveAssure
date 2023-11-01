package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);


        messageListView = findViewById(R.id.messageListView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        backButton = findViewById(R.id.backButton);

        messageList = new ArrayList<>();

        messageAdapter = new MessageAdapter(this, messageList);
        messageListView.setAdapter(messageAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    String messageContent = messageEditText.getText().toString();
                    if (!messageContent.isEmpty()) {

                        MessageAdapter.Message message = new MessageAdapter.Message(userId, messageContent, true);

                        messageList.add(message);
                        messageAdapter.notifyDataSetChanged();

                        messageEditText.setText("");

                        DatabaseReference userMessagesRef = databaseReference.child("user_messages").child(userId);
                        DatabaseReference newMessageRef = userMessagesRef.push(); // Generate a unique key for the message
                        newMessageRef.setValue(message);
                    }
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userMessagesRef = databaseReference.child("user_messages").child(userId);

            userMessagesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    messageList.clear();
                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        MessageAdapter.Message message = messageSnapshot.getValue(MessageAdapter.Message.class);
                        messageList.add(message);
                    }
                    messageAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }
}

