package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

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
        Intent intent = getIntent();
        String documentId = intent.getStringExtra("uid");

        if (documentId != null) {
            // Get a reference to the Firebase Storage root folder
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();

            // Create a reference to the folder based on documentId
            StorageReference documentFolderRef = storageRef.child("users-valid-id/"+documentId);

            // Create a reference to the image file inside the folder
            StorageReference imageRef = documentFolderRef.child(fileName);

            // Convert the Bitmap to a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] imageData = baos.toByteArray();

            // Upload the image
            UploadTask uploadTask = imageRef.putBytes(imageData);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
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
            }).addOnFailureListener(e -> {
                // Handle failure, e.g., show an error message
            });
        } else {
            // Handle the case where documentId is not available
        }
    }


}
