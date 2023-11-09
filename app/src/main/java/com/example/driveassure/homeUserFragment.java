package com.example.driveassure;

// homeUserFragment
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class homeUserFragment extends Fragment implements HomeListingCarAdapter.OnItemClickListener {
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

    private List<HomeListingClass> HistoryList;
    private HomeListingCarAdapter homeListingCarAdapter;

    ProgressBar progressBarID2;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_user, container, false);


        progressBarID2 = view.findViewById(R.id.progressBarID2);

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
        RecyclerView recyclerView = view.findViewById(R.id.homeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        HistoryList = new ArrayList<>();
        homeListingCarAdapter = new HomeListingCarAdapter(getContext(), HistoryList, this);
        recyclerView.setAdapter(homeListingCarAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        fetchDataFromFirestore();


        return view;
    }
    @Override
    public void onItemClick(HomeListingClass history) {
        String historyUid = history.getUid(); // Assuming HomeListingClass has a getUid() method
        String carpostUID = history.getCarpostUID();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null && currentUser.getUid().equals(historyUid)) {
            // The user's UID matches the historyUid, open intent2
            Intent intent2 = new Intent(getContext(), DetailedCarOwnerListingsActivity.class);
            // Pass data as extras in the intent
            intent2.putExtra("historyUid", historyUid);
            intent2.putExtra("CarpostUID", carpostUID);
            startActivity(intent2);
        } else {
            // The user's UID does not match the historyUid, open intent1
            Intent intent = new Intent(getContext(), homepagelistings.class);
            // Pass data as extras in the intent
            intent.putExtra("historyUid", historyUid);
            intent.putExtra("CarpostUID", carpostUID);
            startActivity(intent);
        }
    }

    private void fetchDataFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String currentUserId = firebaseAuth.getCurrentUser().getUid();
        Log.d("BITCHNIGGA", "BITCH ETO YUNG UID NG GUMAGAMIT NA TAO " + currentUserId);

        // Reference to the "history" subcollection of the current user in the "enforcer" collection
        CollectionReference historyRef = db.collection("car-posts");
        historyRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        HistoryList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Process data from the "history" subcollection
                            String name = document.getString("Vehicle Title");
                            // Retrieve the "description" field as a List of Strings
                            String carPostUID = document.getString("Vehicle post-id");
                            String uid = document.getString("uid");
                            String Address = document.getString("Vehicle Address");
                            String Price = document.getString("Vehicle Price");
                            String Transmission = document.getString("Vehicle Transmission");


                            // Fetch the profile picture URL from Firebase Storage
                            Log.d("BITCHNIGGA", "BITCHNIGGA: " + name + carPostUID + "uid: "+ uid + Address + Price + Transmission );
                            fetchProfilePictureUrl(name, Price, Address, uid, "/carpic1.jpg", Transmission, carPostUID);
                        }
                    } else {
                        // Handle the failure scenario
                    }
                });
    }
    private void fetchProfilePictureUrl(String name, String Price, String Address, String uid, String profilePictureUrl, String Transmission, String carPostUID) {
        StorageReference storageRef= FirebaseStorage.getInstance().getReference().child( "carposts/"+ carPostUID+ "/" +"carpic1.jpg");

        final String[] tempProfilePictureUrl = {profilePictureUrl};  // Declare a final temporary variable

        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            tempProfilePictureUrl[0] = uri.toString();  // Assign the value to the temporary variable

            Log.d("BITCHNIGGA", "LOOB NAKO NIGGER: " +  tempProfilePictureUrl[0]);
            // Create a TrainerClass instance with the retrieved data
            HomeListingClass history = new HomeListingClass(tempProfilePictureUrl[0], name, uid, "₱ "+Price + " /day", Address, Transmission, carPostUID);

            HistoryList.add(history);

            // Notify the adapter that data has changed
            // Notify the adapter that data has changed

            homeListingCarAdapter.notifyDataSetChanged();


            progressBarID2.setVisibility(View.GONE);

        }).addOnFailureListener(e -> {
            // Handle any errors that occur while fetching the profile picture URL
        });
    }

}
