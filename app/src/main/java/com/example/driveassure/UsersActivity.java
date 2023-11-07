package com.example.driveassure;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private ListView userListView;
    private List<String> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        // Initialize the user list
        userList = new ArrayList<>();
        userList.add("User 1");
        userList.add("User 2");
        userList.add("User 3");

        // Get the ListView and set the adapter
        userListView = findViewById(R.id.userListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        userListView.setAdapter(adapter);

        // Handle user click event
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedUser = userList.get(position);
                Intent intent = new Intent(UsersActivity.this, ChatRoomActivity.class);
                intent.putExtra("senderName", selectedUser);
                startActivity(intent);
            }
        });
    }
}

