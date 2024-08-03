package com.example.filmkutuphanesi.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.filmkutuphanesi.dao.FavouritesDAO;
import com.example.filmkutuphanesi.dao.UserDAO;
import com.example.filmkutuphanesi.model.Favourites;
import com.example.filmkutuphanesi.model.User;

@Database(entities = {User.class, Favourites.class}, version = 2)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract UserDAO getUserDAO();
    public abstract FavouritesDAO getFavouritesDAO();

}
