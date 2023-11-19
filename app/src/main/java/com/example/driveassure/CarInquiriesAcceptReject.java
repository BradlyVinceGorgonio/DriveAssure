package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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

//
//    private CardView pendingReservationCardView;
//    private CardView approvedReservationCardView;
//    private CardView processingReservationCardView;
    private CardView mainCardView;


    ProgressBar progressBarID;

    CardView pendingReservation;
    CardView approvedReservation;
    CardView processingReservation;

    LinearLayout displayNoView;

    int mycondition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_inquiries_accept_reject);

        pendingReservation = findViewById(R.id.pendingReservation);
        approvedReservation = findViewById(R.id.approvedReservation);
        processingReservation = findViewById(R.id.processingReservation);

        displayNoView = findViewById(R.id.displayNoView);

        progressBarID = findViewById(R.id.progressBarID);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        RecyclerView recyclerView = findViewById(R.id.rentingStatus);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RequestingList = new ArrayList<>();
        requestingRentAdapter = new RequestingRentAdapter(this, RequestingList, this);
        recyclerView.setAdapter(requestingRentAdapter);

        // Fetch data from Firestore and populate adminList
        // ...

//        pendingReservationCardView = findViewById(R.id.pendingReservation);
//        approvedReservationCardView = findViewById(R.id.approvedReservation);
//        processingReservationCardView = findViewById(R.id.processingReservation);
        mainCardView = findViewById(R.id.cardView22);
//
//        pendingReservationCardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Change the background color of mainCardView to light green
//                mainCardView.setCardBackgroundColor(getResources().getColor(R.color.grey));
//            }
//        });
//
//        approvedReservationCardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Change the background color of mainCardView to blue
//                mainCardView.setCardBackgroundColor(getResources().getColor(R.color.blue));
//            }
//        });
//
//        processingReservationCardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Change the background color of mainCardView to yellow
//                mainCardView.setCardBackgroundColor(getResources().getColor(R.color.lightgreen));
//            }
//        });


        pendingReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayNoView.setVisibility(View.GONE);
                fetchDataFromFirestore("owner-view-rent-request");
                mycondition = 0;
                animateCardViewColorChange(mainCardView, getResources().getColor(R.color.grey));

            }
        });

        approvedReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayNoView.setVisibility(View.GONE);
                fetchDataFromFirestore1("renter-processing");
                mycondition = 1;
                animateCardViewColorChange(mainCardView, getResources().getColor(R.color.dirtywhite));
            }
        });
        processingReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayNoView.setVisibility(View.GONE);
                fetchDataFromFirestore2("renter-ready");
                mycondition = 2;
                animateCardViewColorChange(mainCardView, getResources().getColor(R.color.lightgreen));
            }
        });
        fetchDataFromFirestore("owner-view-rent-request");
    }

    private void animateCardViewColorChange(CardView cardView, int targetColor) {
        ObjectAnimator colorAnimator = ObjectAnimator.ofObject(cardView, "cardBackgroundColor",
                new ArgbEvaluator(), ((CardView) cardView).getCardBackgroundColor().getDefaultColor(), targetColor);
        colorAnimator.setDuration(500);
        colorAnimator.start();
    }

    public void fetchDataFromFirestore(String choice) {

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

                        // Check if the result set is empty
                        if (task.getResult().isEmpty()) {
                            // Handle the case where the document is empty
                            displayNoView.setVisibility(View.VISIBLE);
                        } else {
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
                        }
                    } else {
                        // Handle the failure scenario
                        // For example, log an error message
                    }
                });
    }

    public void fetchDataFromFirestore1(String choice) {

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

                        // Check if the result set is empty
                        if (task.getResult().isEmpty()) {
                            // Handle the case where the document is empty
                            displayNoView.setVisibility(View.VISIBLE);
                        } else {
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

                                        }
                                    } else {
                                        // Handle the failure scenario
                                        // For example, log an error message
                                    }
                                });


                            }
                        }
                    } else {
                        // Handle the failure scenario
                        // For example, log an error message
                    }
                });
    }

    public void fetchDataFromFirestore2(String choice) {

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

                        // Check if the result set is empty
                        if (task.getResult().isEmpty()) {
                            // Handle the case where the document is empty
                            displayNoView.setVisibility(View.VISIBLE);
                        } else {
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

                                        }
                                    } else {
                                        // Handle the failure scenario
                                        // For example, log an error message
                                    }
                                });


                            }
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
        String name = rent.getName();
        String carName = rent.getCarName();

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
            Intent intent = new Intent(CarInquiriesAcceptReject.this, ApprovedReservation.class);
            intent.putExtra("renterUID", renterUID);
            intent.putExtra("carUID", carUID);
            intent.putExtra("requestID", requestID);
            intent.putExtra("renterName", name);
            intent.putExtra("carName", carName);
            startActivity(intent);
        }
        else if(mycondition == 2) {
            // Condition 2: Execute this block of code if the condition is false
            Intent intent = new Intent(CarInquiriesAcceptReject.this, ProcessingReservation.class);
            intent.putExtra("renterUID", renterUID);
            intent.putExtra("carUID", carUID);
            intent.putExtra("requestID", requestID);
            intent.putExtra("renterName", name);
            intent.putExtra("carName", carName);
            startActivity(intent);
        }
    }

}