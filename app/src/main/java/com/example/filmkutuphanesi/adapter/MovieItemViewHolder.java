package com.example.filmkutuphanesi.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmkutuphanesi.R;

public class MovieItemViewHolder extends RecyclerView.ViewHolder {
    public TextView movieNameTextView;
    public ImageView movieImageView;
    public TextView rateTextView;

    public MovieItemViewHolder(@NonNull View itemView) {
        super(itemView);
        movieNameTextView = itemView.findViewById(R.id.movieNameTextView);
        movieImageView = itemView.findViewById(R.id.movieImageView);
        rateTextView = itemView.findViewById(R.id.rateTextView);

    }
}