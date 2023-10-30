package com.example.driveassure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Select_Vehicle_Choice extends AppCompatActivity {

    CardView fourWheel;
    CardView motor;
    Button fourButton;
    Button motorButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_vehicle_choice);


        fourWheel = findViewById(R.id.cardView);
        motor = findViewById(R.id.cardView6);
        fourButton = findViewById(R.id.fourButton);
        motorButton = findViewById(R.id.motorButton);

        fourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Select_Vehicle_Choice.this, terms_and_agreement.class);

                // Add the string as an extra
                String message = "Car";
                intent.putExtra("EXTRA_MESSAGE", message);
                Log.d("POWERPOWER", "onClick: Selection muna" + message);
                // Start the receiver activity
                startActivity(intent);
            }
        });


        motorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Select_Vehicle_Choice.this, terms_and_agreement.class);

                // Add the string as an extra
                String message = "Motorcycle";
                intent.putExtra("EXTRA_MESSAGE", message);
                Log.d("POWERPOWER", "onClick: Selection muna " + message);

                // Start the receiver activity
                startActivity(intent);
            }
        });


    }
}