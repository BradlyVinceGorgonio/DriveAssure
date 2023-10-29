package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

public class IdentityVerification extends AppCompatActivity {

    CheckBox checkTermsandCondition;
    CheckBox checkPrivacyPolicy;
    Button next;
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


        checkTermsandCondition = findViewById(R.id.checkTermsandCondition);
        checkPrivacyPolicy = findViewById(R.id.checkPrivacyPolicy);
        next = findViewById(R.id.nextVerificationBtn);

// Initially, disable the button and set the color to grey
        next.setEnabled(false);
        next.setBackgroundColor(getResources().getColor(R.color.disabledGrey));

// Set up a listener for the checkboxes to change the button color and enable/disable
        CompoundButton.OnCheckedChangeListener checkBoxListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkTermsandCondition.isChecked() && checkPrivacyPolicy.isChecked()) {
                    // Both checkboxes are checked, so set the color to blue
                    next.setEnabled(true);
                    next.setBackgroundColor(getResources().getColor(R.color.blue));
                } else {
                    // If either checkbox is unchecked, set the color to grey and disable the button
                    next.setEnabled(false);
                    next.setBackgroundColor(getResources().getColor(R.color.disabledGrey));
                }
            }
        };

        checkTermsandCondition.setOnCheckedChangeListener(checkBoxListener);
        checkPrivacyPolicy.setOnCheckedChangeListener(checkBoxListener);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkTermsandCondition.isChecked() && checkPrivacyPolicy.isChecked()) {
                    // Both checkboxes are checked, so you can proceed with your desired action.
                    // For example, start a new activity or perform a specific task.
                    next.setBackgroundColor(getResources().getColor(R.color.blue));
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
                else {
                    // Handle the case when one or both checkboxes are not checked, e.g., display a message.

                    // Set the disabled color (grey)
                    next.setBackgroundColor(getResources().getColor(R.color.disabledGrey));
                }

            }
        });

    }
}