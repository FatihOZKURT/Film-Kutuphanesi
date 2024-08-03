package com.example.filmkutuphanesi.model;

public class FavouritesMovie {
    private String title;
    private int movie_id;
    private double vote_average;
    private String poster_path;

    public FavouritesMovie(String title, int movie_id, double vote_average, String poster_path ) {
        this.title = title;
        this.movie_id = movie_id;
        this.vote_average = vote_average;
        this.poster_path = poster_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }
}
