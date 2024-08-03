package com.example.filmkutuphanesi.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.filmkutuphanesi.model.Favourites;

import java.util.List;

@Dao
public interface FavouritesDAO {
    @Insert
    void insertFavourites(Favourites favourites);

    @Update
    void updateFavourites(Favourites favourites);

    @Delete
    void deleteFavourites(Favourites favourites);

    @Query("SELECT * FROM favourites WHERE user_id =:userId and movie_id =:movieId")
    Favourites findFavourite(int userId, int movieId);


    @Query("SELECT * FROM favourites WHERE user_id ==:user_id")
    public List<Favourites> getFavouriteByUser(int user_id);


}
