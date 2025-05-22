package com.example.filmkutuphanesi.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.filmkutuphanesi.R;
import com.example.filmkutuphanesi.adapter.FavouritesAdapter;
import com.example.filmkutuphanesi.database.MovieDatabase;
import com.example.filmkutuphanesi.database.MovieDatabaseSingleton;
import com.example.filmkutuphanesi.model.Favourites;
import com.example.filmkutuphanesi.model.FavouritesMovie;
import com.example.filmkutuphanesi.model.Movie;
import com.example.filmkutuphanesi.model.User;
import com.example.filmkutuphanesi.navigation.Navigation;
import com.example.filmkutuphanesi.services.RequestMovie;
import com.example.filmkutuphanesi.services.RetrofitManager;
import com.example.filmkutuphanesi.util.Constants;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FavouritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView2;
    MovieDatabase MovieDB;
    Navigation Navigation;
    List<Favourites> favourites;
    List<FavouritesMovie> movies = new ArrayList<>();
    Retrofit retrofit;
    private FavouritesAdapter moviesAdapter;
    private View toolbar,headerView;
    private TextView username;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private int userID;
    private static final String API_KEY = Constants.API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favourites);

        Intent intent2 = getIntent();
        userID = intent2.getIntExtra("userID", 0);
        initComponents();

        setSupportActionBar((Toolbar) toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(FavouritesActivity.this,drawerLayout,(Toolbar) toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Navigation = new Navigation(FavouritesActivity.this, userID, navigationView, drawerLayout);
        Navigation.navigate();

        recyclerView2.setLayoutManager(new LinearLayoutManager(this));


        MovieDB =  MovieDatabaseSingleton.getInstance(FavouritesActivity.this);
        getUserName();

        retrofit = RetrofitManager.getRetrofitInstance();

        if(movies == null) getUserListInBackground();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else {
                    finish();
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (favourites != null ) favourites.clear();
        movies.clear();
        if (moviesAdapter != null) moviesAdapter.clear();
        getUserListInBackground();

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START);
    }


    @SuppressLint("WrongViewCast")
    private void initComponents() {
        recyclerView2 = findViewById(R.id.recyclerView2);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        headerView = navigationView.getHeaderView(0);
        username = headerView.findViewById(R.id.username);

    }


    public void getUserListInBackground(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler (Looper.getMainLooper());
        executorService. execute(new Runnable() {
            @Override
            public void run() {

                favourites = MovieDB.getFavouritesDAO().getFavouriteByUser(userID);
                moviesAdapter = new FavouritesAdapter(FavouritesActivity.this, movies, userID);

                handler.post(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        for(Favourites f : favourites){
                            RequestMovie requestMovie = retrofit.create(RequestMovie.class);
                            requestMovie.getMovie(f.getMovie_id(), API_KEY).enqueue(new Callback<Movie>() {
                                @Override
                                public void onResponse(Call<Movie> call, Response<Movie> response) {
                                    Movie movie = response.body();
                                    assert movie != null;
                                    movies.add(new FavouritesMovie(movie.getTitle(), movie.getId(), movie.getVoteAverage() , movie.getPosterPath()));
                                    recyclerView2.setAdapter(moviesAdapter);
                                }

                                @Override
                                public void onFailure(Call<Movie> call, Throwable t) {
                                    System.out.println(t.getMessage());
                                }
                            });
                        }


                    }
                });
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