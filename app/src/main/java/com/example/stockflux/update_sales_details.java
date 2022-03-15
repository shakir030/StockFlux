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

public class update_sales_details extends AppCompatActivity {
    public static final String TAG = "Addsales";
    private TextInputEditText sales_update_name,sales_update_id,sales_update_qty,sales_update_per_price,sales_update_discription;
    private Button sales_update_button,sales_reset_button;
    public FirebaseFirestore fStoreUpdate;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String user_id = fAuth.getCurrentUser().getUid();
    String document_id,sales_name,sales_id,sales_date,sales_description;
    int sales_qty,sales_per_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_sales_details);

        sales_update_name = findViewById(R.id.sales_update_name);
        sales_update_id = findViewById(R.id.sales_update_id);
        sales_update_qty = findViewById(R.id.sales_update_qty);
        sales_update_discription = findViewById(R.id.sales_update_description);
        sales_update_per_price = findViewById(R.id.sales_update_per_price);

        sales_reset_button = findViewById(R.id.reset_update_sales_button);
        sales_reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sales_update_qty.setText(null);
                sales_update_per_price.setText(null);
                sales_update_discription.setText(null);
            }
        });

        document_id=getIntent().getStringExtra("doucmentid");
        sales_name = getIntent().getStringExtra("salesname");
        sales_id = getIntent().getStringExtra("salesid");
        sales_qty = getIntent().getIntExtra("sales_qty",0);
        sales_date = getIntent().getStringExtra("salesdate");
        sales_description = getIntent().getStringExtra("salesdescription");
        sales_per_price = getIntent().getIntExtra("sales_per_price",0);

        sales_update_name.setText(sales_name);
        sales_update_id.setText(sales_id);
        sales_update_qty.setText(String.valueOf(sales_qty));
        sales_update_per_price.setText(String.valueOf(sales_per_price));
        sales_update_discription.setText(sales_description);


        sales_update_name.setEnabled(false);
        sales_update_id.setEnabled(false);

        fStoreUpdate = FirebaseFirestore.getInstance();
        sales_update_button = findViewById(R.id.submit_update_sales_button);
        sales_update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check_qty = sales_update_qty.getText().toString().trim();
                String check_per_price = sales_update_per_price.getText().toString().trim();

                if(check_qty.isEmpty()){
                    sales_update_qty.setError("Enter Quantity");
                    sales_update_qty.requestFocus();
                    return;
                }
                if(check_per_price.isEmpty()){
                    sales_update_per_price.setError("Enter Per Price");
                    sales_update_per_price.requestFocus();
                    return;
                }
                updateData(document_id);
            }
        });

    }
    public void updateData(String id) {


        model_class_sales_add sales_update = new model_class_sales_add();

        int qty = Integer.parseInt(sales_update_qty.getText().toString());
        int per_price = Integer.parseInt(sales_update_per_price.getText().toString());
        String description = sales_update_discription.getText().toString().trim();


        sales_update.setQuantity(qty);
        sales_update.setPer_price(per_price);
        sales_update.setDescription(description);




        fStoreUpdate.collection("Users").document(user_id).collection("AddSalesData").document(id).update("quantity",qty,"per_price",per_price,"description",description)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        startActivity(new Intent(update_sales_details.this,activity_sales_view.class));
                        Toast.makeText(update_sales_details.this, "Updated", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}