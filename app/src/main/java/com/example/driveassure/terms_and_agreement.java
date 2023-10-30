package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class terms_and_agreement extends AppCompatActivity {
CheckBox AgreeTermsandAgreement;
Button confirmAgreeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_agreement);


        Intent intent = getIntent();
        String message = intent.getStringExtra("EXTRA_MESSAGE");
        // Vehicle Type
        Log.d("POWERPOWER", "onClick Kaka access lang ng terms and agreement : " + message);
        

        AgreeTermsandAgreement = findViewById(R.id.AgreeTermsandAgreement);
        confirmAgreeBtn = findViewById(R.id.confirmAgreeBtn);

        // Initially, set the button to disabled and gray
        confirmAgreeBtn.setEnabled(false);
        confirmAgreeBtn.setBackgroundColor(getResources().getColor(R.color.disabledGrey));

        AgreeTermsandAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // If the checkbox is checked, enable the button and set it to blue
                    confirmAgreeBtn.setEnabled(true);
                    confirmAgreeBtn.setBackgroundColor(getResources().getColor(R.color.blue));
                } else {
                    // If the checkbox is not checked, disable the button and set it to gray
                    confirmAgreeBtn.setEnabled(false);
                    confirmAgreeBtn.setBackgroundColor(getResources().getColor(R.color.disabledGrey));
                }
            }
        });

        confirmAgreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the string from the intent
                Intent intent = getIntent();
                String message = intent.getStringExtra("EXTRA_MESSAGE");
                // Vehicle Type
                Log.d("POWERPOWER", "onClick Pag pindot ng confirm sa terms : " + message);




                // Add the string as an extra
                Intent intents = new Intent(terms_and_agreement.this, CreatingListingActivity.class);
                intents.putExtra("EXTRA_MESSAGE", message);
                Log.d("POWERPOWER", "onClick: 2" + message);
                // Start the receiver activity
                startActivity(intents);
            }
        });

    }
}