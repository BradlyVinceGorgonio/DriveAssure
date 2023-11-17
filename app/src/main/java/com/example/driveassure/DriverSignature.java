package com.example.driveassure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DriverSignature extends AppCompatActivity {
    signatureView driverSignatureView;
    ImageView clearButtonD;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_signature);

        driverSignatureView = findViewById(R.id.driverSignatureView);
        clearButtonD = findViewById(R.id.clearButtonD);
        saveButton = findViewById(R.id.saveButton);

        clearButtonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                driverSignatureView.clear();
            }
        });




        saveButton.setOnClickListener(v -> {
            // Get the signature bitmaps
            Bitmap driverSign = driverSignatureView.getSignatureBitmap();

            // Upload the bitmaps to Firebase or process them as needed
            // Example: Upload to Firebase Storage
            uploadToFirebaseStorage(driverSign, "Renter Signature.png");
        });
    }

    private void uploadToFirebaseStorage(Bitmap bitmap, String fileName) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String uid = currentUser.getUid();


        if (uid != null) {
            // Get a reference to the Firebase Storage root folder
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();

            // Create a reference to the folder based on documentId
            StorageReference documentFolderRef = storageRef.child("users-valid-id/"+uid);

            // Create a reference to the image file inside the folder
            StorageReference imageRef = documentFolderRef.child(fileName);

            // Convert the Bitmap to a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] imageData = baos.toByteArray();

            // Upload the image
            UploadTask uploadTask = imageRef.putBytes(imageData);

            uploadTask.addOnSuccessListener(taskSnapshot -> {

                insertMeToo();
                uploadPicturesToStorage(uid);


            }).addOnFailureListener(e -> {
                // Handle failure, e.g., show an error message
            });
        } else {
            // Handle the case where documentId is not available
        }
    }

    public void insertMeToo() {
        Intent intent = getIntent();
        String carOwnerId = intent.getStringExtra("CarOwnerId");
        Log.d("ANOLAMANMOUGOK", "insertMeToo: " + carOwnerId);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            // Get the UID of the current user
            String uid = currentUser.getUid();

            // Reference to Firestore
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            // Reference to the "users" collection
            CollectionReference usersCollectionRef = firestore.collection("users");

            // Reference to the source "vehicle approved" subcollection
            CollectionReference sourceCollectionRef = usersCollectionRef
                    .document(carOwnerId)
                    .collection("renters-approved");

            // Reference to the target "renter-processing" subcollection
            CollectionReference targetCollectionRef = usersCollectionRef
                    .document(carOwnerId)
                    .collection("renter-processing");

            // Query and copy documents
            sourceCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Get the data from the source document
                            Object data = document.getData();

                            // Set the document ID in the target collection to match the source document ID
                            targetCollectionRef.document(document.getId()).set(data);

                            // Delete the source document
                            sourceCollectionRef.document(document.getId()).delete();
                        }
                    } else {
                        // Handle errors
                    }
                }
            });
        } else {
            // Handle the case where the user is not signed in
        }
    }



    private void uploadPicturesToStorage(String uid) {
        // Create Firebase Storage reference
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create references for the back and front images in the "users/UID/" folder
        StorageReference backImageRef = storageRef.child("users-valid-id/" + uid + "/back.jpg");
        StorageReference frontImageRef = storageRef.child("users-valid-id/" + uid + "/front.jpg");



        // Create File objects for the "back.jpg" and "front.jpg" files
        File downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File licenseDirectory = new File(downloadsDirectory, "validIdRenter");
        File backJpgFile = new File(licenseDirectory, "back.jpg");
        File frontJpgFile = new File(licenseDirectory, "front.jpg");





        // Counter to track the number of successful uploads
        AtomicInteger successfulUploadCount = new AtomicInteger(0);


        // Upload back image to Firebase Storage
        Uri backImageUri = Uri.fromFile(backJpgFile);
        backImageRef.putFile(backImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Handle successful upload of the back image
                        // You can do something here, like updating the UI or database

                        // Increase the successful upload count
                        successfulUploadCount.incrementAndGet();

                        // Check if both images are successfully uploaded
                        if (successfulUploadCount.get() == 2) {
                            // Both images are uploaded, trigger the next steps
                            performNextSteps();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure of the upload
                    }
                });

        // Upload front image to Firebase Storage
        Uri frontImageUri = Uri.fromFile(frontJpgFile);
        frontImageRef.putFile(frontImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Handle successful upload of the front image
                        // You can do something here, like updating the UI or database

                        // Increase the successful upload count
                        successfulUploadCount.incrementAndGet();

                        // Check if both images are successfully uploaded
                        if (successfulUploadCount.get() == 2) {
                            // Both images are uploaded, trigger the next steps
                            performNextSteps();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure of the upload
                    }
                });
    }
    private void performNextSteps() {

        // Image uploaded successfully
        Dialog dialog = new Dialog(DriverSignature.this);
        dialog.setContentView(R.layout.signaturesuccess);
        dialog.show();
        // Find the 'YES' button in the dialog layout
        Button yesButton = dialog.findViewById(R.id.OKayButton);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Close the dialog if needed
                Intent newActivityIntent = new Intent(DriverSignature.this, userHome.class);
                startActivity(newActivityIntent);
            }
        });

    }


}
