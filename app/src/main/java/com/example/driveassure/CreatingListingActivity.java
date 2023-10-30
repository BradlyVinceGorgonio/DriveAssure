package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

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

    RelativeLayout PickImageButton;

    String[] fuelTypes = {"Gasoline", "Diesel", "Electric", "Hybrid", "Other"};

    String[] conditions = {"Brand new", "Good as new", "Good", "Used"};
    String[] transmissions = {"Automatic", "Manual"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_listing);

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
                String selectedMotorcycleBrand = motorcycleBrands[position];
                Toast.makeText(CreatingListingActivity.this, "Selected Motorcycle Brand: " + selectedMotorcycleBrand, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        carBrandsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedCarBrand = carBrands[position];
                Toast.makeText(CreatingListingActivity.this, "Selected Car Brand: " + selectedCarBrand, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        transmissionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedTransmission = transmissions[position];
                Toast.makeText(CreatingListingActivity.this, "Selected Transmission: " + selectedTransmission, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        fuelTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedFuelType = fuelTypes[position];
                Toast.makeText(CreatingListingActivity.this, "Selected Fuel Type: " + selectedFuelType, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        conditionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedCondition = conditions[position];
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

}