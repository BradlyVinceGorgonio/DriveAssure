package com.example.driveassure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

                uploadPicturesToStorage(uid);

            }).addOnFailureListener(e -> {
                // Handle failure, e.g., show an error message
            });
        } else {
            // Handle the case where documentId is not available
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
