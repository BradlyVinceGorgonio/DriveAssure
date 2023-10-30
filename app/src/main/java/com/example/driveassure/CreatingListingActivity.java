package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreatingListingActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_listing);

        Spinner motorcycleBrandsSpinner = findViewById(R.id.motorcycleBrandsSpinner);
        Spinner carBrandsSpinner = findViewById(R.id.carBrandsSpinner);

        ArrayAdapter<String> motorcycleSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, motorcycleBrands);
        motorcycleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        motorcycleBrandsSpinner.setAdapter(motorcycleSpinnerAdapter);

        ArrayAdapter<String> carSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, carBrands);
        carSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carBrandsSpinner.setAdapter(carSpinnerAdapter);

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
    }
}