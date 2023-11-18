package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class PaymentAndSignature extends AppCompatActivity {

    private boolean answer = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_and_signature);

        Button button = findViewById(R.id.continueBtn);

        // Find the RadioGroup and RadioButtons in your layout
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        RadioButton gcashPaymentRadioButton = findViewById(R.id.gcashPayment);
        RadioButton onSitePaymentRadioButton = findViewById(R.id.onSitePayment);

        // Set the default selected radio button to gcashPayment
        onSitePaymentRadioButton.setChecked(true);

        // Set a listener to detect changes in the selected radio button
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Check which radio button was clicked
                if (checkedId == R.id.gcashPayment) {
                    // gcashPayment is selected, set answer to true
                    answer = true;
                } else if (checkedId == R.id.onSitePayment) {
                    // onSitePayment is selected, set answer to false
                    answer = false;
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                // Pass data as extras in the intent
                Intent intent = getIntent();
                String historyUid = intent.getStringExtra("historyUid");
                String carpostUID = intent.getStringExtra("CarpostUID");
                String ApprovedId = intent.getStringExtra("ApprovedId");
                String carOwnerId = intent.getStringExtra("CarOwnerId");


                Intent intents = new Intent(PaymentAndSignature.this, DownloadDocumentContract.class);
                intents.putExtra("historyUid", historyUid);
                intents.putExtra("CarpostUID", carpostUID);
                intents.putExtra("ApprovedId", ApprovedId);
                intents.putExtra("CarOwnerId", carOwnerId);
                intents.putExtra("PaymentMethod", answer);
                startActivity(intents);
            }
        });
    }
}