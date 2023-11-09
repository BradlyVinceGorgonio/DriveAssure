package com.example.driveassure;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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
        ImageView DisplayImage;  // Add this line

        public HomeListingCarViewHolder(View itemView) {
            super(itemView);
            carListingName = itemView.findViewById(R.id.carNameDisplay);
            carPrice = itemView.findViewById(R.id.carPriceDisplay);
            carLocation = itemView.findViewById(R.id.carLocationDisplay);
            carTransmission = itemView.findViewById(R.id.carTransmissionDisplay);
            cardView = itemView.findViewById(R.id.ownerListingsItems);
            DisplayImage = itemView.findViewById(R.id.carImageDisplay);
        }
    }



}
