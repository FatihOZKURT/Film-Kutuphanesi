package com.example.filmkutuphanesi.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.filmkutuphanesi.R;

public class SplashScreen extends AppCompatActivity {

    boolean nightMODE;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);


        sharedPreferences=getSharedPreferences("MODE", MODE_PRIVATE);
        nightMODE= sharedPreferences.getBoolean("night",false);
        if(nightMODE){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }



        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedPreferences=getSharedPreferences("LoggedUser", MODE_PRIVATE);
                int userID = sharedPreferences.getInt("userID",0);
                if (sharedPreferences.contains("userID") && userID != 0){
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    intent.putExtra("userID",userID);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);


    }
}