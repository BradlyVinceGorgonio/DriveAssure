package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DetailedCarOwnerListingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_car_owner_listings);

        // Retrieve data from the Intent
        String historyUid = getIntent().getStringExtra("historyUid");
        String CarpostUID = getIntent().getStringExtra("CarpostUID");

        // Now you have the data in historyUid and CarpostUID

        TextView uid = findViewById(R.id.deleteME);
        uid.setText(historyUid + " \n" + CarpostUID);


        //USE public class ImageSliderAdapter for loading the image
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^
        //USE homepagelistings.java AS Reference
        //^^^^^^^^^^^^^^
    }
}