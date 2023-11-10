package com.example.driveassure;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

public class ImageSliderAdapter extends PagerAdapter {
    private Context context;
    private List<String> imageUrls;

    public ImageSliderAdapter(Context context) {
        this.context = context;
        this.imageUrls = new ArrayList<>();
    }

    public void addImageUrl(String imageUrl) {
        imageUrls.add(imageUrl);
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        FrameLayout frameLayout = new FrameLayout(context);

        // Create a progress bar
        ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleSmall);
        progressBar.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.Gravity.CENTER));

        frameLayout.addView(progressBar);

        // Create an image view
        ImageView imageView = new ImageView(context);
        imageView.setVisibility(View.INVISIBLE); // Initially hide the image view

        // Load image with Glide
        Glide.with(context)
                .load(imageUrls.get(position))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(
                            @Nullable GlideException e,
                            Object model,
                            Target<Drawable> target,
                            boolean isFirstResource
                    ) {
                        progressBar.setVisibility(View.GONE); // Hide progress bar on failure
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(
                            Drawable resource,
                            Object model,
                            Target<Drawable> target,
                            DataSource dataSource,
                            boolean isFirstResource
                    ) {
                        progressBar.setVisibility(View.GONE); // Hide progress bar on success
                        imageView.setVisibility(View.VISIBLE); // Show the image view
                        return false;
                    }
                })
                .into(imageView);

        frameLayout.addView(imageView);

        container.addView(frameLayout);

        return frameLayout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}