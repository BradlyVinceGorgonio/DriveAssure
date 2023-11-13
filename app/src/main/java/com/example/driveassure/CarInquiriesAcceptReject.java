package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CarInquiriesAcceptReject extends AppCompatActivity implements RequestingRentAdapter.OnItemClickListener {

    private List<RequestingClass> RequestingList;
    private RequestingRentAdapter requestingRentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_inquiries_accept_reject);

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
                            String requestId = document.getString("Request Id");
                            String userId = document.getString("uid");
                            String carPostUid = document.getString("Car To Request");
                            String startedTime = document.getString("Time Start");
                            String finishedTime = document.getString("Time End");
                            String selectedDateFrom = document.getString("Date Start");
                            String selectedDateUntil = document.getString("Date End");
                            String pickUpArea = document.getString("Pickup Location");
                            String returnArea = document.getString("Return Location");
                            String ownerUserUidResult = document.getString("Car Owner uid");


                        }
                    } else {
                        // Handle the failure scenario
                        // For example, log an error message
                    }
                });
    }
    @Override
    public void onItemClick(RequestingClass rent) {
        String renterUID = rent.getUid(); // Assuming AdminClass has a getUid() method to retrieve the admin ID
        String carUID = rent.getCarUID();
        Intent intent = new Intent(CarInquiriesAcceptReject.this, adminAcceptReject.class);
        intent.putExtra("renterUID", renterUID); // Pass the admin ID to the AcceptRejectAdminActivity
        intent.putExtra("carUID", carUID);
        startActivity(intent);
    }
}