package com.example.driveassure;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CarRenterOnProcess extends Fragment {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

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
    TextView returningRenterName;
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
        View view =  inflater.inflate(R.layout.fragment_car_renter_on_process, container, false);


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


        getDatas();



        return view;

    }
    public void getDatas()
    {
        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

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

                                                        vehicleTitle.setText(VehicleTitle);
                                                        vehiclePrices.setText(vehiclePrice);
                                                        vehicleTransmission.setText(transmission);
                                                        fuelType.setText(fuel);
                                                        vehicBrand.setText(brand);




                                                        int daysInt = Integer.parseInt(totalDays);
                                                        int priceInt = Integer.parseInt(vehiclePrice);
                                                        int totalPriceInt = daysInt * priceInt;
                                                        String displayme = String.valueOf(totalPriceInt);
                                                        //totalPayment.setText("₱ " + displayme);


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
                                                        returningRenterName.setText(renterName);

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

    }



