package com.example.driveassure;

import android.content.Context;
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

public class RequestingRentAdapter extends RecyclerView.Adapter<RequestingRentAdapter.RequestingViewHolder>
{
    private Context context;
    private List<RequestingClass> RequestingList;
    private RequestingRentAdapter.OnItemClickListener onItemClickListener; // Define the listener interface

    public interface OnItemClickListener {
        void onItemClick(RequestingClass trainer);
    }

    public RequestingRentAdapter(Context context, List<RequestingClass> RequestingList, RequestingRentAdapter.OnItemClickListener onItemClickListener) {
        this.context = context;
        this.RequestingList = RequestingList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RequestingRentAdapter.RequestingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rentrequestingitems, parent, false);
        return new RequestingRentAdapter.RequestingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestingRentAdapter.RequestingViewHolder holder, int position) {
        RequestingClass rent = RequestingList.get(position);
        holder.userNameDisplay.setText(rent.getName());
        holder.carTitleDisplay.setText(rent.getCarName());
        holder.daysDisplay.setText("Renting for " + rent.getDaysAmount() + " Days");
        holder.rentingUntilDisplay.setText("Pick up location: "+rent.getRentDateRange());


        // Load the profile picture using Glide
        Glide.with(context)
                .load(rent.getProfilePictureUrl())  // Use the appropriate method to get the profile picture URL
                .placeholder(R.drawable.personvector)  // Placeholder image while loading
                .error(R.drawable.personvector)  // Error image if loading fails
                .into(holder.faceDisplay);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(rent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return RequestingList.size();
    }

    public static class RequestingViewHolder extends RecyclerView.ViewHolder {
        TextView userNameDisplay;
        TextView carTitleDisplay;
        TextView daysDisplay;
        TextView rentingUntilDisplay;
        CardView cardView;

        ImageView faceDisplay;  // Add this line

        public RequestingViewHolder(View itemView) {
            super(itemView);
            userNameDisplay = itemView.findViewById(R.id.userNameDisplay);
            carTitleDisplay = itemView.findViewById(R.id.carTitleDisplay);
            daysDisplay = itemView.findViewById(R.id.daysDisplay);
            rentingUntilDisplay = itemView.findViewById(R.id.rentingUntilDisplay);
            cardView = itemView.findViewById(R.id.rentRequestingItems);
            faceDisplay = itemView.findViewById(R.id.faceDisplay);
        }
    }



}
