package com.example.driveassure;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewHolder> {


    private List<SlideItem> slideItem;
    private ViewPager2 viewPager2;


  SlideAdapter(List<SlideItem> slideItem, ViewPager2 viewPager2) {
        this.slideItem = slideItem;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SlideViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_item_container,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
    holder.setImageView(slideItem.get(position));
    if(position == slideItem.size()-2){
        viewPager2.post(runnable);
    }

    }

    @Override
    public int getItemCount() {
        return slideItem.size();
    }

    class SlideViewHolder extends RecyclerView.ViewHolder{

        private RoundedImageView imageView;

      SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
        }
        void setImageView(SlideItem slideItem){
            imageView.setImageResource(slideItem.getImage());
        }
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            slideItem.addAll(slideItem);
            notifyDataSetChanged();
        }
    };
}
