package com.example.driveassure;

// homeUserFragment
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class homeUserFragment extends Fragment {
    ImageButton chatMsg;
    private Button filterButton;
   String selectedCarType;
    String selectedLocation;
    String selectedPriceRange;
    String selectedVehicleType;
    String[] priceRanges = {"500-1000", "1000-1500", "1500-2000", "2000-2500", "2500-3000"};
    String[] locationRanges = {"Caloocan", "Malabon", "Navotas", "Valenzuela", "Quezon City", "Marikina", "Pasig", "Taguig", "Makati", "Manila", "Mandaluyong", "San Juan", "Pasay", "Parañaque", "Las Piñas", "Muntinlupa", "Pateros"};

    String[] motorcycleTypes = {"Aprilia", "Benelli", "Bimota", "BMW", "Buell", "Cagiva", "Can-Am",
            "CCW (Cleveland CycleWerks)", "Ducati", "Harley-Davidson", "Honda", "Husqvarna",
            "Indian", "Kawasaki", "KTM", "Moto Guzzi", "MV Agusta", "Norton", "Royal Enfield",
            "Suzuki", "Triumph", "Ural", "Vespa (Primarily a scooter brand, but they also make some motorcycles)",
            "Victory", "Yamaha", "Zero"};
    String[] carTypes = {"Audi", "Acura", "Alfa Romeo", "Aston Martin", "BMW", "Bentley", "Bugatti", "Buick",
            "Chevrolet", "Cadillac", "Chrysler", "Citroën", "Dodge", "Dacia", "Daewoo", "Daihatsu",
            "Ford", "Ferrari", "Fiat", "Fisker", "GMC", "Genesis", "Honda", "Hyundai", "Hummer",
            "Infiniti", "Isuzu", "Jeep", "Jaguar", "Kia", "Lamborghini", "Land Rover", "Lexus",
            "Mercedes-Benz", "Mazda", "Mitsubishi", "Maserati", "Nissan", "Opel", "Porsche", "Peugeot",
            "Quattroporte (Maserati)", "Rolls-Royce", "Renault", "Subaru", "Skoda", "Suzuki", "Scion",
            "Toyota", "Tesla", "UAZ", "Volkswagen", "Volvo", "Wuling", "Xpeng", "Yugo", "Zenos"
    }; // Add more car types

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_user, container, false);



        TextInputLayout locationTextInputLayout = view.findViewById(R.id.textInputLayoutLocation);
        AutoCompleteTextView locationAutoComplete = view.findViewById(R.id.locationSpinner);

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, locationRanges);
        locationAutoComplete.setAdapter(locationAdapter);

        locationAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedLocation = (String) parent.getItemAtPosition(position);
                // Handle the selected location as needed
            }
        });


        TextInputLayout priceRangeTextInputLayout = view.findViewById(R.id.textInputLayoutPriceRange);
        AutoCompleteTextView priceRangeAutoComplete = view.findViewById(R.id.priceRangeSpinner);

        ArrayAdapter<String> priceRangeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, priceRanges);
        priceRangeAutoComplete.setAdapter(priceRangeAdapter);

        priceRangeAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPriceRange = (String) parent.getItemAtPosition(position);
                // Handle the selected price range as needed
            }
        });

        TextInputLayout vehicleTypeTextInputLayout = view.findViewById(R.id.textInputLayoutVehicleType);
        AutoCompleteTextView vehicleTypeAutoComplete = view.findViewById(R.id.vehicleTypeSpinner);

        ArrayAdapter<String> vehicleTypeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, motorcycleTypes);
        vehicleTypeAutoComplete.setAdapter(vehicleTypeAdapter);

        vehicleTypeAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedVehicleType = (String) parent.getItemAtPosition(position);
                // Handle the selected vehicle type as needed
            }
        });

        TextInputLayout carTypeTextInputLayout = view.findViewById(R.id.textInputLayoutCarType);
        AutoCompleteTextView carTypeAutoComplete = view.findViewById(R.id.carTypeSpinner);

        ArrayAdapter<String> carTypeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, carTypes);
        carTypeAutoComplete.setAdapter(carTypeAdapter);

        carTypeAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCarType = (String) parent.getItemAtPosition(position);
                // Handle the selected car type as needed
            }
        });



        chatMsg = view.findViewById(R.id.chatMsg);
        chatMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), ChatListActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
