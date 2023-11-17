package com.example.driveassure;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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

public class renterUserFragment extends Fragment implements OwnerListingCarAdapter.OnItemClickListener {


    private List<OwnerListingsClass> HistoryList;
    private List<OwnerListingsClass> HistoryList1;
    private OwnerListingCarAdapter ownerListingCarAdapter;
    private OwnerListingCarAdapter ownerListingCarAdapter1;

    LinearLayout displayNoBookmarks;
    LinearLayout displayNoInquiries;
    ProgressBar progressBarID1;
    ProgressBar progressBarID3;
    RecyclerView recyclerView;
    ImageView bigIconImageView;
    RecyclerView approvedRenter;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    String ApprovedId;
    String carOwnerId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_renter_user, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        displayNoBookmarks = view.findViewById(R.id.displayNoWaitings);
        progressBarID1 = view.findViewById(R.id.progressBarID1);
        progressBarID3 = view.findViewById(R.id.progressBarID3);
        displayNoInquiries = view.findViewById(R.id.displayNoInquiries);

        recyclerView = view.findViewById(R.id.waitingRenter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));  // Add closing parenthesis here

        HistoryList = new ArrayList<>();
        ownerListingCarAdapter = new OwnerListingCarAdapter(getContext(), HistoryList, this);
        recyclerView.setAdapter(ownerListingCarAdapter);


        approvedRenter = view.findViewById(R.id.approvedRenter);
        approvedRenter.setLayoutManager(new LinearLayoutManager(getContext()));  // Add closing parenthesis here

        HistoryList1 = new ArrayList<>();
        ownerListingCarAdapter1 = new OwnerListingCarAdapter(getContext(), HistoryList1, this);
        approvedRenter.setAdapter(ownerListingCarAdapter1);



        fetchVehicleLikes();
        fetchVehicleLikes1();

        return view;
    }
    @Override
    public void onItemClick(OwnerListingsClass history) {
        String historyUid = history.getUid(); // Assuming HomeListingClass has a getUid() method
        String carpostUID = history.getCarpostUID();



            Intent intent2 = new Intent(getContext(), rentalTermsandCondition.class);
            // Pass data as extras in the intent
            intent2.putExtra("historyUid", historyUid);
            intent2.putExtra("CarpostUID", carpostUID);
            intent2.putExtra("ApprovedId", ApprovedId);
        intent2.putExtra("CarOwnerId", carOwnerId);
            startActivity(intent2);
    }
    public void fetchVehicleLikes() {
        if (auth.getCurrentUser() != null) {
            // Get the current user's UID
            String uid = auth.getCurrentUser().getUid();

            // Reference to the "vehicle-request" subcollection under the user's document
            CollectionReference vehicleRequestRef = db.collection("users").document(uid).collection("vehicle-request");

            // Fetch the documents from the "vehicle-request" subcollection
            vehicleRequestRef.get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        // Check if there are any documents in the subcollection
                        if (!queryDocumentSnapshots.isEmpty()) {
                            HistoryList.clear();
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                // Get the "Car To Request" field value from the subcollection document
                                String vehicleLike = documentSnapshot.getString("Car To Request");

                                // Log "Car To Request" value being fetched
                                Log.d("FETCH_VEHICLE_LIKE", "Fetching document with Car To Request: " + vehicleLike);

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
                            // Log if the subcollection is empty
                            Log.d("FETCH_VEHICLE_LIKE", "Subcollection is empty");
                            progressBarID1.setVisibility(View.GONE);
                            displayNoBookmarks.setVisibility(View.VISIBLE);
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Log if there is an error fetching the subcollection documents
                        Log.e("FETCH_VEHICLE_LIKE", "Error fetching subcollection documents: " + e.getMessage());
                    });
        }
    }

    public void fetchVehicleLikes1() {
        if (auth.getCurrentUser() != null) {
            // Get the current user's UID
            String uid = auth.getCurrentUser().getUid();

            // Reference to the "vehicle-request" subcollection under the user's document
            CollectionReference vehicleRequestRef = db.collection("users").document(uid).collection("vehicle-approved");

            // Fetch the documents from the "vehicle-request" subcollection
            vehicleRequestRef.get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        // Check if there are any documents in the subcollection
                        if (!queryDocumentSnapshots.isEmpty()) {
                            HistoryList1.clear();
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                // Get the "Car To Request" field value from the subcollection document
                                String vehicleLike = documentSnapshot.getString("Car To Request");
                                ApprovedId = documentSnapshot.getString("Approved Id");
                                carOwnerId = documentSnapshot.getString("Car Owner uid");
                                // Log "Car To Request" value being fetched
                                Log.d("FETCH_VEHICLE_LIKE", "Fetching document with Car To Request: " + vehicleLike);

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

                                                fetchProfilePictureUrl1(name, Price, Address, uids, "/carpic1.jpg", Transmission, carPostUID);

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
                            // Log if the subcollection is empty
                            Log.d("FETCH_VEHICLE_LIKE", "Subcollection is empty");
                            progressBarID3.setVisibility(View.GONE);
                            displayNoInquiries.setVisibility(View.VISIBLE);
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Log if there is an error fetching the subcollection documents
                        Log.e("FETCH_VEHICLE_LIKE", "Error fetching subcollection documents: " + e.getMessage());
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


            progressBarID1.setVisibility(View.GONE);

        }).addOnFailureListener(e -> {
            // Handle any errors that occur while fetching the profile picture URL
        });
    }
    private void fetchProfilePictureUrl1(String name, String Price, String Address, String uid, String profilePictureUrl, String Transmission, String carPostUID) {
        StorageReference storageRef= FirebaseStorage.getInstance().getReference().child( "carposts/"+ carPostUID+ "/" +"carpic1.jpg");

        final String[] tempProfilePictureUrl = {profilePictureUrl};  // Declare a final temporary variable

        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            tempProfilePictureUrl[0] = uri.toString();  // Assign the value to the temporary variable

            Log.d("BITCHNIGGA", "LOOB NAKO NIGGER: " +  tempProfilePictureUrl[0]);
            // Create a TrainerClass instance with the retrieved data
            OwnerListingsClass history = new OwnerListingsClass(tempProfilePictureUrl[0], name, uid, "₱ "+Price + " /day", Address, Transmission, carPostUID);

            HistoryList1.add(history);

            // Notify the adapter that data has changed
            // Notify the adapter that data has changed

            ownerListingCarAdapter1.notifyDataSetChanged();


            progressBarID3.setVisibility(View.GONE);

        }).addOnFailureListener(e -> {
            // Handle any errors that occur while fetching the profile picture URL
        });
    }
}