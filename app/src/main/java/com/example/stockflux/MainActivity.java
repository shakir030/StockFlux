package com.example.stockflux;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity{
    //public DrawerLayout drawerLayout;
    Button logout_button, Reports_Button, purchase_button, sales_button,verify_button;
    //public ActionBarDrawerToggle actionBarDrawerToggle;
    TextView Business_name,full_name,warning_verify;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String user_id = fAuth.getCurrentUser().getUid();
    FirebaseFirestore fGetUser = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Business_name = findViewById(R.id.name_of_organisation);
        full_name = findViewById(R.id.login_user);
        purchase_button = findViewById(R.id.Purchase_button);
        sales_button = findViewById(R.id.Sales_button);
        Reports_Button = findViewById(R.id.Reports_button);


        Query qurey = fGetUser.collection("Users").document(user_id).collection("UsersData");
        qurey.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    Toast.makeText(MainActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                } else {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        user user_data = documentSnapshot.toObject(user.class);
                        String business_name = user_data.getBusinessname();
                        String name = user_data.getFullname();
                        Business_name.setText(business_name);
                        full_name.setText(name);
                    }
                }
            }
        });

        verify_button = findViewById(R.id.verify_button);
        warning_verify = findViewById(R.id.warning_verify);

        FirebaseUser user = fAuth.getCurrentUser();
        if(user.isEmailVerified()) {
            warning_verify.setVisibility(View.GONE);
            verify_button.setVisibility(View.GONE);
            purchase_button.setVisibility(View.VISIBLE);
            sales_button.setVisibility(View.VISIBLE);
            Reports_Button.setVisibility(View.VISIBLE);
            Business_name.setVisibility(View.VISIBLE);
            full_name.setVisibility(View.VISIBLE);
        } else{
            warning_verify.setVisibility(View.VISIBLE);
            verify_button.setVisibility(View.VISIBLE);
            Business_name.setVisibility(View.GONE);
            purchase_button.setVisibility(View.GONE);
            sales_button.setVisibility(View.GONE);
            Reports_Button.setVisibility(View.GONE);
            full_name.setVisibility(View.GONE);
            warning_verify.setText("Please Verify your Account");
            warning_verify.setTextColor(Color.RED);
            verify_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(MainActivity.this, "E-Mail Verification has been sent to your E-Mail", Toast.LENGTH_LONG).show();
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(MainActivity.this, Login.class));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TAG", "onFailure: Email not sent"+e.getMessage());

                        }
                    });
                }
            });
        }


        full_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,user_account.class));
            }
        });


        purchase_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent purchase = new Intent(MainActivity.this, purchase_menu.class);
                startActivity(purchase);
            }
        });


        sales_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, activity_sales_home.class));
            }
        });


        Reports_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent report = new Intent(MainActivity.this, reports.class);
                startActivity(report);
            }
        });

        logout_button = findViewById(R.id.logout_button);
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });



        /*user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("user");
        user_id = user.getUid();
        final TextView user_name = findViewById(R.id.login_user);
        final TextView business_name = findViewById(R.id.name_of_organisation);
        reference.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user user_profile = snapshot.getValue(user.class);
                if (user_profile != null) {
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
        });*/


        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        /*drawerLayout = findViewById(R.id.my_drawer_layout);
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
    }*/
    }
}
