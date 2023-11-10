package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class homepagelistings extends AppCompatActivity {


    private ViewPager viewPager;
    private ImageSliderAdapter imageSliderAdapter;
    private StorageReference storageRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepagelistings);

        // Retrieve data from the Intent
        String historyUid = getIntent().getStringExtra("historyUid");
        String CarpostUID = getIntent().getStringExtra("CarpostUID");
        Log.d("NOBELATO", "Carpost UID " + CarpostUID);

        // Initialize Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("carposts/" + CarpostUID + "/");

        // Initialize ViewPager
        viewPager = findViewById(R.id.imageSlider);
        imageSliderAdapter = new ImageSliderAdapter(this);
        viewPager.setAdapter(imageSliderAdapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager, true);

        // Load images from Firebase Storage
        loadImagesFromFirebase();
    }
    private void loadImagesFromFirebase() {
        storageRef.listAll()
                .addOnSuccessListener(listResult -> {
                    for (StorageReference item : listResult.getItems()) {
                        // Get download URL for each image
                        item.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Add the download URL to the adapter
                            imageSliderAdapter.addImageUrl(uri.toString());
                            // Notify the adapter that the data has changed
                            imageSliderAdapter.notifyDataSetChanged();
                        }).addOnFailureListener(exception -> {
                            // Handle any errors
                        });
                    }
                })
                .addOnFailureListener(exception -> {
                    // Handle any errors
                });
    }
}