package com.example.filmkutuphanesi.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.filmkutuphanesi.model.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);


    @Query("select * from users")
    public List<User> getAllUser () ;

    @Query("select * from users where user_id==:user_id")
    public User getPerson(int user_id);

    @Query("select * from users where password==:password")
    public User checkPassword(String password);

    @Query("SELECT * FROM users where username==:username and password==:password")
    public User getLogin(String username, String password);
}