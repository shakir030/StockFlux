package com.example.stockflux;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class expense_reports extends AppCompatActivity {
    Button purchase_expense,sales_expense;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_reports);

        purchase_expense = findViewById(R.id.purchase_expense);
        sales_expense = findViewById(R.id.sales_expense);

        purchase_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(expense_reports.this,purchase_expense_reports.class));
            }
        });

        sales_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(expense_reports.this,sales_expense_reports.class));
            }
        });

    }
}