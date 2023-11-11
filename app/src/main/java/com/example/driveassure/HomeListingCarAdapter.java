package com.example.driveassure;

import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_STATIC_DP;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.List;


public class HomeListingCarAdapter extends RecyclerView.Adapter<HomeListingCarAdapter.HomeListingCarViewHolder>
{
    private Context context;
    private List<HomeListingClass> HistoryList;
    private HomeListingCarAdapter.OnItemClickListener onItemClickListener; // Define the listener interface

    public interface OnItemClickListener {
        void onItemClick(HomeListingClass history);
    }

    public HomeListingCarAdapter(Context context, List<HomeListingClass> HistoryList, HomeListingCarAdapter.OnItemClickListener onItemClickListener) {
        this.context = context;
        this.HistoryList = HistoryList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public HomeListingCarAdapter.HomeListingCarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.homepostsitems, parent, false);
        return new HomeListingCarAdapter.HomeListingCarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeListingCarAdapter.HomeListingCarViewHolder holder, int position) {
        HomeListingClass admin = HistoryList.get(position);
        holder.carListingName.setText(admin.getName());
        holder.carPrice.setText(admin.getCarPrice());
        holder.carLocation.setText(admin.getCarLocation());
        holder.carTransmission.setText(admin.getCarTransmission());

        PushDownAnim.setPushDownAnimTo(holder.cardView,holder.heartToggleButton).setScale(MODE_STATIC_DP, 8 );

        Log.d("BITCHNIGGA", "INSIDE OF ADAPTER " + admin.getProfilePictureUrl());
        // Load the profile picture using Glide
        Glide.with(context)
                .load(admin.getProfilePictureUrl())  // Use the appropriate method to get the profile picture URL
                .placeholder(R.drawable.carvector)  // Placeholder image while loading
                .error(R.drawable.carvector)  // Error image if loading fails
                .into(holder.DisplayImage);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(admin);
                }
            }
        });

        // Set click listener for the small image button
        // Set click listener for the ToggleButton
        holder.heartToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle ToggleButton state change action
                if (isChecked) {
                    // The ToggleButton is checked
                    Toast.makeText(buttonView.getContext(), "Checked" + admin.getCarpostUID(), Toast.LENGTH_SHORT).show();
                    // Initialize Firebase components

                    addWordToVehicleLikes(admin.getCarpostUID());



                } else {
                    // The ToggleButton is unchecked
                    Toast.makeText(buttonView.getContext(), "Unchecked", Toast.LENGTH_SHORT).show();
                    removeWordFromVehicleLikes(admin.getCarpostUID());
                }

                // Prevent the check change event from being propagated to the CardView
                buttonView.getParent().requestDisallowInterceptTouchEvent(true);
            }
        });
    }
    public void addWordToVehicleLikes(String wordToAdd) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // Get the current user's UID
            String uid = auth.getCurrentUser().getUid();

            // Reference to the "users" collection and the document with the user's UID
            DocumentReference userDocRef = db.collection("users").document(uid);

            // Update the "vehicleLikes" array with the new word
            userDocRef.update("vehicle likes", FieldValue.arrayUnion(wordToAdd))
                    .addOnSuccessListener(aVoid -> {
                        // Update successful
                        // Handle success if needed
                    })
                    .addOnFailureListener(e -> {
                        // Handle error
                        // This might occur if the document doesn't exist or if there is a Firebase-related issue
                    });
        }
    }
    public void removeWordFromVehicleLikes(String wordToRemove) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // Get the current user's UID
            String uid = auth.getCurrentUser().getUid();

            // Reference to the "users" collection and the document with the user's UID
            DocumentReference userDocRef = db.collection("users").document(uid);

            // Update the "vehicleLikes" array by removing the specified word
            userDocRef.update("vehicle likes", FieldValue.arrayRemove(wordToRemove))
                    .addOnSuccessListener(aVoid -> {
                        // Update successful
                        // Handle success if needed
                    })
                    .addOnFailureListener(e -> {
                        // Handle error
                        // This might occur if the document doesn't exist or if there is a Firebase-related issue
                    });
        }
    }

    @Override
    public int getItemCount() {
        return HistoryList.size();
    }

    public static class HomeListingCarViewHolder extends RecyclerView.ViewHolder {
        TextView carListingName;
        TextView carPrice;
        TextView carLocation;
        TextView carTransmission;
        CardView cardView;
        ImageView DisplayImage;
        ToggleButton heartToggleButton;

        public HomeListingCarViewHolder(View itemView) {
            super(itemView);
            carListingName = itemView.findViewById(R.id.carNameDisplay);
            carPrice = itemView.findViewById(R.id.carPriceDisplay);
            carLocation = itemView.findViewById(R.id.carLocationDisplay);
            carTransmission = itemView.findViewById(R.id.carTransmissionDisplay);
            cardView = itemView.findViewById(R.id.ownerListingsItems);
            DisplayImage = itemView.findViewById(R.id.carImageDisplay);
            heartToggleButton = itemView.findViewById(R.id.heartToggleButton);
        }
    }



}
