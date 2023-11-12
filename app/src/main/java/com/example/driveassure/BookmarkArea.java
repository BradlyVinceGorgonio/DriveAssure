package com.example.driveassure;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class BookmarkArea extends AppCompatActivity implements OwnerListingCarAdapter.OnItemClickListener {

    private List<OwnerListingsClass> HistoryList;
    private OwnerListingCarAdapter ownerListingCarAdapter;

    ProgressBar progressBarID;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_area);
        // Initialize Firebase components
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        progressBarID = findViewById(R.id.ProgressBarBookMark);

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        RecyclerView recyclerView = findViewById(R.id.bookmarkRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));  // Add closing parenthesis here

        HistoryList = new ArrayList<>();
        ownerListingCarAdapter = new OwnerListingCarAdapter(this, HistoryList, this);
        recyclerView.setAdapter(ownerListingCarAdapter);


        fetchVehicleLikes();
    }
    @Override
    public void onItemClick(OwnerListingsClass history) {
        String historyUid = history.getUid(); // Assuming HomeListingClass has a getUid() method
        String carpostUID = history.getCarpostUID();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null && currentUser.getUid().equals(historyUid)) {
            // The user's UID matches the historyUid, open intent2
            Intent intent2 = new Intent(BookmarkArea.this, DetailedCarOwnerListingsActivity.class);
            // Pass data as extras in the intent
            intent2.putExtra("historyUid", historyUid);
            intent2.putExtra("CarpostUID", carpostUID);
            startActivity(intent2);
        } else {
            // The user's UID does not match the historyUid, open intent1
            Intent intent = new Intent(BookmarkArea.this, homepagelistings.class);
            // Pass data as extras in the intent
            intent.putExtra("historyUid", historyUid);
            intent.putExtra("CarpostUID", carpostUID);
            startActivity(intent);
        }
    }
    public void fetchVehicleLikes() {
        if (auth.getCurrentUser() != null) {
            // Get the current user's UID
            String uid = auth.getCurrentUser().getUid();

            // Reference to the "users" collection and the document with the user's UID
            DocumentReference userDocRef = db.collection("users").document(uid);

            // Fetch the "vehicleLikes" array from the user's document
            userDocRef.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Get the array contents directly as List<String>
                            List<String> vehicleLikesList = (List<String>) documentSnapshot.get("vehicle likes");
                            if (vehicleLikesList != null && !vehicleLikesList.isEmpty()) {
                                // Process the contents of the "vehicleLikes" array
                                HistoryList.clear();
                                for (String vehicleLike : vehicleLikesList) {
                                    // Log document ID being fetched
                                    Log.d("FETCH_VEHICLE_LIKE", "Fetching document with ID: " + vehicleLike);

                                    // Reference to a document in "car-posts"
                                    DocumentReference carPostDocRef = db.collection("car-posts").document(vehicleLike);

                                    carPostDocRef.get()
                                            .addOnSuccessListener(carPostDocumentSnapshot -> {
                                                if (carPostDocumentSnapshot.exists()) {
                                                    // Log data from "car-posts" document
                                                    Log.d("FETCH_VEHICLE_LIKE", "Document data: " + carPostDocumentSnapshot.getData());



                                                    // Process the data from the "car-posts" document
                                                    // Example: String carModel = carPostDocumentSnapshot.getString("model");
                                                    // Do something with the car data...

                                                    String name = carPostDocumentSnapshot.getString("Vehicle Title");
                                                    String carPostUID = carPostDocumentSnapshot.getString("Vehicle post-id");
                                                    String uids = carPostDocumentSnapshot.getString("uid");
                                                    String Address = carPostDocumentSnapshot.getString("Vehicle Address");
                                                    String Price = carPostDocumentSnapshot.getString("Vehicle Price");
                                                    String Transmission = carPostDocumentSnapshot.getString("Vehicle Transmission");

                                                    fetchProfilePictureUrl(name, Price, Address, uids, "/carpic1.jpg", Transmission, carPostUID);

                                                } else {
                                                    // Log if the document doesn't exist
                                                    Log.d("FETCH_VEHICLE_LIKE", "Document does not exist");
                                                }
                                            })
                                            .addOnFailureListener(e -> {
                                                // Log if there is an error fetching the document
                                                Log.e("FETCH_VEHICLE_LIKE", "Error fetching document: " + e.getMessage());
                                            });
                                }
                            } else {
                                // Log if the "vehicleLikes" array is null
                                Log.d("FETCH_VEHICLE_LIKE", "vehicleLikesList is null");
                                progressBarID.setVisibility(View.GONE);
                                LinearLayout displayNoBookmarks = findViewById(R.id.displayNoBookmarks);
                                displayNoBookmarks.setVisibility(View.VISIBLE);
                            }
                        } else {
                            // Log if the user document doesn't exist
                            Log.d("FETCH_VEHICLE_LIKE", "User document does not exist");
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Log if there is an error fetching the user document
                        Log.e("FETCH_VEHICLE_LIKE", "Error fetching user document: " + e.getMessage());
                    });
        }
    }
    private void fetchProfilePictureUrl(String name, String Price, String Address, String uid, String profilePictureUrl, String Transmission, String carPostUID) {
        StorageReference storageRef= FirebaseStorage.getInstance().getReference().child( "carposts/"+ carPostUID+ "/" +"carpic1.jpg");

        final String[] tempProfilePictureUrl = {profilePictureUrl};  // Declare a final temporary variable

        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            tempProfilePictureUrl[0] = uri.toString();  // Assign the value to the temporary variable

            Log.d("BITCHNIGGA", "LOOB NAKO NIGGER: " +  tempProfilePictureUrl[0]);
            // Create a TrainerClass instance with the retrieved data
            OwnerListingsClass history = new OwnerListingsClass(tempProfilePictureUrl[0], name, uid, "₱ "+Price + " /day", Address, Transmission, carPostUID);

            HistoryList.add(history);

            // Notify the adapter that data has changed
            // Notify the adapter that data has changed

            ownerListingCarAdapter.notifyDataSetChanged();


            progressBarID.setVisibility(View.GONE);

        }).addOnFailureListener(e -> {
            // Handle any errors that occur while fetching the profile picture URL
        });
    }

}
