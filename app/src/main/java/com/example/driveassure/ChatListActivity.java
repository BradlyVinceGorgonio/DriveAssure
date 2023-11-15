package com.example.driveassure;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private ChatListAdapter chatListAdapter;
    private List<ChatItem> chatItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatItemList = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(chatItemList);

        chatListAdapter.setOnItemClickListener(new ChatListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ChatItem chatItem) {
                // Handle item click, e.g., navigate to ChatRoomActivity
                Intent intent = new Intent(ChatListActivity.this, ChatRoomActivity.class);
                intent.putExtra("currentUserUid", chatItem.getCurrentUserUid());
                intent.putExtra("postOwnerUid", chatItem.getPostOwnerUid());
                startActivity(intent);
            }
        });

        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatListAdapter);

        // Register the receiver for new messages
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if ("new-message".equals(intent.getAction())) {
                    String currentUserUid = intent.getStringExtra("currentUserUid");
                    String postOwnerUid = intent.getStringExtra("postOwnerUid");
                    String messageText = intent.getStringExtra("messageText");

                    // Update the chat list with the new message
                    ChatItem chatItem = new ChatItem(currentUserUid, postOwnerUid, messageText);
                    chatItemList.add(chatItem);
                    chatListAdapter.notifyDataSetChanged();
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter("new-message");
        registerReceiver(receiver, intentFilter);
    }
}
