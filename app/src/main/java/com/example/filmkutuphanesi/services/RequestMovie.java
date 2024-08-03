package com.example.filmkutuphanesi.services;



import com.example.filmkutuphanesi.model.Movie;
import com.example.filmkutuphanesi.model.SearchedMovies;
import com.example.filmkutuphanesi.model.TrendMovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RequestMovie {
    @GET("movie/{movie_id}?language=tr-TR")
    Call<Movie> getMovie(@Path("movie_id") int movie_id, @Query("api_key") String api_key);

    @GET("search/movie?language=tr-TR")
    Call<SearchedMovies> searchMovie(@Query("query") String keyword, @Query("api_key") String api_key);

    @GET("trending/movie/day?language=tr-TR")
    Call<TrendMovies> trendMovies(@Query("api_key") String api_key);


}