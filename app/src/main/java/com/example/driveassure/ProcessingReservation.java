package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProcessingReservation extends AppCompatActivity {

    String renterUID;
    String carUID;
    String requestID;
    String renterName;
    String contactNumber;
    String carName;
    String ApprovedId;
    String isGcash;
    String renterUid;
    String DateStart;
    String DateEnd;
    String timeStart;
    String timeEnd;
    String totalDays;
    String vehiclePrice;
    String VehicleTitle;
    String transmission;
    String fuel;
    String brand;
    String pickupLocation;
    String ReturnLocation;
    ImageView renterProfile;
    TextView renterNames;
    TextView renterContact;
    TextView vehicleTitle;
    TextView vehiclePrices;
    TextView vehicleTransmission;
    TextView fuelType;
    TextView vehicBrand;
    TextView carOwnerName;
    TextView carOwnerrContact;
    TextView pickUpDate;
    TextView PickupTime;
    TextView PickupLocation;
    TextView returningRenterName;
    TextView returnrenterContact;
    TextView returnDate;
    TextView returnTime;
    TextView returnLocation;
    TextView rentingDays;
    TextView totalPayment;
    Button exchangeContracts;
    Button startTime;
    String result;

    private CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing_reservation);

        Intent intent = getIntent();
        renterUID = intent.getStringExtra("renterUID");
        carUID = intent.getStringExtra("carUID");
        requestID = intent.getStringExtra("requestID");
        renterName = intent.getStringExtra("renterName");
        carName = intent.getStringExtra("carName");



        renterProfile = findViewById(R.id.renterProfile);
        renterNames = findViewById(R.id.renterName);
        renterContact = findViewById(R.id.renterContact);
        vehicleTitle = findViewById(R.id.vehicleTitle);
        vehiclePrices = findViewById(R.id.vehiclePrice);
        vehicleTransmission = findViewById(R.id.vehicleTransmission);
        fuelType = findViewById(R.id.fuelType);
        vehicBrand = findViewById(R.id.vehicBrand);
        carOwnerName = findViewById(R.id.carOwnerName);
        pickUpDate = findViewById(R.id.pickUpDate);
        PickupTime = findViewById(R.id.PickupTime);
        PickupLocation = findViewById(R.id.PickupLocation);
        returningRenterName = findViewById(R.id.returningRenterName);
        returnrenterContact = findViewById(R.id.returnrenterContact);
        returnDate = findViewById(R.id.returnDate);
        returnTime = findViewById(R.id.returnTime);
        returnLocation = findViewById(R.id.returnLocation);
        exchangeContracts = findViewById(R.id.exchangeContracts);
        startTime = findViewById(R.id.startTime);
        carOwnerrContact =findViewById(R.id.carOwnerrContact);
        rentingDays = findViewById(R.id.rentingDays);
        totalPayment = findViewById(R.id.totalPayment);




        exchangeContracts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTime.setBackgroundColor(getResources().getColor(R.color.blue));
                startTime.setEnabled(true);
            }
        });
        startTime.setBackgroundColor(getResources().getColor(R.color.disabledGrey));
        startTime.setEnabled(false);

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String buttonText = ((Button) view).getText().toString();

                if (buttonText.equals("Time's up!")) {
                    Dialog dialog = new Dialog(ProcessingReservation.this);
                    dialog.setContentView(R.layout.confirmcar);
                    dialog.show();
                    // Find the 'YES' button in the dialog layout
                    Button yesButton = dialog.findViewById(R.id.OKayButton);
                    Button noButton = dialog.findViewById(R.id.NotYetButton);
                    noButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    yesButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss(); // Close the dialog if needed
                            Dialog dialogs = new Dialog(ProcessingReservation.this);
                            dialogs.setContentView(R.layout.onprocessdone);
                            dialogs.show();
                            // Find the 'YES' button in the dialog layout
                            Button yesButton = dialogs.findViewById(R.id.OKayButton);
                            yesButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteDocument(requestID);
                                    dialogs.dismiss(); // Close the dialog if needed
                                    Intent newActivityIntent = new Intent(ProcessingReservation.this, userHome.class);
                                    startActivity(newActivityIntent);
                                }
                            });
                        }
                    });

                } else {
                    String totalTime = calculateTotalTime(DateStart, timeStart, DateEnd, timeEnd);
                    Log.d("POWERBIGLA", "total T " + totalTime);
                    startCountdown(totalTime);
                    startTime.setBackgroundColor(getResources().getColor(R.color.disabledGrey));
                    startTime.setEnabled(false);

                }


            }
        });
        GetItClean();
    }
    private void startCountdown(String totalTime) {
        long millisInFuture = getTimeDifference(totalTime);

        countDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerText(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                handleTimerDone();
            }
        };

        countDownTimer.start();
    }

    private void updateTimerText(long millisUntilFinished) {
        long days = millisUntilFinished / (24 * 60 * 60 * 1000);
        long hours = (millisUntilFinished % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
        long minutes = (millisUntilFinished % (60 * 60 * 1000)) / (60 * 1000);
        long seconds = (millisUntilFinished % (60 * 1000)) / 1000;

        String timeLeft = String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds);
        startTime.setText("Time Left: " + timeLeft);
    }

    private long getTimeDifference(String totalTime) {
        try {
            String[] parts = totalTime.split(",");
            long days = 0;
            long minutes = 0;

            for (String part : parts) {
                String trimmedPart = part.trim();
                if (trimmedPart.endsWith("days")) {
                    days = Long.parseLong(trimmedPart.split("\\s+")[0]);
                } else if (trimmedPart.endsWith("minutes")) {
                    minutes = Long.parseLong(trimmedPart.split("\\s+")[0]);
                }
            }

            return days * (24 * 60 * 60 * 1000) + minutes * (60 * 1000);

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return 0;
        }
    }


    private void handleTimerDone() {
        startTime.setText("Time's up!");
        // Perform actions when the timer is done
        startTime.setBackgroundColor(getResources().getColor(R.color.blue));
        startTime.setEnabled(true);
    }

    private void deleteDocument(String requestID) {
        // Get the current user
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            // Get the user's UID
            String uid = currentUser.getUid();

            // Access Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Reference to the document to be deleted
            DocumentReference rentRequestDocument = db.collection("users")
                    .document(uid)
                    .collection("renter-ready")
                    .document(requestID);

            // Delete the document
            rentRequestDocument.delete()
                    .addOnSuccessListener(aVoid -> {
                        // Document successfully deleted
                        System.out.println("DocumentSnapshot successfully deleted!");
                    })
                    .addOnFailureListener(e -> {
                        // Handle any errors
                        System.err.println("Error deleting document: " + e.getMessage());
                    });
        } else {
            // Handle the case where the current user is null (not authenticated)
            System.err.println("User not authenticated");
        }
    }

    public void GetItClean()
    {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String uid = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference rentRequestDocument = db.collection("users")
                .document(uid)
                .collection("renter-ready")
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
                            timeStart = document.getString("Time Start");
                            timeEnd = document.getString("Time End");
                            pickupLocation = document.getString("Pickup Location");
                            ReturnLocation = document.getString("Return Location");


                            pickUpDate.setText(DateStart);
                            PickupTime.setText(timeStart);
                            PickupLocation.setText(pickupLocation);
                            returnDate.setText(DateEnd);
                            returnTime.setText(timeEnd);
                            returnLocation.setText(ReturnLocation);
                            rentingDays.setText(totalDays);

                            result = calculateTotalTime(DateStart, timeStart, DateEnd, timeEnd);

                            int daysConverted = Integer.parseInt(totalDays);
                            String remaining = calculateTimeLeft(timeStart,timeEnd);

                            if(daysConverted <= 1)
                            {
                                startTime.setText("Start " + result);

                            }
                            else
                            {
                                startTime.setText("Start " + result);

                            }


                            // Image uploaded successfully
                            Dialog dialog = new Dialog(ProcessingReservation.this);
                            dialog.setContentView(R.layout.gobackhome);
                            dialog.show();
                            // Find the 'YES' button in the dialog layout
                            Button yesButton = dialog.findViewById(R.id.OKayButton);
                            TextView messageText  = dialog.findViewById(R.id.messageText);
                            messageText.setText("Not yet time to start the timer! Rental day begins on " +DateStart + " at " + timeStart + ". Please wait for the event day to commence the renting period. Thank you!");
                            yesButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss(); // Close the dialog if needed
                                    Intent newActivityIntent = new Intent(ProcessingReservation.this, userHome.class);
                                    startActivity(newActivityIntent);
                                }
                            });



                            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("users/" + renterUID + "/face.jpg");

                            final String[] tempProfilePictureUrl = {"face.jpg"};  // Declare a final temporary variable

                            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                tempProfilePictureUrl[0] = uri.toString();  // Assign the value to the temporary variable
                                String storageUrl = uri.toString();
                                // Use Glide to load the image into the ImageView
                                Glide.with(this)
                                        .load(storageUrl)
                                        .placeholder(R.drawable.personvector)  // Placeholder image while loading (optional)
                                        .error(R.drawable.personvector)       // Error image if the loading fails (optional)
                                        .into(renterProfile);


                            }).addOnFailureListener(e -> {
                                // Handle any errors that occur while fetching the profile picture URL
                            });

                            FirebaseFirestore dbs = FirebaseFirestore.getInstance();
                            DocumentReference rentRequestDocuments = dbs.collection("car-posts").document(carUID);

                            rentRequestDocuments.get()
                                    .addOnCompleteListener(tasks -> {
                                        if (tasks.isSuccessful()) {
                                            DocumentSnapshot documents = tasks.getResult();
                                            if (documents.exists()) {
                                                // DocumentSnapshot data is available here
                                                VehicleTitle = documents.getString("Vehicle Title");
                                                vehiclePrice = documents.getString("Vehicle Price");
                                                transmission = documents.getString("Vehicle Transmission");
                                                fuel = documents.getString("Vehicle Fuel Type");
                                                brand = documents.getString("Vehicle Brand");

                                                vehicleTitle.setText(VehicleTitle);
                                                vehiclePrices.setText(vehiclePrice);
                                                vehicleTransmission.setText(transmission);
                                                fuelType.setText(fuel);
                                                vehicBrand.setText(brand);




                                                int daysInt = Integer.parseInt(totalDays);
                                                int priceInt = Integer.parseInt(vehiclePrice);
                                                int totalPriceInt = daysInt * priceInt;
                                                String displayme = String.valueOf(totalPriceInt);
                                                totalPayment.setText("₱ " + displayme);


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
                                                contactNumber = documents.getString("contact number");

                                                renterNames.setText(renterName);
                                                renterContact.setText(contactNumber);
                                                carOwnerName.setText("Michael Afton");
                                                carOwnerrContact.setText(contactNumber);
                                                returningRenterName.setText(renterName);

                                            }
                                        }
                                    });

                        }
                    } else {
                        // Handle the failure scenario
                        // For example, log an error message
                    }
                });
    }
    private static String calculateTotalTime(String dateStart, String timeStart, String dateEnd, String timeEnd) {
        try {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

            // Combine date and time strings into Date objects
            Date startDateTime = dateTimeFormat.parse(dateStart + " " + timeStart);
            Date endDateTime = dateTimeFormat.parse(dateEnd + " " + timeEnd);

            // Calculate the time difference in minutes
            long timeDifference = endDateTime.getTime() - startDateTime.getTime();
            long minutesDifference = timeDifference / (60 * 1000);

            if (minutesDifference < 1440) { // If less than a day
                return minutesDifference + " minutes";
            } else {
                long days = minutesDifference / 1440;
                long remainingMinutes = minutesDifference % 1440;
                return days + " days, " + remainingMinutes + " minutes";
            }

        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the exception or return an appropriate value
            return "Error"; // or throw an exception
        }
    }

    private static String calculateTimeLeft(String startTime, String endTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

            // Parse the time strings into Date objects
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);

            // Calculate the time difference in seconds
            long timeDifference = (endDate.getTime() - startDate.getTime()) / 1000;

            if (timeDifference >= 60 * 60) {
                // If the time difference is 1 hour or more, return hours
                long hoursLeft = timeDifference / (60 * 60);
                return hoursLeft + " hours";
            } else if (timeDifference >= 60) {
                // If the time difference is 1 minute or more, return minutes
                long minutesLeft = timeDifference / 60;
                return minutesLeft + " minutes";
            } else {
                // If the time difference is less than 1 minute, return seconds
                return timeDifference + " seconds";
            }

        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the exception or return an appropriate value
            return "Error"; // or throw an exception
        }
    }
}