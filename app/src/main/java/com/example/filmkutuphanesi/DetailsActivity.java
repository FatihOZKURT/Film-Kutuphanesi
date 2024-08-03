package com.example.filmkutuphanesi;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.filmkutuphanesi.database.MovieDatabase;
import com.example.filmkutuphanesi.database.MovieDatabaseSingleton;
import com.example.filmkutuphanesi.model.Favourites;
import com.example.filmkutuphanesi.model.Movie;

import com.example.filmkutuphanesi.model.User;
import com.example.filmkutuphanesi.navigation.Navigation;
import com.example.filmkutuphanesi.services.RequestMovie;
import com.example.filmkutuphanesi.services.RetrofitManager;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailsActivity extends AppCompatActivity {
    MovieDatabase MovieDB;
    Navigation Navigation;
    List<Movie.Genre> categories;
    private int userID,movieID;
    private TextView titleTextView,taglineTextView,overviewTextView,username,genres,relaseDate,voteAverage;
    private ImageView posterImageView;
    private ImageButton favButton;
    boolean isFavorite = false;
    private View toolbar,headerView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private static final String API_KEY = "e6cc416f3552217bd3cf53ad88770012";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details);

        Intent intent3 = getIntent();
        movieID = intent3.getIntExtra("movie_id", 0);
        userID = intent3.getIntExtra("user_id", 0);

        initComponents();
        setSupportActionBar((Toolbar) toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(DetailsActivity.this,drawerLayout,(Toolbar) toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Navigation = new Navigation(DetailsActivity.this, userID, navigationView, drawerLayout);
        Navigation.navigate();

        MovieDB = MovieDatabaseSingleton.getInstance(getApplicationContext());
        getUserName();
        checkFavouriteInBackground();

        Retrofit retrofit = RetrofitManager.getRetrofitInstance();


        RequestMovie requestMovie = retrofit.create(RequestMovie.class);
        requestMovie.getMovie(movieID,API_KEY).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                Movie movie = response.body();
                assert movie != null;

                titleTextView.setText(movie.getTitle());
                taglineTextView.setText(movie.getTagline());
                overviewTextView.setText(movie.getOverview());

                String dateStr = movie.getRelease_date();

                if (dateStr != null) {
                    LocalDate date = LocalDate.parse(dateStr);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", new Locale("tr"));
                    String formattedDate = date.format(formatter);
                    relaseDate.setText(formattedDate);
                } else {
                    relaseDate.setText(movie.getRelease_date());
                }


                double average = movie.getVoteAverage();
                DecimalFormat df = new DecimalFormat("#.#");
                String formattedValue = df.format(average);
                voteAverage.setText(formattedValue);

                categories = movie.getGenres();
                StringJoiner joiner = new StringJoiner(", ");
                for (Movie.Genre genre : categories) {
                    joiner.add(genre.getName());
                }
                String result = joiner.toString();
                genres.setText(result);

                String posterUrl = movie.getBackdrop_path();
                if (posterUrl != null && !posterUrl.isEmpty()) {
                    Picasso.get().load("https://image.tmdb.org/t/p/w500" + posterUrl).into(posterImageView);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                titleTextView.setText(t.getMessage());
            }
        });


        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite) {
                    Favourites favourite = new Favourites(userID, movieID);
                    removeFavouriteInBackground(favourite);
                } else {
                    Favourites favourite = new Favourites(userID, movieID);
                    addFavouriteInBackground(favourite);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void initComponents() {
        titleTextView = findViewById(R.id.titleTextView);
        taglineTextView = findViewById(R.id.taglineTextView);
        overviewTextView = findViewById(R.id.overviewTextView);
        posterImageView = findViewById(R.id.posterImageView);
        favButton = findViewById(R.id.favButton);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        headerView = navigationView.getHeaderView(0);
        username = headerView.findViewById(R.id.username);
        genres = findViewById(R.id.genres);
        relaseDate = findViewById(R.id.relaseDate);
        voteAverage = findViewById(R.id.voteAverage);
    }

    public void addFavouriteInBackground(Favourites favourite){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler (Looper.getMainLooper());
        executorService. execute(new Runnable() {
            @Override
            public void run() {
                MovieDB.getFavouritesDAO().insertFavourites(favourite);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DetailsActivity.this, "Film Favorilere Eklendi", Toast.LENGTH_SHORT).show();
                        favButton.setImageResource(R.drawable.baseline_star_35);
                        isFavorite = true;
                    }
                });


            }
        });
    }

    public void removeFavouriteInBackground(Favourites favourite){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler (Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                MovieDB.getFavouritesDAO().deleteFavourites(favourite);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DetailsActivity.this, "Film Favorilerden Çıkarıldı", Toast.LENGTH_SHORT).show();
                            favButton.setImageResource(R.drawable.baseline_star_border_24);
                            isFavorite = false;
                        }
                    });

            }
        });
    }

    public void checkFavouriteInBackground(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler (Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Favourites existingFavourite = MovieDB.getFavouritesDAO().findFavourite(userID, movieID);
                if (existingFavourite != null) {
                    isFavorite = true;
                    favButton.setImageResource(R.drawable.baseline_star_35);
                }
            }
        });
    }

    public void getUserName(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler (Looper.getMainLooper());
        executorService. execute(new Runnable() {
            @Override
            public void run() {

                User user  = MovieDB.getUserDAO().getPerson(userID);
                username.setText(user.getUsername());


                handler.post(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        username.setText(user.getUsername());
                    }
                });
            }
        });
    }
}