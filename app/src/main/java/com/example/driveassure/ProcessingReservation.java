package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProcessingReservation extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing_reservation);


        // Image uploaded successfully
        Dialog dialog = new Dialog(ProcessingReservation.this);
        dialog.setContentView(R.layout.gobackhome);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        // Find the 'YES' button in the dialog layout
        Button yesButton = dialog.findViewById(R.id.OKayButton);
        TextView messageText  = dialog.findViewById(R.id.messageText);
        messageText.setText("Not yet time to start the timer! Rental day begins on [Event Date]. Please wait for the event day to commence the renting period. Thank you!");
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Close the dialog if needed
                Intent newActivityIntent = new Intent(ProcessingReservation.this, userHome.class);
                startActivity(newActivityIntent);
            }
        });
    }
}