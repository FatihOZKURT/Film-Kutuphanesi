package com.example.filmkutuphanesi.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.filmkutuphanesi.R;
import com.example.filmkutuphanesi.database.MovieDatabase;
import com.example.filmkutuphanesi.database.MovieDatabaseSingleton;
import com.example.filmkutuphanesi.model.User;
import com.example.filmkutuphanesi.navigation.Navigation;
import com.google.android.material.navigation.NavigationView;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UsersettingsActivity extends AppCompatActivity{

    MovieDatabase MovieDB;
    Navigation Navigation;
    User user;
    private View toolbar,headerView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView username;
    private EditText usernameEdit, currentpasswordEdit, passwordEdit, cpasswordEdit ;
    private Button usernameBtn, passwordBtn;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_usersettings);
        Intent intent2 = getIntent();
        userID = intent2.getIntExtra("userID", 0);
        initComponents();
        setSupportActionBar((Toolbar) toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(UsersettingsActivity.this,drawerLayout,(Toolbar) toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Navigation = new Navigation(UsersettingsActivity.this, userID, navigationView, drawerLayout);
        Navigation.navigate();

        MovieDB =  MovieDatabaseSingleton.getInstance(UsersettingsActivity.this);
        getUserName();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });

        usernameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int maxLength = 15;
                int minLength = 5;

                if (s.length() > maxLength) {
                    usernameEdit.setError("Maksimum 15 karakter girebilirsiniz");
                }else if (s.length() < minLength && s.length() != 0){
                    usernameEdit.setError("Kullanıcı adı en az 5 karakter olmalıdır");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        currentpasswordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int maxLength = 15;
                int minLength = 5;

                if (s.length() > maxLength) {
                    currentpasswordEdit.setError("Maksimum 15 karakter girebilirsiniz");
                }else if (s.length() < minLength && s.length() != 0){
                    currentpasswordEdit.setError("Şifre en az 5 karakter olmalıdır");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int maxLength = 15;
                int minLength = 5;

                if (s.length() > maxLength) {
                    passwordEdit.setError("Maksimum 15 karakter girebilirsiniz");
                }else if (s.length() < minLength && s.length() != 0){
                    passwordEdit.setError("Şifre en az 5 karakter olmalıdır");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cpasswordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int maxLength = 15;
                int minLength = 5;

                if (s.length() > maxLength) {
                    cpasswordEdit.setError("Maksimum 15 karakter girebilirsiniz");
                }else if (s.length() < minLength && s.length() != 0){
                    cpasswordEdit.setError("Şifre en az 5 karakter olmalıdır");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        usernameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usernameEdit.getText().toString().length() < 5 || usernameEdit.getText().toString().length() > 15){
                    Toast.makeText(UsersettingsActivity.this, "Kullanıcı adı 5-15 karakter aralığında olmalıdır.", Toast.LENGTH_SHORT).show();
                }else if (user.getUsername().equals(usernameEdit.getText().toString())){
                    Toast.makeText(UsersettingsActivity.this, "Yeni kullanıcı adı eskisiyle aynı olamaz!", Toast.LENGTH_SHORT).show();
                } else{
                    UpdateUsernameInBackground(usernameEdit.getText().toString());
                    getUserName();
                    usernameEdit.clearFocus();
                    usernameEdit.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(usernameEdit.getWindowToken(), 0);
                }
            }
        });

        passwordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordEdit.getText().toString().length() < 5 || passwordEdit.getText().toString().length() > 15){
                    Toast.makeText(UsersettingsActivity.this, "Şifre 5-15 karakter aralığında olmalıdır.", Toast.LENGTH_SHORT).show();
                }else if (!currentpasswordEdit.getText().toString().equals(user.getPassword())){
                    Toast.makeText(UsersettingsActivity.this, "Mevcut şifreniz yanlış!", Toast.LENGTH_SHORT).show();
                }else if (!passwordEdit.getText().toString().equals(cpasswordEdit.getText().toString())) {
                    Toast.makeText(UsersettingsActivity.this, "Şifreler uyuşmuyor!", Toast.LENGTH_SHORT).show();
                }else if (user.getPassword().equals(passwordEdit.getText().toString()) ){
                    Toast.makeText(UsersettingsActivity.this, "Yeni şifreniz eskisiyle aynı olamaz!", Toast.LENGTH_SHORT).show();
                }else{
                    UpdatePasswordInBackground(passwordEdit.getText().toString());

                    currentpasswordEdit.clearFocus();
                    passwordEdit.clearFocus();
                    cpasswordEdit.clearFocus();

                    currentpasswordEdit.setText("");
                    passwordEdit.setText("");
                    cpasswordEdit.setText("");

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(usernameEdit.getWindowToken(), 0);
                }
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
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        username = findViewById(R.id.username);
        headerView = navigationView.getHeaderView(0);
        username = headerView.findViewById(R.id.username);
        usernameEdit = findViewById(R.id.usernameEdit);
        currentpasswordEdit = findViewById(R.id.currentpasswordEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        cpasswordEdit = findViewById(R.id.cpasswordEdit);
        usernameBtn = findViewById(R.id.usernameBtn);
        passwordBtn = findViewById(R.id.passwordBtn);
    }

    public void getUserName(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler (Looper.getMainLooper());
        executorService. execute(new Runnable() {
            @Override
            public void run() {
                username.setVisibility(View.VISIBLE);
                user  = MovieDB.getUserDAO().getPerson(userID);
                if (user != null) username.setText(user.getUsername());

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        username.setText(user.getUsername());
                    }
                });
            }
        });
    }


    public void UpdateUsernameInBackground(String username){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler (Looper.getMainLooper());
        executorService. execute(new Runnable() {
            @Override
            public void run() {

                user.setUsername(username);
                MovieDB.getUserDAO().updateUser(user);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UsersettingsActivity.this, "Kullanıcı Adınız Güncellendi!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void UpdatePasswordInBackground(String password){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler (Looper.getMainLooper());
        executorService. execute(new Runnable() {
            @Override
            public void run() {
                user.setPassword(password);
                MovieDB.getUserDAO().updateUser(user);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UsersettingsActivity.this, "Şifreniz Güncellendi!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}