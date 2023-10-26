package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class IdentityVerification2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity_verification2);

        ImageButton back = findViewById(R.id.backButton2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IdentityVerification2.this, IdentityVerification.class);
                startActivity(intent);
            }
        });
        Button next = findViewById(R.id.startFacialVerification);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intents = getIntent();

                String Name = intents.getStringExtra("name");
                String Email = intents.getStringExtra("email");
                String Number = intents.getStringExtra("number");
                String Password = intents.getStringExtra("password");
                String RePassword = intents.getStringExtra("repassword");


                Intent intent = new Intent(IdentityVerification2.this, FaceVerification.class);
                intent.putExtra("name", Name);
                intent.putExtra("email", Email);
                intent.putExtra("number", Number);
                intent.putExtra("password", Password);
                intent.putExtra("repassword", RePassword);
                startActivity(intent);
            }
        });


    }
}