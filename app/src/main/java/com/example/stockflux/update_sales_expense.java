package com.example.stockflux;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class update_sales_expense extends AppCompatActivity {
    TextInputEditText sales_expense_update_name,sales_expense_update_total_price,sales_expense_update_description;
    Button sales_expense_update_reset,sales_expense_update_submit;
    FirebaseFirestore fS_expense_update;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String user_id = fAuth.getCurrentUser().getUid();
    String document_id,update_name,update_description;
    int update_total_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_sales_expense);

        sales_expense_update_name = findViewById(R.id.sales_expense_update_name);
        sales_expense_update_total_price = findViewById(R.id.sales_expense_update_total_price);
        sales_expense_update_description = findViewById(R.id.sales_expense_update_description);

        document_id = getIntent().getStringExtra("doucmentid");
        update_name = getIntent().getStringExtra("s_expense_name");
        update_total_price = getIntent().getIntExtra("s_expense_total_price",0);
        update_description = getIntent().getStringExtra("s_expense_description");

        sales_expense_update_name.setText(update_name);
        sales_expense_update_total_price.setText(String.valueOf(update_total_price));
        sales_expense_update_description.setText(update_description);

        sales_expense_update_name.setEnabled(false);
        fS_expense_update = FirebaseFirestore.getInstance();

        sales_expense_update_reset = findViewById(R.id.sales_reset_update_button);
        sales_expense_update_submit = findViewById(R.id.sales_submit_update_button);

        sales_expense_update_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sales_expense_update_total_price.setText(null);
                sales_expense_update_description.setText(null);
            }
        });

        sales_expense_update_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String total_price_string = sales_expense_update_total_price.getText().toString().trim();

                if(total_price_string.isEmpty()){
                    sales_expense_update_total_price.setError("Enter Price !!!");
                    sales_expense_update_total_price.requestFocus();
                    return;
                }
                update_sales_expense(document_id);

            }
        });

    }
    public void update_sales_expense(String id) {
        model_class_sales_expenses sales_expense_update = new model_class_sales_expenses();

        int total_price = Integer.parseInt(sales_expense_update_total_price.getText().toString());
        String description = sales_expense_update_description.getText().toString().trim();


        sales_expense_update.setTotal_Price(total_price);
        sales_expense_update.setDescription(description);


        fS_expense_update.collection("Users").document(user_id).collection("salesExpenses").document(id).update("total_Price", total_price, "description", description)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //startActivity(new Intent(update_sales_expense.this,sales_expense_reports.class));
                        Toast.makeText(update_sales_expense.this, "Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(update_sales_expense.this,sales_expense_reports.class));

                    }
                });

    }
}