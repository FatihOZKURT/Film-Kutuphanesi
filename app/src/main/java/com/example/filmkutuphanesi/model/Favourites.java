package com.example.filmkutuphanesi.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "favourites",
        primaryKeys = {"user_id", "movie_id"},
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "user_id",
                childColumns = "user_id",
                onDelete = CASCADE))
public class Favourites {
    @ColumnInfo(name = "user_id")
    private int user_id;
    @ColumnInfo(name = "movie_id")
    private int movie_id;

    public Favourites(int user_id, int movie_id) {
        this.user_id = user_id;
        this.movie_id = movie_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }
}