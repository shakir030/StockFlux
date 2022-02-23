package com.example.stockflux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavAction;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.location.SettingInjectorService;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.ScrollCaptureTarget;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView register_text = findViewById(R.id.login_user);
        register_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(MainActivity.this,Login.class);
                startActivity(register);
            }
        });
        Button Reports_Button = findViewById(R.id.Reports_button);
        Reports_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent report = new Intent(MainActivity.this,reports.class);
                startActivity(report);
            }
        });



        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    // override the onOptionsItemSelected()
    // function to implement
    // the item click listener callback
    // to open and close the navigation
    // drawer when the icon is clicked
    @Override

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            /*switch(item.getItemId()){
                case R.id.nav_settings:{
                    Intent i = new Intent(MainActivity.this,setting.class);
                    startActivity(i);
                    break;
                }
                case R.id.nav_sales:{
                    Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
                    break;
                }
            }*/
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}