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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;


public class profileUserFragment extends Fragment {
    CardView carRentingCardView;
    ImageView favoritesImage;
    CircleImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_user, container, false);

        imageView = view.findViewById(R.id.imageView);
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


        Button profile = view.findViewById(R.id.profileBtn);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Profile.class);
                startActivity(intent);
            }
        });

        Button payment = view.findViewById(R.id.paymentBtn);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Payments.class);
                startActivity(intent);
            }
        });

        Button rentalhistory = view.findViewById(R.id.rentalHistoryBtn);
        rentalhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RentalHistory.class);
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

        LoadProfileImage();


        return view;
    }

    private void LoadProfileImage()
    {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("users/" + userId + "/face.jpg");

        final String[] tempProfilePictureUrl = {"face.jpg"};  // Declare a final temporary variable

        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            tempProfilePictureUrl[0] = uri.toString();  // Assign the value to the temporary variable
            String storageUrl = uri.toString();
            // Use Glide to load the image into the ImageView
            Glide.with(this)
                    .load(storageUrl)
                    .placeholder(R.drawable.personvector)  // Placeholder image while loading (optional)
                    .error(R.drawable.personvector)       // Error image if the loading fails (optional)
                    .into(imageView);

        }).addOnFailureListener(e -> {
            // Handle any errors that occur while fetching the profile picture URL
        });

    }
}