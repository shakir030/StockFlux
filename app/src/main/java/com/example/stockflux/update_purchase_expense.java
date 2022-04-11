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

public class update_purchase_expense extends AppCompatActivity {
    TextInputEditText purchase_expense_update_name,purchase_expense_update_total_price,purchase_expense_update_description;
    Button purchase_expense_update_reset,purchase_expense_update_submit;
    FirebaseFirestore fPexpense_update;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String user_id = fAuth.getCurrentUser().getUid();
    String document_id,update_name,update_description;
    int update_total_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_purchase_expense);

        purchase_expense_update_name = findViewById(R.id.purchase_expense_update_name);
        purchase_expense_update_total_price = findViewById(R.id.purchase_expense_update_total_price);
        purchase_expense_update_description = findViewById(R.id.purchase_expense_update_description);

        document_id = getIntent().getStringExtra("doucmentid");
        update_name = getIntent().getStringExtra("p_expense_name");
        update_total_price = getIntent().getIntExtra("p_expense_total_price",0);
        update_description = getIntent().getStringExtra("p_expense_description");

        purchase_expense_update_name.setText(update_name);
        purchase_expense_update_total_price.setText(String.valueOf(update_total_price));
        purchase_expense_update_description.setText(update_description);

        purchase_expense_update_name.setEnabled(false);
        fPexpense_update = FirebaseFirestore.getInstance();

        purchase_expense_update_reset = findViewById(R.id.purchase_reset_update_button);
        purchase_expense_update_submit = findViewById(R.id.purchase_submit_update_button);

        purchase_expense_update_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                purchase_expense_update_total_price.setText(null);
                purchase_expense_update_description.setText(null);
            }
        });

        purchase_expense_update_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String total_price_string = purchase_expense_update_total_price.getText().toString().trim();

                if(total_price_string.isEmpty()){
                    purchase_expense_update_total_price.setError("Enter Price !!!");
                    purchase_expense_update_total_price.requestFocus();
                    return;
                }
                update_purchase_expense(document_id);

            }
        });

    }
    public void update_purchase_expense(String id) {
        model_class_purchase_expense purchase_expense_update = new model_class_purchase_expense();

        int total_price = Integer.parseInt(purchase_expense_update_total_price.getText().toString());
        String description = purchase_expense_update_description.getText().toString().trim();


        purchase_expense_update.setPurchase_expense_total_price(total_price);
        purchase_expense_update.setPurchase_expense_description(description);


        fPexpense_update.collection("Users").document(user_id).collection("purchaseExpenses").document(id).update("purchase_expense_total_price", total_price, "purchase_expense_description", description)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //startActivity(new Intent(update_purchase_expense.this,purchase_expense_reports.class));
                        Toast.makeText(update_purchase_expense.this, "Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(update_purchase_expense.this,purchase_expense_reports.class));
                    }
                });

    }
}