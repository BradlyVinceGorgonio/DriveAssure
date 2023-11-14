package com.example.driveassure;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;


public class profileUserFragment extends Fragment {
    CardView carRentingCardView;
    ImageView favoritesImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_user, container, false);

        ImageView logout = view.findViewById(R.id.logoutBtn);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        Button Useragreement = view.findViewById(R.id.userAgreementBtn);
        Useragreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), User_Agreement.class);
                startActivity(intent);
            }
        });

        Button VehicleDocuments = view.findViewById(R.id.vehicleDocumentBtn);

        VehicleDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VehicleDocuments.class);
                startActivity(intent);
            }
        });

        favoritesImage = view.findViewById(R.id.favoritesImage);
        favoritesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BookmarkArea.class);
                startActivity(intent);
            }
        });

        carRentingCardView = view.findViewById(R.id.carRentingCardView);
        carRentingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CarInquiriesAcceptReject.class);
                startActivity(intent);
            }
        });

        return view;
    }
}