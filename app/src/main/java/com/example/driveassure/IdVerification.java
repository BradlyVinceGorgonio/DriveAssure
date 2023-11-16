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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class IdVerification extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_CAPTUREBACK = 3;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int REQUEST_IMAGE_PICKBACK = 4;



    private boolean isButton1Clicked = false;
    private boolean isButton2Clicked = false;
    Button uploadvalidFrontID;
    Button uploadvalidBackID;
    Button NextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_verification);



        deleteLicenseDirectory();

        uploadvalidFrontID = findViewById(R.id.uploadvalidFrontID);
        uploadvalidFrontID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageDialog();
                isButton1Clicked = true;
                checkAndUpdateButtonState();
            }
        });

        uploadvalidBackID = findViewById(R.id.uploadvalidBackID);
        uploadvalidBackID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBackImageDialog();
                isButton2Clicked = true;
                checkAndUpdateButtonState();
            }
        });

        NextButton = findViewById(R.id.NextButton);

        NextButton.setEnabled(false);
        NextButton.setBackgroundColor(getResources().getColor(R.color.disabledGrey));
        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


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
                    uploadvalidFrontID = findViewById(R.id.uploadvalidFrontID);

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
                    uploadvalidFrontID = findViewById(R.id.uploadvalidFrontID);

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
                    uploadvalidBackID = findViewById(R.id.uploadvalidBackID);
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
                    uploadvalidBackID = findViewById(R.id.uploadvalidBackID);
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
    private void checkAndUpdateButtonState() {
        // Check if both buttons have been clicked
        if (isButton1Clicked && isButton2Clicked) {
            // Both buttons are clicked, set the boolean to true
            updateSubmitButtonState();
        }
    }
    private void updateSubmitButtonState() {
       if(isButton1Clicked && isButton2Clicked)
       {
           NextButton.setEnabled(true);
           NextButton.setBackgroundColor(getResources().getColor(R.color.blue));

       }

    }
    private void saveImageToDownloads(Bitmap imageBitmap, String fileName) {
        // Get the Downloads directory
        File downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        // Create a subdirectory named "license" if it doesn't exist
        File licenseDirectory = new File(downloadsDirectory, "validIdRenter");
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

        //LAGAY DITO
        Intent intent2 = new Intent(IdVerification.this, DownloadDocumentContract.class);
        startActivity(intent2);

    }

    private void deleteLicenseDirectory() {
        // Get the Downloads directory
        File downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        // Create a subdirectory named "license" if it doesn't exist
        File licenseDirectory = new File(downloadsDirectory, "validIdRenter");

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