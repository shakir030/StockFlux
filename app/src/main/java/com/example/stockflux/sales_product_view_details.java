package com.example.stockflux;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class sales_product_view_details extends AppCompatActivity {
    TextView data_name, data_id, data_qty, data_per_price, data_date, data_total_price, data_description, data_purchase_id;
    EditText costumer_name;
    Button delete_data_button, update_data_button, bill_generate, continue_bill;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String user_id = fAuth.getCurrentUser().getUid();
    MaterialCardView card_view;
    String document_id, purchase_document_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_product_view_details);

        data_name = findViewById(R.id.data_sales_product_name);
        data_id = findViewById(R.id.data_sales_product_id);
        data_qty = findViewById(R.id.data_sales_product_qty);
        data_per_price = findViewById(R.id.data_sales_product_per_price);
        data_date = findViewById(R.id.data_sales_product_date);
        data_total_price = findViewById(R.id.data_sales_product_total_price);
        data_description = findViewById(R.id.data_sales_product_description);
        data_purchase_id = findViewById(R.id.data_sales_product_purchase_id);
        costumer_name = findViewById(R.id.customer_sales_name);
        continue_bill = findViewById(R.id.continue_bill);
        bill_generate = findViewById(R.id.sales_make_bill);

        card_view = findViewById(R.id.card_product);

        delete_data_button = findViewById(R.id.delete_data_sales_button);
        update_data_button = findViewById(R.id.update_data_sales_button);


        String sales_product_name = getIntent().getStringExtra("product_sales_name");
        String date = getIntent().getStringExtra("product_sales_date");

        Date sales_product_date = null;
        try {
            sales_product_date = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Query query = db.collection("Users").document(user_id).collection("AddSalesData").whereEqualTo("name", sales_product_name).whereEqualTo("date", sales_product_date);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    Toast.makeText(sales_product_view_details.this, "document is empty", Toast.LENGTH_SHORT).show();
                } else {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        document_id = documentSnapshot.getId();
                        model_class_sales_add get_data = documentSnapshot.toObject(model_class_sales_add.class);
                        String product_name = get_data.getName();
                        String product_id = get_data.getID();
                        int product_qty = get_data.getQuantity();
                        int product_per_price = get_data.getPer_price();


                        Date date = get_data.getDate();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        String product_date = formatter.format(date);

                        int product_total_price = get_data.getTotal_Price();
                        String product_description = get_data.getDescription();
                        String purchase_id = get_data.getPurchase_ID();

                        data_name.setText(product_name);
                        data_id.setText(product_id);
                        data_qty.setText(String.valueOf(product_qty));
                        data_per_price.setText(String.valueOf(product_per_price));
                        data_date.setText(product_date);
                        data_total_price.setText(String.valueOf(product_total_price));
                        data_description.setText(product_description);
                        data_purchase_id.setText(purchase_id);

                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "onFailure: " + e.getMessage());
            }
        });


        delete_data_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String p_name = data_name.getText().toString().trim();
                String p_id = data_purchase_id.getText().toString().trim();
                int sales_qty = Integer.parseInt(data_qty.getText().toString());

                Query query = db.collection("Users").document(user_id).collection("addProducts").whereEqualTo("product_name", p_name).whereEqualTo("product_id", p_id);
                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()){
                            Toast.makeText(sales_product_view_details.this, "Database is empty and Quantity not Updated", Toast.LENGTH_LONG).show();
                            db.collection("Users").document(user_id).collection("AddSalesData").document(document_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(sales_product_view_details.this, "Product Deleted", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(sales_product_view_details.this, activity_sales_view.class));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("TAG", "onFailure: " + e.getMessage());
                                    Toast.makeText(sales_product_view_details.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            purchase_document_id = documentSnapshot.getId();
                            model_class_purchase_add_data purchase_data = documentSnapshot.toObject(model_class_purchase_add_data.class);
                            int purchase_qty = purchase_data.getProduct_qty();
                            int total_qty_purchase = purchase_qty + sales_qty;

                            String p_id = data_purchase_id.getText().toString().trim();
                            String name_p = data_name.getText().toString().trim();
                            db.collection("Users").document(user_id).collection("addProducts").document(purchase_document_id).update("product_qty", total_qty_purchase).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(sales_product_view_details.this, "Updated", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(sales_product_view_details.this, "On Failure" + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                            db.collection("Users").document(user_id).collection("AddSalesData").document(document_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(sales_product_view_details.this, "Product Deleted", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(sales_product_view_details.this, activity_sales_view.class));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("TAG", "onFailure: " + e.getMessage());
                                    Toast.makeText(sales_product_view_details.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }

                });
            }
        });

        update_data_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query query1 = db.collection("Users").document(user_id).collection("addProducts").whereEqualTo("product_id",data_purchase_id.getText().toString());
                query1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
                            Toast.makeText(sales_product_view_details.this, "Purchase Entry not Found", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Intent update_intent = new Intent(sales_product_view_details.this, update_sales_details.class);
                            String name = data_name.getText().toString().trim();
                            String product_id = data_id.getText().toString();
                            int qty = Integer.parseInt(data_qty.getText().toString());
                            int per_price = Integer.parseInt(data_per_price.getText().toString());
                            int total_price = Integer.parseInt(data_total_price.getText().toString().trim());
                            String date = data_date.getText().toString().trim();
                            String description = data_description.getText().toString().trim();
                            String purchase_id = data_purchase_id.getText().toString().trim();

                            update_intent.putExtra("doucmentid", document_id);
                            update_intent.putExtra("salesname", name);
                            update_intent.putExtra("salesid", product_id);
                            update_intent.putExtra("sales_qty", qty);
                            update_intent.putExtra("sales_per_price", per_price);
                            update_intent.putExtra("salesdate", date);
                            update_intent.putExtra("salesdescription", description);
                            update_intent.putExtra("purchaseid", purchase_id);
                            startActivity(update_intent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(sales_product_view_details.this, "On Failure "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        bill_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                costumer_name.setVisibility(View.VISIBLE);
                continue_bill.setVisibility(View.VISIBLE);

                String name = data_name.getText().toString().trim();
                String id = data_id.getText().toString().trim();
                int qty = Integer.parseInt(data_qty.getText().toString());
                int per_price = Integer.parseInt(data_per_price.getText().toString());
                int total_price = Integer.parseInt(data_total_price.getText().toString().trim());
                continue_bill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent continue_bill = new Intent(sales_product_view_details.this, make_bill_sales.class);
                        String costumer_name_text = costumer_name.getText().toString().trim();
                        continue_bill.putExtra("costumer_name", costumer_name_text);
                        continue_bill.putExtra("product_name", name);
                        continue_bill.putExtra("Bill_id",id);
                        continue_bill.putExtra("product_qty", qty);
                        continue_bill.putExtra("product_per_price", per_price);
                        continue_bill.putExtra("product_total_price", total_price);
                        startActivity(continue_bill);
                    }
                });
            }
        });
    }
}