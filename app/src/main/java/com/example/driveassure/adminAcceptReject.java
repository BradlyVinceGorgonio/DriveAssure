package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class adminAcceptReject extends AppCompatActivity {

    ImageView LicenseNumberImage;
    ImageView LicenseExpiryImage;
    ImageView ProfileDisplayImage;
    TextView Name;
    TextView Email;
    TextView Number;
    TextView LicenseNum;
    TextView LicenseExp;

    Button accept;
    Button reject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_accept_reject);

        LicenseNumberImage = findViewById(R.id.licenseNumberImage);
        LicenseExpiryImage = findViewById(R.id.licenseExpiryImage);
        ProfileDisplayImage = findViewById(R.id.ProfileDisplayImage);

        Name = findViewById(R.id.displayUserName);
        Email = findViewById(R.id.displayEmail);
        Number = findViewById(R.id.displayContact);
        LicenseNum = findViewById(R.id.displayLicenseNum);
        LicenseExp = findViewById(R.id.displayLicenseExp);

        Intent intent = getIntent();
        String adminId = intent.getStringExtra("adminId");
        Log.d("taeka", adminId);

        fetchDataFromFirestore2(adminId);
    }

    private void fetchDataFromFirestore2(String documentId)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("admin")
                .document(documentId)  // Specify the document ID here
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Document exists, you can access its data here
                            String name = document.getString("name");
                            String licenseNumber = document.getString("license num");
                            String uid = document.getString("uid");
                            String email = document.getString("email");
                            String licenseExpiry = document.getString("license exp");
                            String contactnumber = document.getString("contact number");

                            Name.setText(name);
                            Email.setText(email);
                            Number.setText(contactnumber);
                            LicenseNum.setText(licenseNumber);
                            LicenseExp.setText(licenseExpiry);

                            // Fetch the profile picture URL from Firebase Storage
                            String imagePath = "users/" + uid + "/" + "face.jpg";
                            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(imagePath);

                            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();

                                // Load the image into an ImageView using Glide
                                ImageView imageView = findViewById(R.id.ProfileDisplayImage); // Replace with your ImageView ID
                                Glide.with(this).load(imageUrl).into(imageView);

                            }).addOnFailureListener(exception -> {
                                // Handle the failure scenario
                            });

                            // Fetch the validID URL from Firebase Storage
                            String imagePathID = "users/" + uid + "/back.jpg";
                            StorageReference storageRefID = FirebaseStorage.getInstance().getReference().child(imagePathID);

                            storageRefID.getDownloadUrl().addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();

                                // Load the image into an ImageView using Glide
                                ImageView imageView = findViewById(R.id.licenseNumberImage); // Replace with your ImageView ID
                                Glide.with(this).load(imageUrl).into(imageView);

                            }).addOnFailureListener(exception -> {
                                // Handle the failure scenario
                            });
                            // Fetch the Document URL from Firebase Storage
                            String imagePathDoc = "users/" + uid + "/front.jpg";
                            StorageReference storageRefDoc = FirebaseStorage.getInstance().getReference().child(imagePathDoc);

                            storageRefDoc.getDownloadUrl().addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();

                                // Load the image into an ImageView using Glide
                                ImageView imageView = findViewById(R.id.licenseExpiryImage); // Replace with your ImageView ID
                                Glide.with(this).load(imageUrl).into(imageView);

                            }).addOnFailureListener(exception -> {
                                // Handle the failure scenario
                            });

                            accept = findViewById(R.id.userAccept);
                            reject = findViewById(R.id.userReject);

                            accept.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View view)
                                {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    DocumentReference costumerRef = db.collection("admin").document(documentId);

                                    costumerRef.get()
                                            .addOnSuccessListener(documentSnapshot -> {
                                                if (documentSnapshot.exists()) {

                                                    String name = documentSnapshot.getString("name");
                                                    String licenseNumber = documentSnapshot.getString("license num");
                                                    String uid = documentSnapshot.getString("uid");
                                                    String email = documentSnapshot.getString("email");
                                                    String licenseExpiry = documentSnapshot.getString("license exp");
                                                    String contactnumber = documentSnapshot.getString("contact number");

                                                    // Use the retrieved information as needed

                                                    DocumentReference usersRef = db.collection("users").document(documentId);

                                                    Map<String, Object> data = new HashMap<>();
                                                    data.put("uid", uid);
                                                    data.put("license num", name);
                                                    data.put("age", licenseNumber);
                                                    data.put("email", email);
                                                    data.put("license exp", licenseExpiry);
                                                    data.put("contact number", contactnumber);

                                                    usersRef.set(data)
                                                            .addOnSuccessListener(aVoid -> {

                                                                Log.d("deleters", "Success data add");
                                                                Intent intent = new Intent(adminAcceptReject.this, AdminHome.class);
                                                                startActivity(intent);
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                // Error sending document
                                                            });

                                                    DocumentReference requestRef = db.collection("admin")
                                                            .document(documentId);

                                                    requestRef.delete()
                                                            .addOnSuccessListener(aVoid -> {
                                                                // Document successfully deleted
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                // Error deleting document
                                                            });

                                                } else {
                                                    // Document doesn't exist
                                                }
                                            })
                                            .addOnFailureListener(e -> {
                                                // Error retrieving document
                                            });
                                }
                            });

                            reject.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view)
                                {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    DocumentReference requestRef = db.collection("admin")
                                            .document(documentId);

                                    requestRef.delete()
                                            .addOnSuccessListener(aVoid -> {
                                                Log.d("deleters", "Success doc deleted");
                                                Intent intent = new Intent(adminAcceptReject.this, AdminHome.class);
                                                startActivity(intent);
                                            })
                                            .addOnFailureListener(e -> {
                                                // Error deleting document
                                            });
                                }
                            });
                        } else {
                            // Handle the case where the document does not exist
                        }
                    } else {
                        // Handle the failure scenario
                    }
                });
    }
}