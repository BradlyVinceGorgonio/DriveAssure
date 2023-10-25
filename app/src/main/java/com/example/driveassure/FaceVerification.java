package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class FaceVerification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_verification);




       

// Now you have access to the data in the new activity.

        Button button = findViewById(R.id.submitBtn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();

                String Name = intent.getStringExtra("name");
                String Email = intent.getStringExtra("email");
                String Number = intent.getStringExtra("number");
                String Password = intent.getStringExtra("password");
                String RePassword = intent.getStringExtra("repassword");

                Intent intents = new Intent(FaceVerification.this, UploadDriversLicense.class);
                intents.putExtra("name", Name);
                intents.putExtra("email", Email);
                intents.putExtra("number", Number);
                intents.putExtra("password", Password);
                intents.putExtra("repassword", RePassword);
                startActivity(intents);
            }
        });



    }
}