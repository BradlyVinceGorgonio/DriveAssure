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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class UploadDriversLicense extends AppCompatActivity {


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;


    ImageView frontPhotoLicense;
    ImageView backPhotoLicense;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_drivers_license);




        Intent intents = getIntent();

        String Name = intents.getStringExtra("name");
        String Email = intents.getStringExtra("email");
        String Number = intents.getStringExtra("number");
        String Password = intents.getStringExtra("password");
        String RePassword = intents.getStringExtra("repassword");

        // Now you have access to the data in the new activity.
        Log.d("TAEBRADLY", "Name " + Name + " Email " + Email  + " Number " + Number + " Password " + Password + " RePassword " + RePassword  ) ;



        Button frontImage = findViewById(R.id.frontlicense);
        frontImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageDialog();
            }
        });

        Button backlicense = findViewById(R.id.backlicense);
        backlicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageDialog();
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