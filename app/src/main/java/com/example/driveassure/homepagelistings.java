package com.example.driveassure;

import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_STATIC_DP;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class homepagelistings extends AppCompatActivity {


    private ViewPager viewPager;
    private ImageSliderAdapter imageSliderAdapter;
    private StorageReference storageRef;


    TextView title;
    TextView carLocation;
    TextView price;
    TextView yearModel;
    TextView vehicleSeat;
    TextView vehicleKilometer;
    TextView color;
    TextView fuelTypes;
    TextView vehicleCondition;
    TextView brand;
    TextView Model;
    TextView vehicleTransmission;
    TextView description;

    CircleImageView carOwnerProfile;
    TextView carOwnerLocation;
    TextView ownerContactNumber;
    TextView carOwnerName;
    TextView carEmailAddress;
    Button messageCarOwner;

    Button inquireButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepagelistings);

        // Retrieve data from the Intent
        String historyUid = getIntent().getStringExtra("historyUid");
        String CarpostUID = getIntent().getStringExtra("CarpostUID");
        Log.d("NOBELATO", "Carpost UID " + CarpostUID);

        // Initialize Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("carposts/" + CarpostUID + "/");

        // Initialize ViewPager
        viewPager = findViewById(R.id.imageSlider);
        imageSliderAdapter = new ImageSliderAdapter(this);
        viewPager.setAdapter(imageSliderAdapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager, true);

        // Load images from Firebase Storage
        loadImagesFromFirebase();
        // Fetch texts
        fetchDataFromFirestore(CarpostUID, historyUid);

        inquireButton = findViewById(R.id.inquireButton);
        PushDownAnim.setPushDownAnimTo(inquireButton).setScale(MODE_STATIC_DP, 8 );
        inquireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        messageCarOwner = findViewById(R.id.messageCarOwner);
        PushDownAnim.setPushDownAnimTo(messageCarOwner).setScale(MODE_STATIC_DP, 8 );
        messageCarOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void fetchDataFromFirestore(String CarID, String UID)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("car-posts")
                .document(CarID)  // Specify the document ID here
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Document exists, you can access its data here
                            String VehicleAddress = document.getString("Vehicle Address");
                            String VehicleBrand = document.getString("Vehicle Brand");
                            String VehicleColor = document.getString("Vehicle Color");
                            String VehicleCondition = document.getString("Vehicle Condition");
                            String VehicleDescription = document.getString("Vehicle Description");
                            String VehicleFuelType = document.getString("Vehicle Fuel Type");
                            String VehicleKilometer = document.getString("Vehicle Kilometer");
                            String VehicleModel = document.getString("Vehicle Model");
                            String VehiclePrice = document.getString("Vehicle Price");
                            String VehicleSeatCount = document.getString("Vehicle Seat Count");
                            String VehicleTitle = document.getString("Vehicle Title");
                            String VehicleTransmission = document.getString("Vehicle Transmission");
                            String VehicleYearModel = document.getString("Vehicle Year Model");
                            title = findViewById(R.id.title);
                            carLocation = findViewById(R.id.carLocation);
                            price = findViewById(R.id.price);
                            yearModel = findViewById(R.id.yearModel);
                            vehicleSeat = findViewById(R.id.vehicleSeat);
                            vehicleKilometer = findViewById(R.id.vehicleKilometer);
                            color = findViewById(R.id.color);
                            fuelTypes = findViewById(R.id.fuelTypes);
                            vehicleCondition = findViewById(R.id.vehicleCondition);
                            brand = findViewById(R.id.brand);
                            Model = findViewById(R.id.Model);
                            vehicleTransmission = findViewById(R.id.vehicleTransmission);
                            description = findViewById(R.id.description);


                            title.setText(VehicleTitle);
                            carLocation.setText(VehicleAddress);
                            price.setText(VehiclePrice);
                            yearModel.setText(VehicleYearModel);
                            vehicleSeat.setText(VehicleSeatCount);
                            vehicleKilometer.setText(VehicleKilometer);
                            color.setText(VehicleColor);
                            fuelTypes.setText(VehicleFuelType);
                            vehicleCondition.setText(VehicleCondition);
                            brand.setText(VehicleBrand);
                            Model.setText(VehicleModel);
                            vehicleTransmission.setText(VehicleTransmission);
                            description.setText(VehicleDescription);

                            fetchDataFromFirestore1(UID);

                        } else {
                            // Handle the case where the document does not exist
                        }
                    } else {
                        // Handle the failure scenario
                    }
                });
    }
    private void fetchDataFromFirestore1(String UID)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(UID)  // Specify the document ID here
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Document exists, you can access its data here
                            String OwnerName = document.getString("name");
                            String OwnerNumber = document.getString("contact number");
                            String OwnerEmail = document.getString("email");


                            carOwnerName = findViewById(R.id.carOwnerName);
                            ownerContactNumber = findViewById(R.id.ownerContactNumber);
                            carEmailAddress = findViewById(R.id.carEmailAddress);

                            carOwnerName.setText(OwnerName);
                            ownerContactNumber.setText(OwnerNumber);
                            carEmailAddress.setText(OwnerEmail);

                            //Last to
                            carOwnerProfile = findViewById(R.id.carOwnerProfile);

                            // Fetch the profile picture URL from Firebase Storage
                            String imagePath = "users/" + UID + "/" + "face.jpg";
                            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(imagePath);

                            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();

                                // Load the image into an ImageView using Glide
                                carOwnerProfile = findViewById(R.id.carOwnerProfile); // Replace with your ImageView ID
                                Glide.with(this).load(imageUrl).into(carOwnerProfile);

                            }).addOnFailureListener(exception -> {
                                // Handle the failure scenario
                            });


                        } else {
                            // Handle the case where the document does not exist
                        }
                    } else {
                        // Handle the failure scenario
                    }
                });
    }

    private void loadImagesFromFirebase() {
        storageRef.listAll()
                .addOnSuccessListener(listResult -> {
                    for (StorageReference item : listResult.getItems()) {
                        // Get download URL for each image
                        item.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Add the download URL to the adapter
                            imageSliderAdapter.addImageUrl(uri.toString());
                            // Notify the adapter that the data has changed
                            imageSliderAdapter.notifyDataSetChanged();
                        }).addOnFailureListener(exception -> {
                            // Handle any errors
                        });
                    }
                })
                .addOnFailureListener(exception -> {
                    // Handle any errors
                });
    }


}