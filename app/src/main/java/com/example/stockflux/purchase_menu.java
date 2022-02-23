package com.example.stockflux;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.stockflux.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

public class purchase_menu extends AppCompatActivity implements View.OnClickListener {
    private Button btn1, btn2, btn3,btn4;
    // creating three buttons
    // by the of btn1, btn2,btn3,btn4

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // connecting buttons with the
        // layout using findViewById()
        btn1 = findViewById(R.id.b1);
        btn2 = findViewById(R.id.b2);
        btn3 = findViewById(R.id.b3);
        btn4 = findViewById(R.id.b4);
        // apply setOnClickListener over buttons
        btn1.setOnClickListener( purchase_menu.this);
        btn2.setOnClickListener( purchase_menu.this);
        btn3.setOnClickListener( purchase_menu.this);
        btn4.setOnClickListener( purchase_menu.this);
    }

    // common onClick() for all buttons
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            // cases applied over different buttons
            case R.id.b1:

                // Toast message appears when button pressed
                Intent intent = new Intent( purchase_menu.this, activity_add.class);
                startActivity(intent);
                break;

            case R.id.b2:

                intent = new Intent( purchase_menu.this,activity_view.class);
                startActivity(intent);
                break;

            case R.id.b3:

                intent = new Intent( purchase_menu.this,activity_expense.class);
                startActivity(intent);
                break;

            case R.id.b4:

                intent = new Intent( purchase_menu.this, activity_supplier.class);
                startActivity(intent);
                break;
        }
    }
}

