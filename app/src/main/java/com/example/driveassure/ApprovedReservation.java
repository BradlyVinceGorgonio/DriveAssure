package com.example.driveassure;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ApprovedReservation extends AppCompatActivity {

    String renterUID;
    String carUID;
    String requestID;
    String ApprovedId;
    String isGcash;
    String renterUid;
    ImageView ValidID;
    ImageView uploadedPayment;
    ProgressBar ProgressBarFaceId;
    TextView onTheDayPayment;
    Button approvedButton;
    TextView textView25;
    String VehicleTitle;
    String renterName;
    String VehiclePrice;
    String DateStart;
    String DateEnd;
    String totalDays;
    TextView renterNamed;
    TextView CarName;
    TextView dateStart;
    TextView dateEnd;
    TextView paymentmethod;
    TextView totalDay;
    TextView totalPrice;

    private static final String CHANNEL_ID = "DownloadChannel";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_reservation);

        Intent intent = getIntent();
        renterUID = intent.getStringExtra("renterUID");
        carUID = intent.getStringExtra("carUID");
        requestID = intent.getStringExtra("requestID");
        Log.d("ANOAPPROVEDID", "onCreate: " + requestID + " \n " + carUID + " \n " + renterUID);

        renterNamed =findViewById(R.id.renterNamed);
        CarName = findViewById(R.id.CarName);
        dateStart = findViewById(R.id.dateStart);
        dateEnd =findViewById(R.id.dateEnd);
        paymentmethod =findViewById(R.id.paymentmethod);
        totalDay = findViewById(R.id.totalDays);
        totalPrice = findViewById(R.id.totalPrice);


        ValidID = findViewById(R.id.ValidID);
        uploadedPayment = findViewById(R.id.uploadedPayment);
        ProgressBarFaceId = findViewById(R.id.ProgressBarFaceId);
        onTheDayPayment = findViewById(R.id.onTheDayPayment);
        approvedButton = findViewById(R.id.approvedBtn);
        textView25 = findViewById(R.id.textView25);




        createNotificationChannel();
        textView25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadDocx(view);
                Toast.makeText(getApplicationContext(),"Contract Downloaded", Toast.LENGTH_SHORT).show();
            }
        });




        approvedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                transferDocuments();

            }
        });



        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("users-valid-id/" + renterUID + "/front.jpg");

        final String[] tempProfilePictureUrl = {"front.jpg"};  // Declare a final temporary variable

        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {

            tempProfilePictureUrl[0] = uri.toString();  // Assign the value to the temporary variable
            String storageUrl = uri.toString();
            // Use Glide to load the image into the ImageView
            Glide.with(this)
                    .load(storageUrl)
                    .placeholder(R.drawable.personvector)  // Placeholder image while loading (optional)
                    .error(R.drawable.personvector)       // Error image if the loading fails (optional)
                    .into(ValidID);
            ProgressBarFaceId.setVisibility(View.GONE);


        }).addOnFailureListener(e -> {
            // Handle any errors that occur while fetching the profile picture URL
        });






        GetItClean();
    }

    public void transferDocuments()
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String uid = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the "renter-processing" document
        DocumentReference renterProcessingDocument = db.collection("users")
                .document(uid)
                .collection("renter-processing")
                .document(requestID);
            // Reference to the "renter-ready" subcollection
        CollectionReference renterReadyCollection = db.collection("users")
                .document(uid)
                .collection("renter-ready");

        // Get the data from "renter-processing" document
        renterProcessingDocument.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Transfer the data to "renter-ready" subcollection
                        renterReadyCollection.document(requestID).set(documentSnapshot.getData())
                                .addOnSuccessListener(aVoid -> {
                                    // Document successfully transferred to "renter-ready" subcollection
                                    // Now, delete the document from "renter-processing"
                                    renterProcessingDocument.delete()
                                            .addOnSuccessListener(aVoid1 -> {
                                                transferDocumentsrenter();
                                            })
                                            .addOnFailureListener(e -> {
                                                // Handle errors in deleting the document
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    // Handle errors in transferring the document to "renter-ready"
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle errors in getting the document from "renter-processing"
                });


    }

    public void transferDocumentsrenter()
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String uid = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the "renter-processing" document
        DocumentReference renterProcessingDocument = db.collection("users")
                .document(uid)
                .collection("renter-ready")
                .document(requestID);
        // Reference to the "renter-ready" subcollection
        CollectionReference renterReadyCollection = db.collection("users")
                .document(renterUID)
                .collection("vehicle-ready");

        // Get the data from "renter-processing" document
        renterProcessingDocument.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Transfer the data to "renter-ready" subcollection
                        renterReadyCollection.document(requestID).set(documentSnapshot.getData())
                                .addOnSuccessListener(aVoid -> {
                                    Intent intent = new Intent(ApprovedReservation.this, ProcessingReservation.class);
                                    startActivity(intent);
                                })
                                .addOnFailureListener(e -> {
                                    // Handle errors in transferring the document to "renter-ready"
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle errors in getting the document from "renter-processing"
                });


    }

    public void GetItClean()
    {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String uid = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference rentRequestDocument = db.collection("users")
                .document(uid)
                .collection("renter-processing")
                .document(requestID);

        rentRequestDocument.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            ApprovedId = document.getString("Approved id");
                            isGcash = document.getString("isGcash");
                            renterUid = document.getString("renter uid");
                            carUID = document.getString("Car To Request");

                            DateStart = document.getString("Date Start");
                            DateEnd = document.getString("Date End");
                            totalDays= document.getString("Total Time");



                            if(isGcash.equals("true"))
                            {

                                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("users-valid-id/" + renterUID + "/gcash.jpg");

                                final String[] tempProfilePictureUrl = {"front.jpg"};  // Declare a final temporary variable

                                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    tempProfilePictureUrl[0] = uri.toString();  // Assign the value to the temporary variable
                                    String storageUrl = uri.toString();
                                    // Use Glide to load the image into the ImageView
                                    Glide.with(this)
                                            .load(storageUrl)
                                            .placeholder(R.drawable.personvector)  // Placeholder image while loading (optional)
                                            .error(R.drawable.personvector)       // Error image if the loading fails (optional)
                                            .into(uploadedPayment);


                                }).addOnFailureListener(e -> {
                                    // Handle any errors that occur while fetching the profile picture URL
                                });

                            }
                            else if(isGcash.equals("false"))
                            {
                                onTheDayPayment.setVisibility(View.VISIBLE);

                            }

                            FirebaseFirestore dbs = FirebaseFirestore.getInstance();
                            DocumentReference rentRequestDocuments = dbs.collection("car-posts").document(carUID);

                            rentRequestDocuments.get()
                                    .addOnCompleteListener(tasks -> {
                                        if (tasks.isSuccessful()) {
                                            DocumentSnapshot documents = tasks.getResult();
                                            if (documents.exists()) {
                                                // DocumentSnapshot data is available here
                                                String VehicleTitle = documents.getString("Vehicle Title");
                                                String vehiclePrice = documents.getString("Vehicle Price");

                                                paymentmethod.setText("₱ "+vehiclePrice);

                                                CarName.setText(VehicleTitle);

                                                int daysInt = Integer.parseInt(totalDays);
                                                int priceInt = Integer.parseInt(vehiclePrice);
                                                int totalPriceInt = daysInt * priceInt;
                                                String displayme = String.valueOf(totalPriceInt);
                                                totalPrice.setText("₱ " + displayme );


                                                // Now you can use the retrieved data as needed
                                                // For example, update UI elements with the data
                                            } else {
                                                // The document does not exist
                                                // Handle accordingly
                                            }
                                        } else {
                                            // An error occurred while fetching the document
                                            // Handle accordingly
                                        }
                                    });

                            FirebaseFirestore dbss = FirebaseFirestore.getInstance();
                            DocumentReference rentRequestDocumentss = dbss.collection("users").document(renterUid);
                            rentRequestDocumentss.get()
                                    .addOnCompleteListener(tasks -> {
                                        if (tasks.isSuccessful()) {
                                            DocumentSnapshot documents = tasks.getResult();
                                            if (documents.exists()) {
                                                renterName = documents.getString("name");
                                                renterNamed.setText(renterName);
                                            }
                                        }
                                    });





                            dateStart.setText(DateStart);
                            dateEnd.setText(DateEnd);
                            totalDay.setText(totalDays);






                        }
                    } else {
                        // Handle the failure scenario
                        // For example, log an error message
                    }
                });
    }

    public void downloadDocx(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            new ApprovedReservation.DownloadFileTask(this).execute();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static class DownloadFileTask extends AsyncTask<Void, Void, Void> {
        private Context context;

        DownloadFileTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            InputStream inputStream = context.getResources().openRawResource(R.raw.ownercontract);
            File outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File outputFile = new File(outputDir, "carowneragreement.docx");

            try {
                if (!outputDir.exists()) {
                    outputDir.mkdirs();
                }

                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }

                bufferedInputStream.close();
                fileOutputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception, e.g., display a toast message
                publishProgress(); // Inform onPostExecute about the error
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            showDownloadCompleteNotification();
        }

        private void showDownloadCompleteNotification() {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.heart_icon) // Use your custom small icon
                    .setContentTitle("Download Complete")
                    .setContentText("Rental Agreement.docx file downloaded")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManager.notify(1, builder.build());
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Toast.makeText(context, "Error downloading file", Toast.LENGTH_SHORT).show();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Download Channel";
            String description = "Channel for download notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}