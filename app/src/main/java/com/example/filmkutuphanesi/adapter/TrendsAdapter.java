package com.example.filmkutuphanesi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmkutuphanesi.DetailsActivity;
import com.example.filmkutuphanesi.R;
import com.example.filmkutuphanesi.model.SearchedMovies;
import com.example.filmkutuphanesi.model.TrendMovies;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class TrendsAdapter extends RecyclerView.Adapter<MovieItemViewHolder> {
    public Context context;
    public List<TrendMovies.Results> moviesList;
    int userID;

    public TrendsAdapter(Context context, List<TrendMovies.Results> moviesList,int userID) {
        this.context = context;
        this.userID=userID;
        this.moviesList = moviesList;
    }

    @NonNull
    @Override
    public MovieItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieItemViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MovieItemViewHolder holder, int position) {
        TrendMovies.Results movie = moviesList.get(position);
        holder.movieNameTextView.setText(movie.getTitle());

        float average = movie.getVote_average();
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
                intent3.putExtra("movie_id", movie.getId());
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
