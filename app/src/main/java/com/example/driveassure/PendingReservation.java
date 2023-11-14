package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

public class PendingReservation extends AppCompatActivity {
String requestId;
String userId;
String carPostUid;
String pickUpArea;
String returnArea;
String DateStart;
String DateEnd;
String TimeStart;
String TimeEnd;
String ownerUserUidResult;
String totalTime;
String requestName;
String VehicleTitle;
String ContactNumber;

TextView renterName;
TextView renterContact;
TextView datePickerFromButton;
TextView timeStartedFrom;
TextView pickUpLocation;
TextView datePickerUntilButton;
TextView timeFinishedFrom;
TextView returnLocation;
Button rejectBtn;
Button acceptBtn;

ImageView renterProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_reservation);

        renterName = findViewById(R.id.renterName);
        renterContact = findViewById(R.id.renterContact);
        datePickerFromButton = findViewById(R.id.datePickerFromButton);
        timeStartedFrom = findViewById(R.id.timeStartedFrom);
        pickUpLocation =findViewById(R.id.pickUpLocation);
        datePickerUntilButton = findViewById(R.id.datePickerUntilButton);
        timeFinishedFrom = findViewById(R.id.timeFinishedFrom);
        returnLocation = findViewById(R.id.returnLocation);
        rejectBtn = findViewById(R.id.rejectBtn);
        acceptBtn = findViewById(R.id.acceptBtn);

        renterProfile = findViewById(R.id.renterProfile);


        fetchDataFromFirestore();
    }
    public void fetchDataFromFirestore()
    {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String uid = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference rentRequestCollection = db.collection("users")
                .document(uid)
                .collection("owner-view-rent-request");

        rentRequestCollection.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            requestId = document.getString("Request Id");

                            userId = document.getString("uid ");
                            carPostUid = document.getString("Car To Request");
                            pickUpArea = document.getString("Pickup Location");
                            ownerUserUidResult = document.getString("Car Owner uid");
                            totalTime = document.getString("Total Time");

                            returnArea = document.getString("Return Location");
                            DateStart = document.getString("Date Start");
                            DateEnd = document.getString("Date End");
                            TimeStart = document.getString("Time Start");
                            TimeEnd = document.getString("Time End");

                            // Assuming "uid" is the UID you want to fetch data for
                            DocumentReference userDocument = db.collection("users").document(uid);

                            userDocument.get().addOnCompleteListener(tasks -> {
                                if (tasks.isSuccessful()) {
                                    DocumentSnapshot documente = tasks.getResult();
                                    if (documente.exists()) {
                                        // Access the data here
                                        requestName = documente.getString("name");
                                        ContactNumber = documente.getString("contact number");



                                        // Assuming "uid" is the UID you want to fetch data for
                                        DocumentReference userDocumente = db.collection("car-posts").document(carPostUid);

                                        userDocumente.get().addOnCompleteListener(taskse -> {
                                            if (taskse.isSuccessful()) {
                                                DocumentSnapshot documentre = taskse.getResult();
                                                if (documentre.exists()) {
                                                    // Access the data here
                                                    VehicleTitle = documentre.getString("Vehicle Title");

                                                    renterName.setText(requestName);
                                                    renterContact.setText(ContactNumber);
                                                    datePickerFromButton.setText(DateStart);
                                                    timeStartedFrom.setText(TimeStart);
                                                    pickUpLocation.setText(pickUpArea);
                                                    datePickerUntilButton.setText(DateEnd);
                                                    timeFinishedFrom.setText(TimeEnd);
                                                    returnLocation.setText(returnArea);

                                                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("users/" + uid + "/face.jpg");

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

                                                    // Use the fetched data as needed
                                                } else {
                                                    // Document does not exist
                                                    // Handle this case as needed
                                                }
                                            } else {
                                                // Handle the failure scenario
                                                // For example, log an error message
                                            }
                                        });

                                        // Use the fetched data as needed
                                    } else {
                                        // Document does not exist
                                        // Handle this case as needed
                                    }
                                } else {
                                    // Handle the failure scenario
                                    // For example, log an error message
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