package com.example.filmkutuphanesi.navigation;



import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.SharedPreferences;
import com.example.filmkutuphanesi.FavouritesActivity;
import com.example.filmkutuphanesi.LoginActivity;
import com.example.filmkutuphanesi.MainActivity;
import com.example.filmkutuphanesi.R;
import com.example.filmkutuphanesi.SettingsActivity;
import com.google.android.material.navigation.NavigationView;



public class Navigation implements NavigationView.OnNavigationItemSelectedListener {
    public NavigationView navigationView;
    public DrawerLayout drawerLayout;
    public int userID;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public Navigation (Context context, int userID, NavigationView navigationView, DrawerLayout drawerLayout){
        this.context = context;
        this.userID = userID;
        this.navigationView = navigationView;
        this.drawerLayout = drawerLayout;
    }

    public void navigate(){
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        sharedPreferences = context.getSharedPreferences("LoggedUser", Context.MODE_PRIVATE);
        if (menuItem.getItemId() == R.id.nav_home){
            if (context instanceof MainActivity) {
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }else {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("userID", userID);
                context.startActivity(intent);
            }
        } else if (menuItem.getItemId() == R.id.nav_favourites) {
            if (context instanceof FavouritesActivity) {
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }else {
                Intent intent = new Intent(context, FavouritesActivity.class);
                intent.putExtra("userID", userID);
                context.startActivity(intent);
            }
        }else if (menuItem.getItemId() == R.id.nav_settings){
            if (context instanceof SettingsActivity) {
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }else {
                Intent intent = new Intent(context, SettingsActivity.class);
                intent.putExtra("userID", userID);
                context.startActivity(intent);
            }
        }else if (menuItem.getItemId() == R.id.nav_logout){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Çıkış Yap");
            builder.setMessage("Çıkış yapmak istediğinize emin misiniz?");
            builder.setIcon(R.drawable.exit_icon);
            builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editor=sharedPreferences.edit();
                    editor.putInt("userID",0);
                    editor.apply();
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
        return true;
    }
}
