package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;



public class UploadDriversLicense extends AppCompatActivity {
    ImageButton backButtons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_drivers_license);


        backButtons = findViewById(R.id.backButton12sa);
        backButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadDriversLicense.this, FaceVerification.class);
                startActivity(intent);
            }
        });

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