package com.example.driveassure;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
public class create_new_listing extends Fragment {
    CardView fourWheel;
    CardView motor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_create_new_listing, container, false);

        fourWheel = view.findViewById(R.id.cardView);
        motor = view.findViewById(R.id.cardView6);

        fourWheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), terms_and_agreement.class);

                // Add the string as an extra
                String message = "Car";
                intent.putExtra("EXTRA_MESSAGE", message);

                // Start the receiver activity
                startActivity(intent);
            }
        });

        motor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), terms_and_agreement.class);

                // Add the string as an extra
                String message = "Motorcycle";
                intent.putExtra("EXTRA_MESSAGE", message);

                // Start the receiver activity
                startActivity(intent);
            }
        });
        return view;
    }
}