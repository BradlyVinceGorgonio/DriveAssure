package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CarInquiriesAcceptReject extends AppCompatActivity implements RequestingRentAdapter.OnItemClickListener {

    private List<RequestingClass> RequestingList;
    private RequestingRentAdapter requestingRentAdapter;


    ProgressBar progressBarID;

    CardView pendingReservation;
    CardView approvedReservation;
    CardView processingReservation;

    int mycondition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_inquiries_accept_reject);

        pendingReservation = findViewById(R.id.pendingReservation);
        approvedReservation = findViewById(R.id.approvedReservation);
        processingReservation = findViewById(R.id.processingReservation);

        progressBarID = findViewById(R.id.progressBarID);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        RecyclerView recyclerView = findViewById(R.id.rentingStatus);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RequestingList = new ArrayList<>();
        requestingRentAdapter = new RequestingRentAdapter(this, RequestingList, this);
        recyclerView.setAdapter(requestingRentAdapter);

        // Fetch data from Firestore and populate adminList
        // ...

        pendingReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchDataFromFirestore("owner-view-rent-request");
                mycondition = 0;
            }
        });
        approvedReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchDataFromFirestore1("renters-approved");
                mycondition = 1;
            }
        });
        processingReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchDataFromFirestore("processing");
            }
        });
        //fetchDataFromFirestore("owner-view-rent-request");
    }
    public void fetchDataFromFirestore(String choice)
    {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String uid = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference rentRequestCollection = db.collection("users")
                .document(uid)
                .collection(choice);

        rentRequestCollection.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        RequestingList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String requestId = document.getString("Request Id");
                            String userId = document.getString("uid ");
                            String carPostUid = document.getString("Car To Request");
                            String pickUpArea = document.getString("Pickup Location");
                            String ownerUserUidResult = document.getString("Car Owner uid");
                            String totalTime = document.getString("Total Time");

                            // Assuming "uid" is the UID you want to fetch data for
                            DocumentReference userDocument = db.collection("users").document(userId);

                            userDocument.get().addOnCompleteListener(tasks -> {
                                if (tasks.isSuccessful()) {
                                    DocumentSnapshot documente = tasks.getResult();
                                    if (documente.exists()) {
                                        // Access the data here
                                        String requestName = documente.getString("name");

                                        // Assuming "uid" is the UID you want to fetch data for
                                        DocumentReference userDocumente = db.collection("car-posts").document(carPostUid);

                                        userDocumente.get().addOnCompleteListener(taskse -> {
                                            if (taskse.isSuccessful()) {
                                                DocumentSnapshot documentre = taskse.getResult();
                                                if (documentre.exists()) {
                                                    // Access the data here
                                                    String VehicleTitle = documentre.getString("Vehicle Title");

                                                   fetchProfilePictureUrl(requestName, pickUpArea, totalTime, VehicleTitle, userId, "/" + "face.jpg", carPostUid, requestId);
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
    public void fetchDataFromFirestore1(String choice)
    {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String uid = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference rentRequestCollection = db.collection("users")
                .document(uid)
                .collection(choice);

        rentRequestCollection.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        RequestingList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String requestId = document.getString("Approved Id");
                            String userId = document.getString("renter uid");
                            String carPostUid = document.getString("Car To Request");
                            String pickUpArea = document.getString("Pickup Location");
                            String totalTime = document.getString("Total Time");

                            // Assuming "uid" is the UID you want to fetch data for
                            DocumentReference userDocument = db.collection("users").document(userId);

                            userDocument.get().addOnCompleteListener(tasks -> {
                                if (tasks.isSuccessful()) {
                                    DocumentSnapshot documente = tasks.getResult();
                                    if (documente.exists()) {
                                        // Access the data here
                                        String requestName = documente.getString("name");

                                        // Assuming "uid" is the UID you want to fetch data for
                                        DocumentReference userDocumente = db.collection("car-posts").document(carPostUid);

                                        userDocumente.get().addOnCompleteListener(taskse -> {
                                            if (taskse.isSuccessful()) {
                                                DocumentSnapshot documentre = taskse.getResult();
                                                if (documentre.exists()) {
                                                    // Access the data here
                                                    String VehicleTitle = documentre.getString("Vehicle Title");

                                                    fetchProfilePictureUrl(requestName, pickUpArea, totalTime, VehicleTitle, userId, "/" + "face.jpg", carPostUid, requestId);
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
    private void fetchProfilePictureUrl(String name, String pickUpArea, String totalTime, String vehicleTitle, String uid, String profilePictureUrl, String carPostUid, String requestId) {
        Log.d("taeka", uid + " weird " + profilePictureUrl);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("users/" + uid + profilePictureUrl);

        final String[] tempProfilePictureUrl = {profilePictureUrl};  // Declare a final temporary variable

        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            tempProfilePictureUrl[0] = uri.toString();  // Assign the value to the temporary variable

            Log.d("MGAINFOS","withing void"+  requestId+"/"+ uid +"/"+ carPostUid +"/"+ pickUpArea +"/ \n" + "/"+ totalTime + "/" + name + "/"+ vehicleTitle);
            // Create a TrainerClass instance with the retrieved data
            RequestingClass rents = new RequestingClass(tempProfilePictureUrl[0], name, uid, vehicleTitle, totalTime, pickUpArea, carPostUid, requestId);
            RequestingList.add(rents);

            // Notify the adapter that data has changed
            requestingRentAdapter.notifyDataSetChanged();
            progressBarID.setVisibility(View.GONE);
        }).addOnFailureListener(e -> {
            // Handle any errors that occur while fetching the profile picture URL
        });
    }

    @Override
    public void onItemClick(RequestingClass rent) {
        String renterUID = rent.getUid();
        String carUID = rent.getCarUID();
        String requestID = rent.getRequestID();

        // Add your condition here
        if (mycondition == 0) {
            // Condition 1: Execute this block of code if the condition is true
            Intent intent = new Intent(CarInquiriesAcceptReject.this, PendingReservation.class);
            intent.putExtra("renterUID", renterUID);
            intent.putExtra("carUID", carUID);
            intent.putExtra("requestID", requestID);
            startActivity(intent);
        } else if(mycondition == 1) {
            // Condition 2: Execute this block of code if the condition is false
            Intent intent = new Intent(CarInquiriesAcceptReject.this, DriverSignature.class);
            intent.putExtra("renterUID", renterUID);
            intent.putExtra("carUID", carUID);
            intent.putExtra("requestID", requestID);
            startActivity(intent);
        }
    }

}