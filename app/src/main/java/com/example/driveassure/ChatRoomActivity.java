package com.example.driveassure;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import com.example.driveassure.R;


public class ChatRoomActivity extends AppCompatActivity {

    private ListView messageListView;
    private EditText messageEditText;
    private Button sendButton;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private Handler messageHandler;
    private Runnable messageRunnable;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        messageListView = findViewById(R.id.messageListView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList);
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
                receiveMessage("New message");
                messageHandler.postDelayed(this, 3000);
            }
        };
        messageHandler.postDelayed(messageRunnable, 3000);
    }

    private void sendMessage(String messageText) {
        if (messageList != null && messageAdapter != null) {
            Message message = new Message("You", messageText);
            messageList.add(message);
            messageAdapter.notifyDataSetChanged();
            scrollToBottom();
        }
    }

    private void receiveMessage(String messageText) {
        if (messageList != null && messageAdapter != null) {
            Message message = new Message("OtherUser", messageText);
            messageList.add(message);
            messageAdapter.notifyDataSetChanged();
            scrollToBottom();
        }
    }

    private void scrollToBottom() {
        messageListView.post(new Runnable() {
            @Override
            public void run() {
                messageListView.setSelection(messageAdapter.getCount() - 1);
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
    }
}
