package com.example.stockflux;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class activity_sales_home extends AppCompatActivity implements View.OnClickListener {
    private Button btn1, btn2, btn3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_home);
        btn1 = findViewById(R.id.b1);
        btn2 = findViewById(R.id.b2);
        btn3 = findViewById(R.id.b3);
        // apply setOnClickListener over buttons
        btn1.setOnClickListener(activity_sales_home.this);
        btn2.setOnClickListener(activity_sales_home.this);
        btn3.setOnClickListener(activity_sales_home.this);
    }

    // common onClick() for all buttons
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            // cases applied over different buttons
            case R.id.b1:

                // Toast message appears when button pressed
                Intent intent = new Intent(activity_sales_home.this, sales_add.class);
                startActivity(intent);
                break;

            case R.id.b2:

                intent = new Intent(activity_sales_home.this,activity_view.class);
                startActivity(intent);
                break;

            case R.id.b3:

                intent = new Intent(activity_sales_home.this,activity_expense.class);
                startActivity(intent);
                break;

        }
    }
}