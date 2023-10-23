package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class LoginHomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_home_page);


        TextView registerTextView = findViewById(R.id.registerTextView);

        registerTextView.setMovementMethod(LinkMovementMethod.getInstance());

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, e.g., navigate to a new activity
                Intent intent = new Intent(LoginHomePage.this, RegisterPage.class);
                startActivity(intent);
            }
        });

    }
}