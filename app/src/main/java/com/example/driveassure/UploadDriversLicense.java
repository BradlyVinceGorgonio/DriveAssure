package com.example.driveassure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class UploadDriversLicense extends AppCompatActivity {


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_CAPTUREBACK = 3;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int REQUEST_IMAGE_PICKBACK = 4;


    ImageView frontPhotoLicense;
    ImageView backPhotoLicense;
    Button frontImage;
    Button backlicense;

    EditText licenseNumber;
    EditText licenseExpiryDate;
    CheckBox registerCheck;
    Button registrationBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_drivers_license);

        // Initials
        deleteLicenseDirectory();





        // Assuming you have Firebase initialized
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        licenseNumber = findViewById(R.id.licenseNumber);
        licenseExpiryDate = findViewById(R.id.licenseExpiryDate);

        registrationBtn = findViewById(R.id.registrationBtn);
        registrationBtn.setBackgroundColor(getResources().getColor(R.color.disabledGrey));
        registerCheck = findViewById(R.id.registerCheck);

        frontImage = findViewById(R.id.frontlicense);
        frontImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageDialog();
            }
        });

        backlicense = findViewById(R.id.backlicense);
        backlicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBackImageDialog();
            }
        });

        frontPhotoLicense = findViewById(R.id.frontPhotoLicense);
        backPhotoLicense = findViewById(R.id.backPhotoLicense);


        // Set an OnTextChangedListener for licenseNumber EditText
        licenseNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateSubmitButtonState();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // Set an OnTextChangedListener for licenseExpiryDate EditText
        licenseExpiryDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateSubmitButtonState();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // Set an OnCheckedChangeListener for the checkbox
        registerCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSubmitButtonState();
            }
        });

        // Set an OnClickListener for the front image view
        frontPhotoLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add your logic for selecting an image for frontPhotoLicense
                updateSubmitButtonState();
            }
        });

        // Set an OnClickListener for the back image view
        backPhotoLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add your logic for selecting an image for backPhotoLicense
                updateSubmitButtonState();
            }
        });






        registrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intents = getIntent();

                String Name = intents.getStringExtra("name");
                String Email = intents.getStringExtra("email");
                String Number = intents.getStringExtra("number");
                String Password = intents.getStringExtra("password");
                String RePassword = intents.getStringExtra("repassword");

                // Now you have access to the data in the new activity.
                Log.d("TAEBRADLY", "Name " + Name + " Email " + Email  + " Number " + Number + " Password " + Password + " RePassword " + RePassword  ) ;

                uploadCredentialsToFireStoreAndPictures(Name, Email, Number, Password);

            }
        });


    }
    private boolean hasImage(ImageView imageView) {
        return imageView.getDrawable() != null;
    }

    private void updateSubmitButtonState() {
        boolean image1HasImage = hasImage(frontPhotoLicense);
        boolean image2HasImage = hasImage(backPhotoLicense);
        boolean isChecked = registerCheck.isChecked();
        boolean hasLicenseNumber = !TextUtils.isEmpty(licenseNumber.getText().toString());
        boolean hasExpiryDate = !TextUtils.isEmpty(licenseExpiryDate.getText().toString());

        if (image1HasImage && image2HasImage && isChecked && hasLicenseNumber && hasExpiryDate) {
            registrationBtn.setBackgroundColor(getResources().getColor(R.color.blue));
            registrationBtn.setEnabled(true);
        } else {
            registrationBtn.setBackgroundColor(getResources().getColor(R.color.disabledGrey));
            registrationBtn.setEnabled(false);
        }
    }


    private void uploadCredentialsToFireStoreAndPictures(String Name, String Email, String Number, String Password)
    {
        // Assuming you have Firebase initialized
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Step 1: Create a user in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Step 2: Get the UID and add data to Firestore
                                String uid = user.getUid();

                                // Create a map with the user data
                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put("name", Name);
                                userMap.put("email", Email);
                                userMap.put("contact number", Number);

                                // Add the user data to the Firestore "users" collection with the UID as the document name
                                db.collection("users")
                                        .document(uid)
                                        .set(userMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Document added successfully
                                                // Handle success as needed

                                                uploadPicturesToStorage(uid);


                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle errors if the document could not be added
                                            }
                                        });
                            }
                        }
                        else {
                            // Check if the error indicates that the email is already registered
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                // Email is already registered

                                Toast.makeText(getApplicationContext(), "Email is already registered.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Handle other registration errors
                                // For example, invalid email format or weak password

                                Toast.makeText(getApplicationContext(), "Registration failed. Please check your email and password.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    private void uploadPicturesToStorage(String uid) {
        // Create Firebase Storage reference
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create references for the back and front images in the "users/UID/" folder
        StorageReference backImageRef = storageRef.child("users/" + uid + "/back.jpg");
        StorageReference frontImageRef = storageRef.child("users/" + uid + "/front.jpg");

        // Create File objects for the "back.jpg" and "front.jpg" files
        File downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File licenseDirectory = new File(downloadsDirectory, "license");
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
        // Implement the next steps after both images are uploaded
        // This can include updating the UI, saving data to Firestore, etc.
        Intent intent = new Intent(UploadDriversLicense.this, userHome.class);
        startActivity(intent);
    }


    private void openImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image Source");
        builder.setItems(new CharSequence[]{"Camera", "Gallery"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // User chose Camera
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                } else {
                    // User chose Gallery
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
                }
            }
        });
        builder.show();
    }

    private void openBackImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image Source");
        builder.setItems(new CharSequence[]{"Camera", "Gallery"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // User chose Camera
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTUREBACK); // Use REQUEST_IMAGE_CAPTUREBACK here
                } else {
                    // User chose Gallery
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, REQUEST_IMAGE_PICKBACK); // Use REQUEST_IMAGE_PICKBACK here
                }
            }
        });
        builder.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == REQUEST_IMAGE_CAPTURE)
            {
                // Handle the image captured from the camera here
                // The captured image will be in the 'data' intent
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                if (imageBitmap != null)
                {
                    Log.d("NUYON", "onActivityResult IMAGE BITMAP: " + imageBitmap);

                    // Set the image bitmap to the ImageView
                    frontPhotoLicense = findViewById(R.id.frontPhotoLicense);
                    frontPhotoLicense.setImageBitmap(imageBitmap);

                    // You can also save the image if needed
                    saveImageToDownloads(imageBitmap, "front.jpg");
                } else
                {
                    Log.d("NUYON", "onActivityResult: Image Bitmap is null");
                }
            }
            else if (requestCode == REQUEST_IMAGE_PICK)
            {
                // Handle the selected image from the gallery here
                if (data != null)
                {
                    Uri imageUri = data.getData();
                    frontPhotoLicense = findViewById(R.id.frontPhotoLicense);
                    frontPhotoLicense.setImageURI(imageUri);

                    // You can also save the image if needed, e.g., to a file
                    // The image data is now accessible via the 'imageUri'
                    Bitmap selectedImage = getImageFromUri(imageUri);
                    if (selectedImage != null) {
                        saveImageToDownloads(selectedImage, "front.jpg");
                    }
                }
            }
            else if (requestCode == REQUEST_IMAGE_CAPTUREBACK)
            {
                // Handle the image captured from the camera here for the backLicense
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                if (imageBitmap != null) {
                    // Set the image bitmap to the backLicense ImageView
                    backPhotoLicense = findViewById(R.id.backPhotoLicense);
                    backPhotoLicense.setImageBitmap(imageBitmap);
                    updateSubmitButtonState();
                    // You can also save the image with a different filename if needed
                    saveImageToDownloads(imageBitmap, "back.jpg");
                } else {
                    Log.d("NUYON", "onActivityResult: Image Bitmap is null for backLicense");
                }

            }
            else if(requestCode == REQUEST_IMAGE_PICKBACK)
            {
                // Handle the selected image from the gallery here for the backLicense
                if (data != null) {
                    Uri imageUri = data.getData();
                    backPhotoLicense = findViewById(R.id.backPhotoLicense);
                    backPhotoLicense.setImageURI(imageUri);
                    updateSubmitButtonState();

                    // You can also save the image with a different filename if needed
                    Bitmap selectedImage = getImageFromUri(imageUri);
                    if (selectedImage != null) {
                        saveImageToDownloads(selectedImage, "back.jpg");
                    }
                }
            }
            else
            {
                Log.d("NUYON", "onActivityResult:  Wala nangyari");

            }
        }
        else
        {
            Log.d("WADAPAK", "Not Result Ok Okay?");

        }
    }

    private void saveImageToDownloads(Bitmap imageBitmap, String fileName) {
        // Get the Downloads directory
        File downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        // Create a subdirectory named "license" if it doesn't exist
        File licenseDirectory = new File(downloadsDirectory, "license");
        if (!licenseDirectory.exists()) {
            licenseDirectory.mkdirs();
        }

        // Create a file for the image in the "license" directory
        File imageFile = new File(licenseDirectory, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            Log.d("NUYON", "Image saved to: " + imageFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteLicenseDirectory() {
        // Get the Downloads directory
        File downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        // Create a subdirectory named "license" if it doesn't exist
        File licenseDirectory = new File(downloadsDirectory, "license");

        if (licenseDirectory.exists() && licenseDirectory.isDirectory()) {
            deleteDirectory(licenseDirectory);
        }
    }
    // Function to delete a directory and its contents
    private void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        if (file.delete()) {
                            Log.d("NUYON", "File deleted: " + file.getAbsolutePath());
                        } else {
                            Log.e("NUYON", "Failed to delete file: " + file.getAbsolutePath());
                        }
                    }
                }
            }
        }
        if (directory.delete()) {
            Log.d("NUYON", "Folder deleted: " + directory.getAbsolutePath());
        } else {
            Log.e("NUYON", "Failed to delete folder: " + directory.getAbsolutePath());
        }
    }

    private Bitmap getImageFromUri(Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0) {
            // Check if the permissions were granted
            if (grantResults.length == 2 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // You can now use the camera and storage
            } else {
                Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }
}