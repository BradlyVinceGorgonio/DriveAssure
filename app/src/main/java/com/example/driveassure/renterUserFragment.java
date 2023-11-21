package com.example.driveassure;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
    RelativeLayout noReady;
    RelativeLayout Ready;

    ImageView renterProfile;
    TextView renterNames;
    TextView renterContact;
    TextView vehicleTitle;
    TextView vehiclePrices;
    TextView vehicleTransmission;
    TextView fuelType;
    TextView vehicBrand;
    TextView carOwnerName;
    TextView pickUpDate;
    TextView PickupTime;
    TextView PickupLocation;
    TextView returnrenterContact;
    TextView returnDate;
    TextView returnTime;
    TextView returnLocation;
    TextView exchangeContracts;
    TextView carOwnerrContact;
    TextView rentingDays;
    Button timeRemaining;

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

        noReady = view.findViewById(R.id.noReady);
        Ready = view.findViewById(R.id.Ready);

        checkReady();


        renterProfile = view.findViewById(R.id.renterProfile);
        renterNames = view.findViewById(R.id.renterName);
        renterContact = view.findViewById(R.id.renterContact);
        vehiclePrices = view.findViewById(R.id.vehiclePrice);
        vehicleTransmission = view.findViewById(R.id.vehicleTransmission);
        fuelType = view.findViewById(R.id.fuelType);
        vehicBrand = view.findViewById(R.id.vehicBrand);
        carOwnerName = view.findViewById(R.id.carOwnerName);
        pickUpDate = view.findViewById(R.id.pickUpDate);
        PickupTime = view.findViewById(R.id.PickupTime);
        PickupLocation = view.findViewById(R.id.PickupLocation);
        returnDate = view.findViewById(R.id.returnDate);
        returnTime = view.findViewById(R.id.returnTime);
        returnLocation = view.findViewById(R.id.returnLocation);
        carOwnerrContact =view.findViewById(R.id.carOwnerrContact);
        //rentingDays = view.findViewById(R.id.rentingDays);
        //totalPayment = view.findViewById(R.id.totalPayment);
        timeRemaining = view.findViewById(R.id.timeRemaining);

        timeRemaining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.carreturned);
                dialog.show();
                // Find the 'YES' button in the dialog layout
                Button yesButton = dialog.findViewById(R.id.OKayButton);
                Button noButton = dialog.findViewById(R.id.NotYetButton);
                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss(); // Close the dialog if needed
                    }
                });
                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Dialog dialogs = new Dialog(getContext());
                        dialogs.setContentView(R.layout.congratulations);
                        dialogs.show();
                        // Find the 'YES' button in the dialog layout
                        Button yesButton = dialogs.findViewById(R.id.OKayButton);
                        yesButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteAllVehicleReadyDocuments();
                                dialogs.dismiss(); // Close the dialog if needed
                                Intent newActivityIntent = new Intent(getContext(), userHome.class);
                                startActivity(newActivityIntent);
                            }
                        });

                    }
                });
            }
        });




        getDatas();


        return view;
    }

    // Function to delete all documents in "vehicle-ready" collection
    private void deleteAllVehicleReadyDocuments() {
        // Get the current user
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            // Get the UID of the current user
            String uid = currentUser.getUid();

            // Reference to the "vehicle-ready" collection for the current user
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(uid)
                    .collection("vehicle-ready")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Iterate through the documents and delete each one
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().delete()
                                        .addOnSuccessListener(aVoid -> {
                                            // Document successfully deleted
                                            System.out.println("Document successfully deleted!");
                                        })
                                        .addOnFailureListener(e -> {
                                            // Handle any errors
                                            System.err.println("Error deleting document: " + e.getMessage());
                                        });
                            }
                        } else {
                            // Handle errors in getting documents
                            System.err.println("Error getting documents: " + task.getException().getMessage());
                        }
                    });
        } else {
            // Handle the case where the current user is null (not authenticated)
            System.err.println("User not authenticated");
        }
    }
    public void getDatas()
    {

        // Initialize Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Get the current user
        FirebaseUser currentUser = mAuth.getCurrentUser();



        if (currentUser != null) {
            // Get the UID of the current user
            String uid = currentUser.getUid();

            // Reference to the "vehicle-ready" collection for the current user
            db.collection("users").document(uid)
                    .collection("vehicle-ready")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // Retrieve data from each document
                                    String ApprovedId = document.getString("Approved id");
                                    String isGcash = document.getString("isGcash");
                                    String renterUid = document.getString("renter uid");
                                    String carOwner = document.getString("Car Owner uid");
                                    String carUID = document.getString("Car To Request");
                                    String DateStart = document.getString("Date Start");
                                    String DateEnd = document.getString("Date End");
                                    String totalDays= document.getString("Total Time");
                                    String timeStart = document.getString("Time Start");
                                    String timeEnd = document.getString("Time End");
                                    String pickupLocation = document.getString("Pickup Location");
                                    String ReturnLocation = document.getString("Return Location");

                                    pickUpDate.setText(DateStart);
                                    PickupTime.setText(timeStart);
                                    PickupLocation.setText(pickupLocation);
                                    timeRemaining.setText("Renting ends on "+DateEnd);



                                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("users/" + uid + "/face.jpg");

                                    final String[] tempProfilePictureUrl = {"face.jpg"};  // Declare a final temporary variable

                                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                        tempProfilePictureUrl[0] = uri.toString();  // Assign the value to the temporary variable
                                        String storageUrl = uri.toString();
                                        // Use Glide to load the image into the ImageView
                                        Glide.with(getContext())
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
                                                        String VehicleTitle = documents.getString("Vehicle Title");
                                                        String vehiclePrice = documents.getString("Vehicle Price");
                                                        String transmission = documents.getString("Vehicle Transmission");
                                                        String fuel = documents.getString("Vehicle Fuel Type");
                                                        String brand = documents.getString("Vehicle Brand");

                                                        //vehiclePrices.setText(vehiclePrice);
                                                        vehicleTransmission.setText(transmission);
                                                        fuelType.setText(fuel);
                                                        vehicBrand.setText(brand);




//                                                        int daysInt = Integer.parseInt(totalDays);
//                                                        int priceInt = Integer.parseInt(vehiclePrice);
//                                                        int totalPriceInt = daysInt * priceInt;
//                                                        String displayme = String.valueOf(totalPriceInt);
//                                                        //totalPayment.setText("₱ " + displayme);


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
                                                        String renterName = documents.getString("name");
                                                        String contactNumber = documents.getString("contact number");

                                                        renterNames.setText(renterName);
                                                        renterContact.setText(contactNumber);
                                                        carOwnerName.setText("Michael Afton");
                                                        carOwnerrContact.setText(contactNumber);

                                                    }
                                                }
                                            });

                                }
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    public void checkReady()
    {
        // Get the current user
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser users = auth.getCurrentUser();

        // Get the Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Check if the "users" collection, current auth uid document, "vehicle-ready" subcollection contains any document
        if (users != null) {
            String userId = users.getUid();
            CollectionReference vehicleReadyCollection = db.collection("users").document(userId).collection("vehicle-ready");

            vehicleReadyCollection.get().addOnCompleteListener(tasks -> {
                if (tasks.isSuccessful()) {
                    QuerySnapshot querySnapshot = tasks.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        // Subcollection contains at least one document, set your boolean variable to true
                        Ready.setVisibility(View.VISIBLE);

                    }
                    else {
                        noReady.setVisibility(View.VISIBLE);
                        fetchVehicleLikes();
                        fetchVehicleLikes1();
                    }
                } else {

                }
            });
        }


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