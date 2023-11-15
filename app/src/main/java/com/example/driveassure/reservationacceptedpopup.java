package com.example.driveassure;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

public class reservationacceptedpopup extends Dialog {

    public reservationacceptedpopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservationacceptedpopup);

        // Customize your dialog here

        Button btnClose = findViewById(R.id.OKayButton);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), userHome.class);
                getContext().startActivity(intent);
                dismiss(); // Close the dialog when the close button is clicked

            }
        });
    }
}
