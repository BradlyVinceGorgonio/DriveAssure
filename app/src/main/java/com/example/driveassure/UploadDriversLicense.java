package com.example.driveassure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


public class UploadDriversLicense extends AppCompatActivity {


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;


    ImageView frontPhotoLicense;
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
    }
    private void openImageDialog() {
        // Create an intent to open the camera or gallery
        Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageIntent.setType("image/*");

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Create a chooser for both the camera and gallery options
        Intent chooserIntent = Intent.createChooser(imageIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});

        startActivityForResult(chooserIntent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICK) {
                // Handle the selected image from the gallery here
                if (data != null) {
                    Uri imageUri = data.getData();
                    frontPhotoLicense = findViewById(R.id.frontPhotoLicense);
                    frontPhotoLicense.setImageURI(imageUri);
                    Log.d("NUYON", "onActivityResult: " + imageUri);
                    // You can also save the image if needed, e.g., to a file
                    // The image data is now accessible via the 'imageUri'
                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Handle the image captured from the camera here
                // The captured image will be in the 'data' intent
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                Log.d("NUYON", "onActivityResult: " + imageBitmap);

                // Set the image bitmap to the ImageView
                frontPhotoLicense = findViewById(R.id.frontPhotoLicense);
                frontPhotoLicense.setImageBitmap(imageBitmap);

                // You can also save the image if needed
            }
        }
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