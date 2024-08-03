package com.example.filmkutuphanesi;

import android.annotation.SuppressLint;
import androidx.activity.OnBackPressedCallback;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.filmkutuphanesi.adapter.MoviesAdapter;
import com.example.filmkutuphanesi.adapter.TrendsAdapter;
import com.example.filmkutuphanesi.database.MovieDatabase;
import com.example.filmkutuphanesi.database.MovieDatabaseSingleton;
import com.example.filmkutuphanesi.model.SearchedMovies;
import com.example.filmkutuphanesi.model.TrendMovies;
import com.example.filmkutuphanesi.model.User;
import com.example.filmkutuphanesi.navigation.Navigation;
import com.example.filmkutuphanesi.services.RequestMovie;
import com.example.filmkutuphanesi.services.RetrofitManager;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    MovieDatabase MovieDB;
    Navigation Navigation;
    private RecyclerView recyclerView;
    private MoviesAdapter moviesAdapter;
    private TrendsAdapter trensAdapter;
    private SearchView searchView;
    private TextView trendsText,username;
    private View toolbar,headerView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private int userID;

    private static final String API_KEY = "e6cc416f3552217bd3cf53ad88770012";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        Intent intent2 = getIntent();
        userID = intent2.getIntExtra("userID", 0);

        initComponents();

        setSupportActionBar((Toolbar) toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,(Toolbar) toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Navigation = new Navigation(MainActivity.this, userID, navigationView, drawerLayout);
        Navigation.navigate();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MovieDB =  MovieDatabaseSingleton.getInstance(MainActivity.this);
        getUserName();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });

        Retrofit retrofit = RetrofitManager.getRetrofitInstance();
        getTrendMovies(retrofit,userID);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                RequestMovie requestMovie = retrofit.create(RequestMovie.class);
                requestMovie.searchMovie(query, API_KEY).enqueue(new Callback<SearchedMovies>() {

                    @Override
                    public void onResponse(Call<SearchedMovies> call, Response<SearchedMovies> response) {
                        trendsText.setText("");
                        List<SearchedMovies.Results> searchedMoviesList = response.body().getResults();

                        moviesAdapter = new MoviesAdapter(MainActivity.this, searchedMoviesList, userID);
                        recyclerView.setAdapter(moviesAdapter);
                    }

                    @Override
                    public void onFailure(Call<SearchedMovies> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                RequestMovie requestMovie = retrofit.create(RequestMovie.class);
                requestMovie.searchMovie(newText, API_KEY).enqueue(new Callback<SearchedMovies>() {

                    @Override
                    public void onResponse(Call<SearchedMovies> call, Response<SearchedMovies> response) {
                        trendsText.setText("");

                        List<SearchedMovies.Results> searchedMoviesList = response.body().getResults();

                        moviesAdapter = new MoviesAdapter(MainActivity.this, searchedMoviesList, userID);
                        recyclerView.setAdapter(moviesAdapter);
                    }

                    @Override
                    public void onFailure(Call<SearchedMovies> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getTrendMovies(retrofit, userID);
                return false;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START);
    }

    @SuppressLint({"WrongViewCast", "InflateParams"})
    private void initComponents() {
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        trendsText = findViewById(R.id.trendsText);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        username = findViewById(R.id.username);
        headerView = navigationView.getHeaderView(0);
        username = headerView.findViewById(R.id.username);
    }

    private void getTrendMovies(Retrofit retrofit, int userID){
        RequestMovie requestMovie = retrofit.create(RequestMovie.class);
        requestMovie.trendMovies(API_KEY).enqueue(new Callback<TrendMovies>() {
            @Override
            public void onResponse(Call<TrendMovies> call, Response<TrendMovies> response) {
                trendsText.setText("Trend Filmler");
                List<TrendMovies.Results> trendMovieList = response.body().getResults();

                trensAdapter = new TrendsAdapter(MainActivity.this,trendMovieList,userID);
                recyclerView.setAdapter(trensAdapter);
            }

            @Override
            public void onFailure(Call<TrendMovies> call, Throwable t) {

            }
        });
    }

    public void getUserName(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler (Looper.getMainLooper());
        executorService. execute(new Runnable() {
            @Override
            public void run() {
                username.setVisibility(View.VISIBLE);
                User user  = MovieDB.getUserDAO().getPerson(userID);
                username.setText(user.getUsername());

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        username.setText(user.getUsername());
                    }
                });
            }
        });
    }

}