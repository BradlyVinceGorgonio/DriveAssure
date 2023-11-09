package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class homepagelistings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepagelistings);

        // Retrieve data from the Intent
        String historyUid = getIntent().getStringExtra("historyUid");
        String CarpostUID = getIntent().getStringExtra("CarpostUID");



    }
}