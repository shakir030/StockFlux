package com.example.stockflux;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class update_sales_details extends AppCompatActivity {
    public static final String TAG = "Addsales";
    private TextInputEditText sales_update_name,sales_update_id,sales_update_qty,sales_update_per_price,sales_update_discription;
    private Button sales_update_button,sales_reset_button;
    TextView total_qty_update;
    public FirebaseFirestore fStoreUpdate,fPurchaseDetails;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String user_id = fAuth.getCurrentUser().getUid();
    String document_id,sales_name,sales_id,sales_date,sales_description,purchase_id;
    int sales_qty,sales_per_price,total_QTY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_sales_details);

        sales_update_name = findViewById(R.id.sales_update_name);
        sales_update_id = findViewById(R.id.sales_update_id);
        sales_update_qty = findViewById(R.id.sales_update_qty);
        sales_update_discription = findViewById(R.id.sales_update_description);
        sales_update_per_price = findViewById(R.id.sales_update_per_price);
        total_qty_update = findViewById(R.id.qty_total_stored);

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
        purchase_id = getIntent().getStringExtra("purchaseid");



        sales_update_name.setText(sales_name);
        sales_update_id.setText(sales_id);
        sales_update_qty.setText(String.valueOf(sales_qty));
        sales_update_per_price.setText(String.valueOf(sales_per_price));
        sales_update_discription.setText(sales_description);


        sales_update_name.setEnabled(false);
        sales_update_id.setEnabled(false);

        fStoreUpdate = FirebaseFirestore.getInstance();
        fPurchaseDetails = FirebaseFirestore.getInstance();

        Query query = fPurchaseDetails.collection("Users").document(user_id).collection("addProducts").whereEqualTo("product_name",sales_name).whereEqualTo("product_id",purchase_id);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    model_class_purchase_add_data purchase_data = documentSnapshot.toObject(model_class_purchase_add_data.class);
                    int total_qty_purchase = purchase_data.getProduct_qty();
                    total_QTY = total_qty_purchase + sales_qty;
                    total_qty_update.setText(String.valueOf(total_QTY));
                }
            }
        });

        sales_update_button = findViewById(R.id.submit_update_sales_button);
        sales_update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check_qty = sales_update_qty.getText().toString().trim();
                String check_per_price = sales_update_per_price.getText().toString().trim();
                int check_qty_int = Integer.parseInt(sales_update_qty.getText().toString());
                int total_qty_int = Integer.parseInt(total_qty_update.getText().toString());

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

                if(check_qty_int > total_qty_int){
                    sales_update_qty.setError("Not Enough Quantity!!!");
                    sales_update_qty.requestFocus();
                    return;
                }
                Query query = fPurchaseDetails.collection("Users").document(user_id).collection("addProducts").whereEqualTo("product_name",sales_name).whereEqualTo("product_id",purchase_id);
                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            String purchase_document_id = documentSnapshot.getId();
                            int remaining_qty = total_qty_int - check_qty_int;
                            fPurchaseDetails.collection("Users").document(user_id).collection("addProducts").document(purchase_document_id).update("product_qty",remaining_qty).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(update_sales_details.this, "Quantity Updated ", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                });
                updateData(document_id);
            }
        });

    }
    public void updateData(String id) {


        model_class_sales_add sales_update = new model_class_sales_add();

        int qty = Integer.parseInt(sales_update_qty.getText().toString());
        int total_qty = Integer.parseInt(total_qty_update.getText().toString());
        int per_price = Integer.parseInt(sales_update_per_price.getText().toString());
        String description = sales_update_discription.getText().toString().trim();

        int remaining_qty = total_qty - qty;

        sales_update.setQuantity(qty);
        sales_update.setPer_price(per_price);
        sales_update.setDescription(description);


        fStoreUpdate.collection("Users").document(user_id).collection("AddSalesData").document(id).update("quantity", qty, "per_price", per_price, "description", description)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        startActivity(new Intent(update_sales_details.this, activity_sales_view.class));
                        Toast.makeText(update_sales_details.this, "Updated", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}