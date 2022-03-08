package com.example.stockflux;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class activity_sales_home extends AppCompatActivity {
    Button add_sales,view_sales,expense_sales;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_home);

        add_sales = findViewById(R.id.add_sales_menu_button);
        view_sales = findViewById(R.id.view_sales_menu_button);
        expense_sales = findViewById(R.id.expense_sales_menu_button);

        add_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity_sales_home.this,sales_add.class));
            }
        });

        view_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity_sales_home.this,activity_sales_view.class));
            }
        });

        expense_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity_sales_home.this,activity_expense_sales.class));
            }
        });
    }
}