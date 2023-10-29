package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AdminHome extends AppCompatActivity implements AdminUserAdapter.OnItemClickListener {

    private List<AdminClass> adminList;
    private AdminUserAdapter adminAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adminList = new ArrayList<>();
        adminAdapter = new AdminUserAdapter(this, adminList, this);
        recyclerView.setAdapter(adminAdapter);

        // Fetch data from Firestore and populate adminList
        // ...
        fetchDataFromFirestore();
    }
    @Override
    public void onItemClick(AdminClass admin) {
        String adminId = admin.getUid(); // Assuming AdminClass has a getUid() method to retrieve the admin ID
        Intent intent = new Intent(AdminHome.this, adminAcceptReject.class);
        intent.putExtra("adminId", adminId); // Pass the admin ID to the AcceptRejectAdminActivity
        startActivity(intent);
    }

    private void fetchDataFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("admin")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        adminList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = document.getString("name");
                            String licenseNumber = document.getString("license num");
                            String uid = document.getString("uid");

                            String licenseExpiry = document.getString("license exp");
                            String contactnumber = document.getString("contact number");

                            // Fetch the profile picture URL from Firebase Storage
                            fetchProfilePictureUrl(name, contactnumber, licenseExpiry, licenseNumber, uid, "/" + "face.jpg");
                        }
                    } else {
                        // Handle the failure scenario
                    }
                });
    }
    private void fetchProfilePictureUrl(String name, String contactnumber, String licenseExpiry, String licenseNumber, String uid, String profilePictureUrl) {
        Log.d("taeka", uid + " weird " + profilePictureUrl);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("users/" + uid + profilePictureUrl);

        final String[] tempProfilePictureUrl = {profilePictureUrl};  // Declare a final temporary variable

        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            tempProfilePictureUrl[0] = uri.toString();  // Assign the value to the temporary variable

            // Create a TrainerClass instance with the retrieved data
            AdminClass admin = new AdminClass(tempProfilePictureUrl[0], name, uid, licenseNumber, licenseExpiry, contactnumber);
            adminList.add(admin);

            // Notify the adapter that data has changed
            adminAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            // Handle any errors that occur while fetching the profile picture URL
        });
    }
}