package com.example.driveassure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class CreatingListingActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 101;
    private ViewPager imageSlider;
    private ImagePagerAdapter imagePagerAdapter;
    private ArrayList<Uri> selectedImageUris;

    String[] motorcycleBrands = {
            "Aprilia", "Benelli", "Bimota", "BMW", "Buell", "Cagiva", "Can-Am",
            "CCW (Cleveland CycleWerks)", "Ducati", "Harley-Davidson", "Honda", "Husqvarna",
            "Indian", "Kawasaki", "KTM", "Moto Guzzi", "MV Agusta", "Norton", "Royal Enfield",
            "Suzuki", "Triumph", "Ural", "Vespa (Primarily a scooter brand, but they also make some motorcycles)",
            "Victory", "Yamaha", "Zero"
    };

    String[] carBrands = {
            "Audi", "Acura", "Alfa Romeo", "Aston Martin", "BMW", "Bentley", "Bugatti", "Buick",
            "Chevrolet", "Cadillac", "Chrysler", "Citroën", "Dodge", "Dacia", "Daewoo", "Daihatsu",
            "Ford", "Ferrari", "Fiat", "Fisker", "GMC", "Genesis", "Honda", "Hyundai", "Hummer",
            "Infiniti", "Isuzu", "Jeep", "Jaguar", "Kia", "Lamborghini", "Land Rover", "Lexus",
            "Mercedes-Benz", "Mazda", "Mitsubishi", "Maserati", "Nissan", "Opel", "Porsche", "Peugeot",
            "Quattroporte (Maserati)", "Rolls-Royce", "Renault", "Subaru", "Skoda", "Suzuki", "Scion",
            "Toyota", "Tesla", "UAZ", "Volkswagen", "Volvo", "Wuling", "Xpeng", "Yugo", "Zenos"
    };

    String[] fuelTypes = {"Gasoline", "Diesel", "Electric", "Hybrid", "Other"};

    String[] conditions = {"Brand new", "Good as new", "Good", "Used"};
    String[] transmissions = {"Automatic", "Manual"};

    String selectedMotorcycleBrand;
    String selectedCarBrand;
    String selectedTransmission;
    String selectedFuelType;
    String selectedCondition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_listing);

        // Add this to your onCreate method or the Application class
        FirebaseApp.initializeApp(this);


        // Retrieve the string from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra("EXTRA_MESSAGE");
        // Vehicle Type
        Log.d("POWERPOWER", "Message nasa listing act na: " + message);


        Spinner motorcycleBrandsSpinner = findViewById(R.id.motorcycleBrandsSpinner);
        Spinner carBrandsSpinner = findViewById(R.id.carBrandsSpinner);
        Spinner transmissionSpinner = findViewById(R.id.transmissionSpinner);
        Spinner fuelTypeSpinner = findViewById(R.id.fuelTypeSpinner);
        Spinner conditionSpinner = findViewById(R.id.conditionSpinner);

        ArrayAdapter<String> motorcycleSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, motorcycleBrands);
        motorcycleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        motorcycleBrandsSpinner.setAdapter(motorcycleSpinnerAdapter);

        ArrayAdapter<String> carSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, carBrands);
        carSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carBrandsSpinner.setAdapter(carSpinnerAdapter);

        ArrayAdapter<String> transmissionSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, transmissions);
        transmissionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transmissionSpinner.setAdapter(transmissionSpinnerAdapter);

        ArrayAdapter<String> fuelTypeSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fuelTypes);
        fuelTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fuelTypeSpinner.setAdapter(fuelTypeSpinnerAdapter);

        ArrayAdapter<String> conditionSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, conditions);
        conditionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionSpinner.setAdapter(conditionSpinnerAdapter);


        motorcycleBrandsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedMotorcycleBrand = motorcycleBrands[position];
                Toast.makeText(CreatingListingActivity.this, "Selected Motorcycle Brand: " + selectedMotorcycleBrand, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        carBrandsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedCarBrand = carBrands[position];
                Toast.makeText(CreatingListingActivity.this, "Selected Car Brand: " + selectedCarBrand, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        transmissionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedTransmission = transmissions[position];
                Toast.makeText(CreatingListingActivity.this, "Selected Transmission: " + selectedTransmission, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        fuelTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedFuelType = fuelTypes[position];
                Toast.makeText(CreatingListingActivity.this, "Selected Fuel Type: " + selectedFuelType, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        conditionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedCondition = conditions[position];
                Toast.makeText(CreatingListingActivity.this, "Selected Condition: " + selectedCondition, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button selectImagesButton = findViewById(R.id.selectImagesButton);
        imageSlider = findViewById(R.id.imageSlider);
        selectedImageUris = new ArrayList<>();
        imagePagerAdapter = new ImagePagerAdapter(this, selectedImageUris);
        imageSlider.setAdapter(imagePagerAdapter);

        selectImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        imageSlider = findViewById(R.id.imageSlider);

        imageSlider.setAdapter(imagePagerAdapter);

// Attach the TabLayout to the ViewPager
        tabLayout.setupWithViewPager(imageSlider, true);


    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.getClipData() != null) {
                    int itemCount = data.getClipData().getItemCount();
                    for (int i = 0; i < itemCount; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        saveImageToDownloads(imageUri);
                        selectedImageUris.add(imageUri);
                    }
                } else if (data.getData() != null) {
                    Uri imageUri = data.getData();
                    saveImageToDownloads(imageUri);
                    selectedImageUris.add(imageUri);
                }

                imagePagerAdapter.notifyDataSetChanged();
            }
        }
    }
    private void saveImageToDownloads(Uri imageUri) {
        try {
            String fileName = "carPics_" + System.currentTimeMillis() + ".jpg";
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File carPicsDir = new File(downloadsDir, "carPics");

            if (!carPicsDir.exists()) {
                carPicsDir.mkdirs();
            }

            File outputFile = new File(carPicsDir, fileName);

            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            OutputStream outputStream = new FileOutputStream(outputFile);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadImagesAndFieldsToFirebase()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        // Retrieve the string from the intent
        Intent intent = getIntent();
        String carType= intent.getStringExtra("EXTRA_MESSAGE");
        // Car Type ^^

        Map<String, Object> carData = new HashMap<>();
        carData.put("vehicle type", carType);
        carData.put("selectedMotorcycleBrand", selectedMotorcycleBrand);
        carData.put("selectedCarBrand", selectedCarBrand);
        carData.put("selectedTransmission", selectedTransmission);
        carData.put("selectedFuelType", selectedFuelType);
        carData.put("selectedCondition", selectedCondition);


        DocumentReference carPostDocument = db.collection("car-posts").document(); // Automatically generates a unique ID
        String carPostId = carPostDocument.getId(); // Get the ID

        carPostDocument.set(carData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Car-post document added successfully
                        // You have the carPostId as the document's ID
                        // Now create a subcollection with the same ID
                        createSubcollectionWithSameId(db, carPostId, carData);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors
                    }
                });
    }
    private void createSubcollectionWithSameId(FirebaseFirestore db, String carPostId, Map<String, Object> carData) {

        // Initialize Firebase Authentication
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Check if the user is signed in (authenticated)
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        db.collection("users")
                .document(userId)
                .collection("vehicle listings")
                .document(carPostId) // Set the same document ID as the car-post document
                .set(carData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Subcollection document added successfully with the same ID
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors
                    }
                });
    }

}