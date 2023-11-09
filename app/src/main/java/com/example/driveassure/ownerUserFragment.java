package com.example.driveassure;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class ownerUserFragment extends Fragment implements OwnerListingCarAdapter.OnItemClickListener {

    private List<OwnerListingsClass> HistoryList;
    private OwnerListingCarAdapter ownerListingCarAdapter;

    ProgressBar progressBarID;

    Button addCarBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_user, container, false);

        progressBarID = view.findViewById(R.id.progressBarID);

        addCarBtn = view.findViewById(R.id.addCarBtn);
        addCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), Select_Vehicle_Choice.class);
                startActivity(intent);

            }
        });

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        RecyclerView recyclerView = view.findViewById(R.id.addcarListingsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        HistoryList = new ArrayList<>();
        ownerListingCarAdapter = new OwnerListingCarAdapter(getContext(), HistoryList, this);
        recyclerView.setAdapter(ownerListingCarAdapter);

        fetchDataFromFirestore();
        return view;
    }
    @Override
    public void onItemClick(OwnerListingsClass history) {


        String historyUid = history.getUid(); // Assuming TrainerClass has a getUid() method
        String CarpostUID = history.getCarpostUID();

        Intent intent = new Intent(getContext(), DetailedCarOwnerListingsActivity.class);

        // Pass data as extras in the intent
        intent.putExtra("historyUid", historyUid);
        intent.putExtra("CarpostUID", CarpostUID);

        startActivity(intent); 


    }

    private void fetchDataFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String currentUserId = firebaseAuth.getCurrentUser().getUid();
        Log.d("BITCHNIGGA", "BITCH ETO YUNG UID NG GUMAGAMIT NA TAO " + currentUserId);

        // Reference to the "history" subcollection of the current user in the "enforcer" collection
        CollectionReference historyRef = db.collection("users")
                .document(currentUserId)
                .collection("vehicle listings");
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