package com.example.stockflux;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class profit_loss extends AppCompatActivity {
    TextView item_name,purchase_price,sales_price,profit_loss_status;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    Button submit_pl;
    String user_id = fAuth.getCurrentUser().getUid();
    FirebaseFirestore db;
    String name_item;
    int purchase_total_price,sales_total_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit_loss);

        item_name = findViewById(R.id.item_name_data);
        purchase_price = findViewById(R.id.purchase_total_price_data);
        sales_price = findViewById(R.id.sales_total_price_data);
        profit_loss_status = findViewById(R.id.profit_loss_status_data);

        db = FirebaseFirestore.getInstance();
        Query query = db.collection("Users").document(user_id).collection("addProducts").whereEqualTo("product_name","shirt");
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    model_class_purchase_add_data purchase_model = documentSnapshot.toObject(model_class_purchase_add_data.class);
                    name_item = purchase_model.getProduct_name();
                    purchase_total_price = purchase_model.getProduct_total_price();

                    item_name.setText(name_item);
                    purchase_price.setText(String.valueOf(purchase_total_price));
                }
            }
        });

        Query squery = db.collection("Users").document(user_id).collection("AddSalesData").whereEqualTo("name","shirt");
        squery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    model_class_sales_add sales_model = documentSnapshot.toObject(model_class_sales_add.class);
                    sales_total_price = sales_model.getTotal_Price();
                    sales_price.setText(String.valueOf(sales_total_price));
                }
            }
        });


        submit_pl = findViewById(R.id.submit_pl);
        submit_pl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(purchase_total_price < sales_total_price){
                    int result = sales_total_price - purchase_total_price;
                    profit_loss_status.setText(String.valueOf(result));
                    profit_loss_status.setTextColor(Color.GREEN);
                }
                else
                {
                    int result = sales_total_price - purchase_total_price;
                    profit_loss_status.setText(String.valueOf(result));
                    profit_loss_status.setTextColor(Color.RED);
                }
            }
        });

    }
}