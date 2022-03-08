package com.example.stockflux;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    private Button logout_button,Reports_Button,purchase_button,sales_button;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        purchase_button = findViewById(R.id.Purchase_button);
        purchase_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent purchase = new Intent(MainActivity.this,purchase_menu.class);
                startActivity(purchase);
            }
        });

        sales_button = findViewById(R.id.Sales_button);
        sales_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,activity_sales_home.class));
            }
        });


        Reports_Button = findViewById(R.id.Reports_button);
        Reports_Button.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View view) {
                Intent report = new Intent(MainActivity.this,reports.class);
                startActivity(report);
            }
        });

        logout_button = findViewById(R.id.logout_button);
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,Login.class));
            }
        });
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("user");
        user_id = user.getUid();
        final TextView user_name = findViewById(R.id.login_user);
        final TextView business_name = findViewById(R.id.name_of_organisation);
        reference.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user user_profile = snapshot.getValue(user.class);
                if(user_profile != null){
                    String user_profile_name = user_profile.fullname;
                    String user_profile_business_name = user_profile.busssinessname;
                    user_name.setText(user_profile_name);
                    business_name.setText(user_profile_business_name);
                    
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Something wrong happen please try again", Toast.LENGTH_LONG).show();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}