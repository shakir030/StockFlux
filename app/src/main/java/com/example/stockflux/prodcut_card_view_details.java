package com.example.stockflux;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class prodcut_card_view_details extends AppCompatActivity {
    TextView data_name, data_id, data_qty, data_per_price, data_date, data_total_price, data_description;
    Button delete_data_button,update_data_button;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String user_id = fAuth.getCurrentUser().getUid();
    MaterialCardView card_view;
    Date product_date;
    String document_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prodcut_card_view_details);

        data_name = findViewById(R.id.data_product_name);
        data_id = findViewById(R.id.data_product_id);
        data_qty = findViewById(R.id.data_product_qty);
        data_per_price = findViewById(R.id.data_product_per_price);
        data_date = findViewById(R.id.data_product_date);
        data_total_price = findViewById(R.id.data_product_total_price);
        data_description = findViewById(R.id.data_product_description);

        card_view = findViewById(R.id.card_product);

        delete_data_button = findViewById(R.id.delete_data_product_button);
        update_data_button = findViewById(R.id.update_data_product_button);


        String product_name = getIntent().getStringExtra("product_name");
        String date = getIntent().getStringExtra("product_date");

        try {
            product_date = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Query query = db.collection("Users").document(user_id).collection("addProducts").whereEqualTo("product_name",product_name).whereEqualTo("product_date",product_date);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){
                    Toast.makeText(prodcut_card_view_details.this, "document is empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        document_id = documentSnapshot.getId();
                        model_class_purchase_add_data get_data = documentSnapshot.toObject(model_class_purchase_add_data.class);
                        String product_name = get_data.getProduct_name();
                        String product_id = get_data.getProduct_id();
                        int product_qty = get_data.getProduct_qty();
                        int product_per_price = get_data.getProduct_per_price();

                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        Date product_date = get_data.getProduct_date();
                        String string_product_date = formatter.format(product_date);

                        int product_total_price = get_data.getProduct_total_price();
                        String product_description = get_data.getProduct_description();

                        data_name.setText(product_name);
                        data_id.setText(product_id);
                        data_qty.setText(String.valueOf(product_qty));
                        data_per_price.setText(String.valueOf(product_per_price));
                        data_date.setText(string_product_date);
                        data_total_price.setText(String.valueOf(product_total_price));
                        data_description.setText(product_description);
                        
                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "onFailure: "+e.getMessage());
            }
        });


        delete_data_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Users").document(user_id).collection("addProducts").document(document_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(prodcut_card_view_details.this, "Doucment Deleted", Toast.LENGTH_SHORT).show();
                        card_view.setVisibility(View.GONE);
                        startActivity(new Intent(prodcut_card_view_details.this, purchase_view.class));
                    }
                });
            }
        });

        update_data_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Users").document(user_id).collection("addProducts").document(document_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Intent update_intent = new Intent(prodcut_card_view_details.this,update_purchase_details.class);
                        String name = data_name.getText().toString().trim();
                        String product_id = data_id.getText().toString();
                        int qty = Integer.parseInt(data_qty.getText().toString());
                        int per_price = Integer.parseInt(data_per_price.getText().toString());
                        int total_price = Integer.parseInt(data_total_price.getText().toString().trim());
                        String date = data_date.getText().toString().trim();
                        String description = data_description.getText().toString().trim();
                        update_intent.putExtra("doucmentid",document_id);
                        update_intent.putExtra("productname",name);
                        update_intent.putExtra("productid",product_id);
                        update_intent.putExtra("product_qty",qty);
                        update_intent.putExtra("product_per_price",per_price);
                        update_intent.putExtra("product_total_price",total_price);
                        update_intent.putExtra("productdate",date);
                        update_intent.putExtra("productdescription",description);
                        startActivity(update_intent);
                    }
                });
            }
        });


    }
}