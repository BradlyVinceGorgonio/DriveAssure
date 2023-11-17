package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PaymentAndSignature extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_and_signature);

        Button button = findViewById(R.id.continueBtn);
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
                startActivity(intents);
            }
        });
    }
}