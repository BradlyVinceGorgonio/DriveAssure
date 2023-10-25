package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class UploadDriversLicense extends AppCompatActivity {
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

    }
}