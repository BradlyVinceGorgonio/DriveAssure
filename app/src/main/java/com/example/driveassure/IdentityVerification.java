package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class IdentityVerification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity_verification);


        ImageButton backButton = findViewById(R.id.backButton1);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IdentityVerification.this, RegisterPage.class);
                startActivity(intent);
            }
        });


        Button next = findViewById(R.id.nextVerificationBtn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();

                String Name = intent.getStringExtra("name");
                String Email = intent.getStringExtra("email");
                String Number = intent.getStringExtra("number");
                String Password = intent.getStringExtra("password");
                String RePassword = intent.getStringExtra("repassword");

                // Now you have access to the data in the new activity.

                Intent intents = new Intent(IdentityVerification.this, IdentityVerification2.class);
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