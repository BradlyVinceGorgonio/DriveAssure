package com.example.driveassure;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends Fragment {

    private EditText messageEditText;
    private Button sendButton;
    private TextView messageTextView;

    private DatabaseReference databaseReference;

    public ChatActivity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_activity, container, false);

        messageEditText = view.findViewById(R.id.messageEditText);
        sendButton = view.findViewById(R.id.sendButton);
        messageTextView = view.findViewById(R.id.messageTextView);

        // Initialize Firebase (Make sure you have already set up Firebase in your project)
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("chat"); // "chat" is the name of the Firebase database node where you want to store messages

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        return view;
    }

    private void sendMessage() {
        String message = messageEditText.getText().toString();
        if (!message.isEmpty()) {
            DatabaseReference messageRef = databaseReference.push(); // Generate a unique key for the message
            messageRef.setValue(message); // Store the message in the Firebase database
            messageEditText.setText(""); // Clear the input field

            // Append the sent message to the existing text in the TextView
            String currentText = messageTextView.getText().toString();
            messageTextView.setText(currentText + "\n" + message);
        }
    }
}