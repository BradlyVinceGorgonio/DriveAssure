package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;
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
    String requestName;
    String VehicleTitle;
    String requestId;
    String userId;
    String carPostUid;
    String pickUpArea;
    String ownerUserUidResult;
    String totalTime;
    ProgressBar progressBarID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_inquiries_accept_reject);

        progressBarID = findViewById(R.id.progressBarID);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        RecyclerView recyclerView = findViewById(R.id.rentingStatus);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RequestingList = new ArrayList<>();
        requestingRentAdapter = new RequestingRentAdapter(this, RequestingList, this);
        recyclerView.setAdapter(requestingRentAdapter);

        // Fetch data from Firestore and populate adminList
        // ...
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

                            // Assuming "uid" is the UID you want to fetch data for
                            DocumentReference userDocument = db.collection("users").document(uid);

                            userDocument.get().addOnCompleteListener(tasks -> {
                                if (tasks.isSuccessful()) {
                                    DocumentSnapshot documente = tasks.getResult();
                                    if (documente.exists()) {
                                        // Access the data here
                                        requestName = documente.getString("name");

                                        // Assuming "uid" is the UID you want to fetch data for
                                        DocumentReference userDocumente = db.collection("car-posts").document(carPostUid);

                                        userDocumente.get().addOnCompleteListener(taskse -> {
                                            if (taskse.isSuccessful()) {
                                                DocumentSnapshot documentre = taskse.getResult();
                                                if (documentre.exists()) {
                                                    // Access the data here
                                                    VehicleTitle = documentre.getString("Vehicle Title");

                                                    Log.d("MGAINFOS",  requestId+"/"+ userId +"/"+ carPostUid +"/"+ pickUpArea +"/ \n"+ ownerUserUidResult + "/"+ totalTime + "/" + requestName + "/"+ VehicleTitle);
                                                   fetchProfilePictureUrl(requestName, pickUpArea, totalTime, VehicleTitle, uid, "/" + "face.jpg", carPostUid, requestId);
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
        String renterUID = rent.getUid(); // Assuming AdminClass has a getUid() method to retrieve the admin ID
        String carUID = rent.getCarUID();
        String requestID = rent.getRequestID();
        Intent intent = new Intent(CarInquiriesAcceptReject.this, PendingReservation.class);
        intent.putExtra("renterUID", renterUID); // Pass the admin ID to the AcceptRejectAdminActivity
        intent.putExtra("carUID", carUID);
        intent.putExtra("requestID", requestID);

        startActivity(intent);
    }
}