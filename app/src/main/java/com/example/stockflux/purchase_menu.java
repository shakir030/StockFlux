package com.example.stockflux;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationView;

public class purchase_menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_menu);
        Button b1 = findViewById(R.id.add_purchase_button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add = new Intent(purchase_menu.this,activity_add.class);
                startActivity(add);
            }
        });
        Button b2 = findViewById(R.id.view_purchase_button);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent view_purchase = new Intent(purchase_menu.this,activity_view.class);
                startActivity(view_purchase);
            }
        });
        Button b3 = findViewById(R.id.expense_purchase_button);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent expense = new Intent(purchase_menu.this,activity_expense.class);
                startActivity(expense);
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_purchase){
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}

