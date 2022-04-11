package com.example.stockflux;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class reports extends AppCompatActivity {
    Button purchase_expense,product_list,sales_expense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        purchase_expense = findViewById(R.id.purchase_expense_reports_button);
        purchase_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(reports.this,purchase_expense_reports.class));
            }
        });

        sales_expense = findViewById(R.id.sales_expense_reports_button);
        sales_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(reports.this,sales_expense_reports.class));
            }
        });

        product_list = findViewById(R.id.Products_reports);
        product_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(reports.this,product_reports.class));
            }
        });

    }
}