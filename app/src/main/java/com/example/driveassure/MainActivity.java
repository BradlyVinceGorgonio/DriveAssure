package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginHomePage.class);
        startActivity(intent);
    }
}