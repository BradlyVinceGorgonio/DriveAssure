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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
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

                insertmeReal();



            }).addOnFailureListener(e -> {
                // Handle failure, e.g., show an error message
            });
        } else {
            // Handle the case where documentId is not available
        }
    }



    private void insertmeReal()
    {
        Intent intent = getIntent();
        String carOwnerId = intent.getStringExtra("historyUid");
        String carpostUID = intent.getStringExtra("CarpostUID");

        Log.d("ANOLAMANMOUGOK", "car owner: " + carOwnerId);
        Log.d("ANOLAMANMOUGOK", "carpost: " + carpostUID);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Assuming your collection path is "users"
        CollectionReference usersCollection = db.collection("users");


        // Assuming carOwnerId and carpostUID are variables containing the values you want to match

        // Assuming "renters-approved" is the subcollection
        CollectionReference rentersApprovedCollection = usersCollection.document(carOwnerId).collection("renters-approved");

        // Query the subcollection with the conditions on "Car Owner uid" and "Car to Request"
        Query query = rentersApprovedCollection
                .whereEqualTo("Car Owner uid", carOwnerId)
                .whereEqualTo("Car To Request", carpostUID);

        // Execute the query
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Intent intent = getIntent();
                        String approvedId = document.getString("Approved Id");
                        String carOwnerId = document.getString("Car Owner uid");
                        String PaymentMethod = intent.getStringExtra("PaymentMethod");

                        TransferToProcessingAndDeleteApprovedRenters(approvedId, carOwnerId);


                    }
                } else {
                    // Handle errors
                    Log.e("BAKITDIUMAANDAR", "Error getting documents: ", task.getException());
                }
            }
        });

    }

    private void addPaymentMethod(String approvedId, String PaymentMethod, String carOwnerId)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();



        // Assuming yourBooleanFieldName is the name of the boolean field you want to add
        String yourBooleanFieldName = "isGcash";

        // Assuming yourBooleanFieldValue is the boolean value you want to set


        // Reference to the "renters-processing" subcollection document
        DocumentReference rentersProcessingDocument = db.collection("users")
                .document(carOwnerId)
                .collection("renter-processing")
                .document(approvedId);

        // Create a Map to store the field and its value
        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("isGcash", PaymentMethod);

        // Set the new fields in the document
        rentersProcessingDocument.set(fieldMap, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Document successfully updated with the new field
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure to update the document
                    }
                });


    }

    private void TransferToProcessingAndDeleteApprovedRenters(String approvedId, String carOwnerId)
    {

        FirebaseFirestore db = FirebaseFirestore.getInstance();


// Reference to the "renters-approved" subcollection document
        DocumentReference rentersApprovedDocument = db.collection("users")
                .document(carOwnerId)
                .collection("renters-approved")
                .document(approvedId);

// Get the data from the "renters-approved" document
        rentersApprovedDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Get the data from the document
                        Map<String, Object> data = document.getData();

                        // Reference to the "renter-processing" subcollection document
                        CollectionReference renterProcessingCollection = db.collection("users")
                                .document(carOwnerId)
                                .collection("renter-processing");

                        // Add the data to the "renter-processing" subcollection
                        renterProcessingCollection.document(approvedId).set(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Document successfully moved to "renter-processing"
                                        // Now, delete the document from "renters-approved"
                                        rentersApprovedDocument.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                                                String uid = currentUser.getUid();
                                                uploadPicturesToStorage(uid);

                                                Intent intent = getIntent();
                                                String PaymentMethod = intent.getStringExtra("PaymentMethod");


                                                addPaymentMethod(approvedId,PaymentMethod, carOwnerId);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle failure to delete document from "renters-approved"
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle failure to add document to "renter-processing"
                                    }
                                });
                    } else {
                        // Document does not exist in "renters-approved"
                    }
                } else {
                    // Handle errors in getting the document from "renters-approved"
                    Log.e("Firestore", "Error getting document: ", task.getException());
                }
            }
        });
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
