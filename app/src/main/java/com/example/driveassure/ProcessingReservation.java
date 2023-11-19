package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProcessingReservation extends AppCompatActivity {

    String renterUID;
    String carUID;
    String requestID;
    String renterName;
    String carName;
    String ApprovedId;
    String isGcash;
    String renterUid;
    String DateStart;
    String DateEnd;
    String totalDays;
    String vehiclePrice;
    String VehicleTitle;
    ImageView renterProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing_reservation);

        Intent intent = getIntent();
        renterUID = intent.getStringExtra("renterUID");
        carUID = intent.getStringExtra("carUID");
        requestID = intent.getStringExtra("requestID");
        renterName = intent.getStringExtra("renterName");
        carName = intent.getStringExtra("carName");



        renterProfile = findViewById(R.id.renterProfile);

        // Image uploaded successfully
        Dialog dialog = new Dialog(ProcessingReservation.this);
        dialog.setContentView(R.layout.gobackhome);
        dialog.show();
        // Find the 'YES' button in the dialog layout
        Button yesButton = dialog.findViewById(R.id.OKayButton);
        TextView messageText  = dialog.findViewById(R.id.messageText);
        messageText.setText("Not yet time to start the timer! Rental day begins on [Event Date]. Please wait for the event day to commence the renting period. Thank you!");
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Close the dialog if needed
                Intent newActivityIntent = new Intent(ProcessingReservation.this, userHome.class);
                startActivity(newActivityIntent);
            }
        });
        GetItClean();
    }


    public void GetItClean()
    {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String uid = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference rentRequestDocument = db.collection("users")
                .document(uid)
                .collection("renter-ready")
                .document(requestID);

        rentRequestDocument.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            ApprovedId = document.getString("Approved id");
                            isGcash = document.getString("isGcash");
                            renterUid = document.getString("renter uid");
                            carUID = document.getString("Car To Request");

                            DateStart = document.getString("Date Start");
                            DateEnd = document.getString("Date End");
                            totalDays= document.getString("Total Time");


                            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("users/" + renterUID + "/face.jpg");

                            final String[] tempProfilePictureUrl = {"face.jpg"};  // Declare a final temporary variable

                            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                tempProfilePictureUrl[0] = uri.toString();  // Assign the value to the temporary variable
                                String storageUrl = uri.toString();
                                // Use Glide to load the image into the ImageView
                                Glide.with(this)
                                        .load(storageUrl)
                                        .placeholder(R.drawable.personvector)  // Placeholder image while loading (optional)
                                        .error(R.drawable.personvector)       // Error image if the loading fails (optional)
                                        .into(renterProfile);


                            }).addOnFailureListener(e -> {
                                // Handle any errors that occur while fetching the profile picture URL
                            });

                            FirebaseFirestore dbs = FirebaseFirestore.getInstance();
                            DocumentReference rentRequestDocuments = dbs.collection("car-posts").document(carUID);

                            rentRequestDocuments.get()
                                    .addOnCompleteListener(tasks -> {
                                        if (tasks.isSuccessful()) {
                                            DocumentSnapshot documents = tasks.getResult();
                                            if (documents.exists()) {
                                                // DocumentSnapshot data is available here
                                                VehicleTitle = documents.getString("Vehicle Title");
                                                vehiclePrice = documents.getString("Vehicle Price");


                                                int daysInt = Integer.parseInt(totalDays);
                                                int priceInt = Integer.parseInt(vehiclePrice);
                                                int totalPriceInt = daysInt * priceInt;
                                                String displayme = String.valueOf(totalPriceInt);
                                                //totalPrice.setText("₱ " + displayme );


                                                // Now you can use the retrieved data as needed
                                                // For example, update UI elements with the data
                                            } else {
                                                // The document does not exist
                                                // Handle accordingly
                                            }
                                        } else {
                                            // An error occurred while fetching the document
                                            // Handle accordingly
                                        }
                                    });

                            FirebaseFirestore dbss = FirebaseFirestore.getInstance();
                            DocumentReference rentRequestDocumentss = dbss.collection("users").document(renterUid);
                            rentRequestDocumentss.get()
                                    .addOnCompleteListener(tasks -> {
                                        if (tasks.isSuccessful()) {
                                            DocumentSnapshot documents = tasks.getResult();
                                            if (documents.exists()) {
                                                renterName = documents.getString("name");

                                            }
                                        }
                                    });

                        }
                    } else {
                        // Handle the failure scenario
                        // For example, log an error message
                    }
                });
    }
}