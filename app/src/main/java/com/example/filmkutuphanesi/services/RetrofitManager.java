package com.example.filmkutuphanesi.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.filmkutuphanesi.util.Constants;

public class RetrofitManager {
    private static Retrofit retrofit;
    private static final String BASE_URL = Constants.BASE_URL;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
