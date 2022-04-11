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

public class update_purchase_details extends AppCompatActivity {
   public static final String TAG = "AddPurchase";
   private TextInputEditText product_update_name,product_update_id,product_update_qty,product_update_per_price,product_update_total_price,product_update_discription;
   private Button update_button,reset_button;
   public FirebaseFirestore firestore_database;
    String document_id,product_name,product_id,product_date,product_description;
    int product_qty,product_per_price,product_total_price;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String user_id = fAuth.getCurrentUser().getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_purchase_details);

        product_update_name = findViewById(R.id.purchase_update_name);
        product_update_id = findViewById(R.id.purchase_update_id);
        product_update_qty = findViewById(R.id.purchase_update_qty);
        product_update_per_price = findViewById(R.id.purchase_update_per_price);
        product_update_discription = findViewById(R.id.purchase_update_description);
        product_update_total_price = findViewById(R.id.purchase_update_total_price);

        reset_button = findViewById(R.id.reset_update_button);
        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                product_update_qty.setText(null);
                product_update_per_price.setText(null);
                product_update_discription.setText(null);
            }
        });

        document_id=getIntent().getStringExtra("doucmentid");
        product_name = getIntent().getStringExtra("productname");
        product_id = getIntent().getStringExtra("productid");
        product_qty = getIntent().getIntExtra("product_qty",0);
        product_per_price = getIntent().getIntExtra("product_per_price",0);
        product_date = getIntent().getStringExtra("productdate");
        product_description = getIntent().getStringExtra("productdescription");
        product_total_price = getIntent().getIntExtra("product_total_price",0);

        product_update_name.setText(product_name);
        product_update_id.setText(product_id);
        product_update_qty.setText(String.valueOf(product_qty));
        product_update_per_price.setText(String.valueOf(product_per_price));
        product_update_total_price.setText(String.valueOf(product_total_price));
        product_update_discription.setText(product_description);


        product_update_name.setEnabled(false);
        product_update_id.setEnabled(false);
        product_update_total_price.setEnabled(false);

        firestore_database = FirebaseFirestore.getInstance();
        update_button = findViewById(R.id.submit_update_button);
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check_qty = product_update_qty.getText().toString().trim();
                String check_per_price = product_update_per_price.getText().toString().trim();

                if(check_qty.isEmpty()){
                    product_update_qty.setError("Enter Quantity");
                    product_update_qty.requestFocus();
                    return;
                }
                if(check_per_price.isEmpty()){
                    product_update_per_price.setError("Enter Per Price");
                    product_update_per_price.requestFocus();
                    return;
                }
                updateData(document_id);
           }
        });

    }
    public void updateData(String id) {


       model_class_purchase_add_data purchase_update = new model_class_purchase_add_data();

        int qty = Integer.parseInt(product_update_qty.getText().toString());
        int per_price = Integer.parseInt(product_update_per_price.getText().toString());
        int total_price = Integer.parseInt(product_update_total_price.getText().toString());
        String description = product_update_discription.getText().toString().trim();


        purchase_update.setProduct_qty(qty);
        purchase_update.setProduct_per_price(per_price);
        purchase_update.setProduct_description(description);
        total_price = qty * per_price;
        purchase_update.setProduct_total_price(total_price);




        firestore_database.collection("Users").document(user_id).collection("addProducts").document(id).update("product_qty",qty,"product_per_price",per_price,"product_description",description,"product_total_price",total_price)

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        startActivity(new Intent(update_purchase_details.this, purchase_view.class));
                        Toast.makeText(update_purchase_details.this, "Updated", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}