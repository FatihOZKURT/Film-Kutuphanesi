package com.example.filmkutuphanesi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmkutuphanesi.activities.DetailsActivity;
import com.example.filmkutuphanesi.R;
import com.example.filmkutuphanesi.model.FavouritesMovie;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<MovieItemViewHolder> {
    public Context context;
    public List<FavouritesMovie> moviesList;
    int userID;

    public FavouritesAdapter(Context context, List<FavouritesMovie> moviesList,int userID) {
        this.context = context;
        this.userID=userID;
        this.moviesList = moviesList;
    }


    @SuppressLint("NotifyDataSetChanged")
    public void clear() {
        moviesList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieItemViewHolder holder, int position) {
        FavouritesMovie movie = moviesList.get(position);
        holder.movieNameTextView.setText(movie.getTitle());

        double average = movie.getVote_average();
        DecimalFormat df = new DecimalFormat("#.#");
        String formattedValue = df.format(average);
        holder.rateTextView.setText(formattedValue);

        String posterUrl = movie.getPoster_path();
        if (posterUrl != null && !posterUrl.isEmpty()) {
            Picasso.get().load("https://image.tmdb.org/t/p/w500" + posterUrl).into(holder.movieImageView);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(context, DetailsActivity.class);
                intent3.putExtra("movie_id", movie.getMovie_id());
                intent3.putExtra("user_id", userID);
                context.startActivity(intent3);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
