package com.example.driveassure;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

public class acceptedreceivedbyrenter extends Dialog {
    public acceptedreceivedbyrenter(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acceptedreceivedbyrenterpopup);

        // Customize your dialog here

        Button btnClose = findViewById(R.id.OKayButton);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss(); // Close the dialog when the close button is clicked

            }
        });
    }
}
