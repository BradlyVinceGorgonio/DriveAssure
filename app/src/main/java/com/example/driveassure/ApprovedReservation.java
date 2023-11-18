package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ApprovedReservation extends AppCompatActivity {

    String renterUID;
    String carUID;
    String requestID;
    String ApprovedId;
    String isGcash;
    String renterUid;
    ImageView ValidID;
    ImageView uploadedPayment;
    ProgressBar ProgressBarFaceId;
    TextView onTheDayPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_reservation);

        Intent intent = getIntent();
        renterUID = intent.getStringExtra("renterUID");
        carUID = intent.getStringExtra("carUID");
        requestID = intent.getStringExtra("requestID");
        Log.d("ANOAPPROVEDID", "onCreate: " + requestID + " \n " + carUID + " \n " + renterUID);



        ValidID = findViewById(R.id.ValidID);
        uploadedPayment = findViewById(R.id.uploadedPayment);
        ProgressBarFaceId = findViewById(R.id.ProgressBarFaceId);
        onTheDayPayment = findViewById(R.id.onTheDayPayment);





        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("users-valid-id/" + renterUID + "/front.jpg");

        final String[] tempProfilePictureUrl = {"front.jpg"};  // Declare a final temporary variable

        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {

            tempProfilePictureUrl[0] = uri.toString();  // Assign the value to the temporary variable
            String storageUrl = uri.toString();
            // Use Glide to load the image into the ImageView
            Glide.with(this)
                    .load(storageUrl)
                    .placeholder(R.drawable.personvector)  // Placeholder image while loading (optional)
                    .error(R.drawable.personvector)       // Error image if the loading fails (optional)
                    .into(ValidID);
            ProgressBarFaceId.setVisibility(View.GONE);


        }).addOnFailureListener(e -> {
            // Handle any errors that occur while fetching the profile picture URL
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
                .collection("renter-processing")
                .document(requestID);

        rentRequestDocument.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            ApprovedId = document.getString("Approved id");
                            isGcash = document.getString("isGcash");
                            renterUid = document.getString("renter uid");

                            if(isGcash.equals("true"))
                            {
                                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("users-valid-id/" + renterUID + "/gcash.jpg");

                                final String[] tempProfilePictureUrl = {"front.jpg"};  // Declare a final temporary variable

                                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    tempProfilePictureUrl[0] = uri.toString();  // Assign the value to the temporary variable
                                    String storageUrl = uri.toString();
                                    // Use Glide to load the image into the ImageView
                                    Glide.with(this)
                                            .load(storageUrl)
                                            .placeholder(R.drawable.personvector)  // Placeholder image while loading (optional)
                                            .error(R.drawable.personvector)       // Error image if the loading fails (optional)
                                            .into(uploadedPayment);


                                }).addOnFailureListener(e -> {
                                    // Handle any errors that occur while fetching the profile picture URL
                                });

                            }
                            else if(isGcash.equals("false"))
                            {
                                onTheDayPayment.setVisibility(View.VISIBLE);

                            }
                        }
                    } else {
                        // Handle the failure scenario
                        // For example, log an error message
                    }
                });
    }
}