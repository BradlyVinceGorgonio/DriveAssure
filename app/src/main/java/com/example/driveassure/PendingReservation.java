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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PendingReservation extends AppCompatActivity {
String requestId;
String userId;
String carPostUid;
String pickUpArea;
String returnArea;
String DateStart;
String DateEnd;
String TimeStart;
String TimeEnd;
String ownerUserUidResult;
String totalTime;
String requestName;
String VehicleTitle;
String ContactNumber;

TextView renterName;
TextView renterContact;
TextView datePickerFromButton;
TextView timeStartedFrom;
TextView pickUpLocation;
TextView datePickerUntilButton;
TextView timeFinishedFrom;
TextView returnLocation;
Button rejectBtn;
Button acceptBtn;

ImageView renterProfile;

String renterUID;
String carUID;
String requestID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_reservation);


        // Inside the onCreate() method or wherever you need to retrieve the values
        Intent intent = getIntent();
        renterUID = intent.getStringExtra("renterUID");
        carUID = intent.getStringExtra("carUID");
        requestID = intent.getStringExtra("requestID");
        // Now you have the values in the variables renterUID, carUID, and requestID


        renterName = findViewById(R.id.renterName);
        renterContact = findViewById(R.id.renterContact);
        datePickerFromButton = findViewById(R.id.datePickerFromButton);
        timeStartedFrom = findViewById(R.id.timeStartedFrom);
        pickUpLocation =findViewById(R.id.pickUpLocation);
        datePickerUntilButton = findViewById(R.id.datePickerUntilButton);
        timeFinishedFrom = findViewById(R.id.timeFinishedFrom);
        returnLocation = findViewById(R.id.returnLocation);
        rejectBtn = findViewById(R.id.rejectBtn);
        acceptBtn = findViewById(R.id.acceptBtn);

        renterProfile = findViewById(R.id.renterProfile);

        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetItClean();


            }
        });
        fetchDataFromFirestore();
    }

    private void showCustomDialog() {
        reservationacceptedpopup customDialog = new reservationacceptedpopup(this);
        customDialog.show();
    }


    public void GetItClean()
    {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String uid = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference rentRequestDocument = db.collection("users")
                .document(uid)
                .collection("owner-view-rent-request")
                .document(requestID);

        rentRequestDocument.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            requestId = document.getString("Request Id");

                            userId = document.getString("uid ");
                            carPostUid = document.getString("Car To Request");
                            pickUpArea = document.getString("Pickup Location");
                            ownerUserUidResult = document.getString("Car Owner uid");
                            totalTime = document.getString("Total Time");

                            returnArea = document.getString("Return Location");
                            DateStart = document.getString("Date Start");
                            DateEnd = document.getString("Date End");
                            TimeStart = document.getString("Time Start");
                            TimeEnd = document.getString("Time End");

                            // Assuming "uid" is the UID you want to fetch data for
                            DocumentReference userDocument = db.collection("users").document(renterUID);

                            userDocument.get().addOnCompleteListener(tasks -> {
                                if (tasks.isSuccessful()) {
                                    DocumentSnapshot documente = tasks.getResult();
                                    if (documente.exists()) {
                                        // Access the data here
                                        requestName = documente.getString("name");
                                        ContactNumber = documente.getString("contact number");



                                        // Assuming "uid" is the UID you want to fetch data for
                                        DocumentReference userDocumente = db.collection("car-posts").document(carPostUid);

                                        userDocumente.get().addOnCompleteListener(taskse -> {
                                            if (taskse.isSuccessful()) {
                                                DocumentSnapshot documentre = taskse.getResult();
                                                if (documentre.exists()) {
                                                    // Access the data here
                                                    VehicleTitle = documentre.getString("Vehicle Title");

                                                    InsertDatas(requestId, userId, carPostUid, pickUpArea, ownerUserUidResult, totalTime, returnArea, DateStart, DateEnd, TimeStart, TimeEnd);

                                                    // Use the fetched data as needed
                                                } else {
                                                    // Document does not exist
                                                    // Handle this case as needed
                                                }
                                            } else {
                                                // Handle the failure scenario
                                                // For example, log an error message
                                            }
                                        });

                                        // Use the fetched data as needed
                                    } else {
                                        // Document does not exist
                                        // Handle this case as needed
                                    }
                                } else {
                                    // Handle the failure scenario
                                    // For example, log an error message
                                }
                            });


                        }
                    } else {
                        // Handle the failure scenario
                        // For example, log an error message
                    }
                });
    }
    public void InsertDatas(String requestId,String userId,String carPostUid,String  pickUpArea,String ownerUserUidResult,String totalTime,String returnArea, String DateStart,String DateEnd,String TimeStart, String TimeEnd)
    {
        String randomUid = UUID.randomUUID().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String ownerUserUid = user.getUid();




        // Reference to the "users" collection
        CollectionReference usersCollection = db.collection("users");

        // Reference to the "tae" document inside the "users" collection
        DocumentReference taeDocument = usersCollection.document(userId);

        // Reference to the "vehicle-request" subcollection inside the "tae" document
        CollectionReference vehicleRequestCollection = taeDocument.collection("vehicle-approved");

        // Create a document with the current user's UID
        DocumentReference userDocument = vehicleRequestCollection.document(randomUid);

        // Create a data object with the "name" field using a HashMap
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("Car To Request", carPostUid);
        requestData.put("renter uid", userId);
        requestData.put("Time Start", TimeStart);
        requestData.put("Time End", TimeEnd);
        requestData.put("Date Start", DateStart);
        requestData.put("Date End", DateEnd);
        requestData.put("Pickup Location", pickUpArea);
        requestData.put("Return Location", returnArea);
        requestData.put("Approved Id", randomUid);
        requestData.put("Car Owner uid", ownerUserUid);
        requestData.put("Total Time", totalTime);


        // Set the data to the document
        userDocument.set(requestData)
                .addOnSuccessListener(aVoid -> {

                    insertMeToo(randomUid,requestId, userId, carPostUid, pickUpArea, ownerUserUidResult, totalTime, returnArea, DateStart, DateEnd, TimeStart, TimeEnd);





                })
                .addOnFailureListener(e -> {
                    // Handle errors here
                });

    }
    public void insertMeToo(String randomUid,String requestId,String userId,String carPostUid,String  pickUpArea,String ownerUserUidResult,String totalTime,String returnArea, String DateStart,String DateEnd,String TimeStart, String TimeEnd)
    {
        Log.d("MUMENRIDER", "Loob ng Insert mE To ");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String ownerUserUid = user.getUid();
        // Reference to the "users" collection
        CollectionReference usersCollections = db.collection("users");

        // Reference to the current user's document using their UID
        DocumentReference userDocuments = usersCollections.document(ownerUserUid);
        // Reference to the "vehicle-request" subcollection inside the "tae" document
        CollectionReference rentRequestCollection = userDocuments.collection("renters-approved");
        DocumentReference userDocumentsw = rentRequestCollection.document(randomUid);

        // Create a data object with the "name" field using a HashMap
        Map<String, Object> requestDatas = new HashMap<>();
        requestDatas.put("Car To Request", carPostUid);
        requestDatas.put("renter uid", userId);
        requestDatas.put("Time Start", TimeStart);
        requestDatas.put("Time End", TimeEnd);
        requestDatas.put("Date Start", DateStart);
        requestDatas.put("Date End", DateEnd);
        requestDatas.put("Pickup Location", pickUpArea);
        requestDatas.put("Return Location", returnArea);
        requestDatas.put("Approved Id", randomUid);
        requestDatas.put("Car Owner uid", ownerUserUid);
        requestDatas.put("Total Time", totalTime);

        Log.d("MUMENRIDER", requestDatas.toString());


        // Update the document with the new data
        userDocumentsw.set(requestDatas)
                .addOnSuccessListener(aVoidst -> {
                    removeVehicleRequest();
                    removeOwnerViewRentRequest();
                    insertMeToo2(randomUid,requestId,userId,carPostUid,  pickUpArea, ownerUserUidResult, totalTime,returnArea,  DateStart, DateEnd, TimeStart, TimeEnd);

                })
                .addOnFailureListener(e -> {
                    // Handle errors here
                });


    }
    public void insertMeToo2(String randomUid,String requestId,String userId,String carPostUid,String  pickUpArea,String ownerUserUidResult,String totalTime,String returnArea, String DateStart,String DateEnd,String TimeStart, String TimeEnd)
    {
        Log.d("MUMENRIDER", "Loob ng Insert mE To ");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String ownerUserUid = user.getUid();
        // Reference to the "users" collection
        CollectionReference usersCollections = db.collection("rent-transactions");

        // Reference to the current user's document using their UID
        DocumentReference userDocuments = usersCollections.document(randomUid);
        // Reference to the "vehicle-request" subcollection inside the "tae" document


        // Create a data object with the "name" field using a HashMap
        Map<String, Object> requestDatas = new HashMap<>();
        requestDatas.put("Car To Request", carPostUid);
        requestDatas.put("renter uid", userId);
        requestDatas.put("Time Start", TimeStart);
        requestDatas.put("Time End", TimeEnd);
        requestDatas.put("Date Start", DateStart);
        requestDatas.put("Date End", DateEnd);
        requestDatas.put("Pickup Location", pickUpArea);
        requestDatas.put("Return Location", returnArea);
        requestDatas.put("Approved Id", randomUid);
        requestDatas.put("Car Owner uid", ownerUserUid);
        requestDatas.put("Total Time", totalTime);

        Log.d("MUMENRIDER", requestDatas.toString());


        // Update the document with the new data
        userDocuments.set(requestDatas)
                .addOnSuccessListener(aVoidst -> {

                    showCustomDialog();
                })
                .addOnFailureListener(e -> {
                    // Handle errors here
                });


    }

    public void removeVehicleRequest()
    {
        Intent intent = getIntent();
        String requestID = intent.getStringExtra("requestID");

        Log.d("MUMENRIDER", "APPROVEEEE#ED");

        FirebaseFirestore dbs = FirebaseFirestore.getInstance();
        FirebaseAuth auths = FirebaseAuth.getInstance();

        FirebaseUser currentUser = auths.getCurrentUser();



        DocumentReference rentRequestDocument = dbs.collection("users")
                .document(renterUID)
                .collection("vehicle-request")
                .document(requestID);
        rentRequestDocument.delete()
                .addOnSuccessListener(aVoidse -> {
                    // Document successfully deleted
                    Log.d("MUMENRIDER ", "DELETED ANG DATAAAAA");
                    Log.d("MUMENRIDER ", "requestID" + requestID);
                })
                .addOnFailureListener(e -> {
                    // Handle errors here
                    Log.d("MUMENRIDER ","SIRA AND DELETED" + e.getMessage());
                });

    }

    public void removeOwnerViewRentRequest()
    {
        Intent intent = getIntent();
        String requestID = intent.getStringExtra("requestID");

        Log.d("MUMENRIDER", "APPROVEEEE#ED");

        FirebaseFirestore dbs = FirebaseFirestore.getInstance();
        FirebaseAuth auths = FirebaseAuth.getInstance();
        String owner = auths.getUid();

        FirebaseUser currentUser = auths.getCurrentUser();

        String ownering = currentUser.getUid();



        DocumentReference rentRequestDocument = dbs.collection("users")
                .document(ownering)
                .collection("owner-view-rent-request")
                .document(requestID);
        rentRequestDocument.delete()
                .addOnSuccessListener(aVoidse -> {
                    // Document successfully deleted

                })
                .addOnFailureListener(e -> {
                    // Handle errors here
                    Log.d("MUMENRIDER ","SIRA AND DELETED" + e.getMessage());
                });

    }



    public void fetchDataFromFirestore()
    {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String uid = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference rentRequestDocument = db.collection("users")
                .document(uid)
                .collection("owner-view-rent-request")
                .document(requestID);

        rentRequestDocument.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            requestId = document.getString("Request Id");

                            userId = document.getString("uid ");
                            carPostUid = document.getString("Car To Request");
                            pickUpArea = document.getString("Pickup Location");
                            ownerUserUidResult = document.getString("Car Owner uid");
                            totalTime = document.getString("Total Time");

                            returnArea = document.getString("Return Location");
                            DateStart = document.getString("Date Start");
                            DateEnd = document.getString("Date End");
                            TimeStart = document.getString("Time Start");
                            TimeEnd = document.getString("Time End");

                            // Assuming "uid" is the UID you want to fetch data for
                            DocumentReference userDocument = db.collection("users").document(renterUID);

                            userDocument.get().addOnCompleteListener(tasks -> {
                                if (tasks.isSuccessful()) {
                                    DocumentSnapshot documente = tasks.getResult();
                                    if (documente.exists()) {
                                        // Access the data here
                                        requestName = documente.getString("name");
                                        ContactNumber = documente.getString("contact number");



                                        // Assuming "uid" is the UID you want to fetch data for
                                        DocumentReference userDocumente = db.collection("car-posts").document(carPostUid);

                                        userDocumente.get().addOnCompleteListener(taskse -> {
                                            if (taskse.isSuccessful()) {
                                                DocumentSnapshot documentre = taskse.getResult();
                                                if (documentre.exists()) {
                                                    // Access the data here
                                                    VehicleTitle = documentre.getString("Vehicle Title");

                                                    renterName.setText(requestName);
                                                    renterContact.setText(ContactNumber);
                                                    datePickerFromButton.setText(DateStart);
                                                    timeStartedFrom.setText(TimeStart);
                                                    pickUpLocation.setText(pickUpArea);
                                                    datePickerUntilButton.setText(DateEnd);
                                                    timeFinishedFrom.setText(TimeEnd);
                                                    returnLocation.setText(returnArea);

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

                                                    // Use the fetched data as needed
                                                } else {
                                                    // Document does not exist
                                                    // Handle this case as needed
                                                }
                                            } else {
                                                // Handle the failure scenario
                                                // For example, log an error message
                                            }
                                        });

                                        // Use the fetched data as needed
                                    } else {
                                        // Document does not exist
                                        // Handle this case as needed
                                    }
                                } else {
                                    // Handle the failure scenario
                                    // For example, log an error message
                                }
                            });


                        }
                    } else {
                        // Handle the failure scenario
                        // For example, log an error message
                    }
                });
    }
}