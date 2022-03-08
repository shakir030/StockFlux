package com.example.stockflux;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class activity_view extends AppCompatActivity {

    private TextView select_date, data_name, data_id, data_qty, data_per_price, data_date, data_total_price, data_description;
    private Button search_button, select_date_button,delete_data_button,update_data_button;
    private Spinner spinner_list_name_product;
    private MaterialCardView card_view;
    private DatePickerDialog datePickerDialog;
    private FirebaseFirestore db;
    private String document_id;
    ArrayList<String> values = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filled_purchase_view);

        data_name = findViewById(R.id.data_view_name);
        data_id = findViewById(R.id.data_view_id);
        data_qty = findViewById(R.id.data_view_qty);
        data_per_price = findViewById(R.id.data_view_per_price);
        data_date = findViewById(R.id.data_view_date);
        data_total_price = findViewById(R.id.data_view_total_price);
        data_description = findViewById(R.id.data_view_description);
        select_date = findViewById(R.id.date_select_view);
        search_button = findViewById(R.id.search_button);
        select_date_button = findViewById(R.id.select_date_button);
        spinner_list_name_product = findViewById(R.id.spinner_add_view_purchase);
        card_view = findViewById(R.id.card);

        db = FirebaseFirestore.getInstance(); // get firestore data instance

        // select date and values to spinner data view
        select_date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date_pick = select_date.getText().toString().trim();
                Query query = db.collection("addProducts").whereEqualTo("product_date", date_pick);
                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                model_class_purchase_add_data data_view = documentSnapshot.toObject(model_class_purchase_add_data.class);
                                String product_name = data_view.getProduct_name();
                                values.add(product_name);
                                ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<String>(activity_view.this, android.R.layout.simple_spinner_dropdown_item, values);
                                spinner_list_name_product.setAdapter(SpinnerAdapter);
                                search_button.setClickable(true);
                            }
                        }
                        else
                        {
                            card_view.setVisibility(View.GONE);
                            spinner_list_name_product.setAdapter(null);
                            Toast.makeText(activity_view.this, "No Data Found", Toast.LENGTH_SHORT).show();
                            search_button.setClickable(false);
                        }
                    }
                });
            }
        });


        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getdata();
                querydata();

                String date_pick = select_date.getText().toString().trim();

                Query rquery = db.collection("addProducts").whereEqualTo("product_date",date_pick);
                rquery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                                model_class_purchase_add_data data_view = documentSnapshot.toObject(model_class_purchase_add_data.class);
                                String product_name = data_view.getProduct_name();
                                values.remove(product_name);
                                ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<String>(activity_view.this, android.R.layout.simple_spinner_dropdown_item, values);
                                spinner_list_name_product.setAdapter(SpinnerAdapter);
                                search_button.setClickable(false);
                            }
                        }
                        else
                        {
                            Toast.makeText(activity_view.this, "No Data Found Button", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(activity_view.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                select_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        delete_data_button = findViewById(R.id.delete_data_button);
        delete_data_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_data(document_id);
            }
        });

        update_data_button = findViewById(R.id.update_add_button);
        update_data_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_data(document_id);
            }
        });
    }



    private void querydata() {
        String date_pick = select_date.getText().toString().trim();
        String text = spinner_list_name_product.getSelectedItem().toString();

        Query query = db.collection("addProducts").whereEqualTo("product_date", date_pick).whereEqualTo("product_name", text);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                StringBuilder builder = new StringBuilder();
                if (queryDocumentSnapshots.isEmpty()) {
                    Toast.makeText(activity_view.this, "no data found", Toast.LENGTH_SHORT).show();
                    card_view.setVisibility(View.GONE);
                } else {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        document_id = documentSnapshot.getId();
                        model_class_purchase_add_data data_view = documentSnapshot.toObject(model_class_purchase_add_data.class);
                        String product_name = data_view.getProduct_name();
                        card_view.setVisibility(View.VISIBLE);
                        data_name.setText(product_name);

                        String product_id = data_view.getProduct_id();
                        data_id.setText(product_id);

                        int product_qty = data_view.getProduct_qty();
                        data_qty.setText(String.valueOf(product_qty));

                        int product_per_price = data_view.getProduct_per_price();
                        data_per_price.setText(String.valueOf(product_per_price));

                        String product_date = data_view.getProduct_date();
                        data_date.setText(product_date);

                        int product_total_price = data_view.getProduct_total_price();
                        data_total_price.setText(String.valueOf(product_total_price));

                        String product_description = data_view.getProduct_description();
                        data_description.setText(product_description);

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity_view.this, "Enter Both Details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void delete_data(String id)
    {
        db.collection("addProducts").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity_view.this, "Doucment Deleted", Toast.LENGTH_SHORT).show();
                card_view.setVisibility(View.GONE);
            }
        });
    }
    public void update_data(String id)
    {
        db.collection("addProducts").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Intent update = new Intent(activity_view.this,update_purchase_details.class);
                String document_id = id.toString().trim();
                String name = data_name.getText().toString().trim();
                String product_id = data_id.getText().toString();
                int qty = Integer.parseInt(data_qty.getText().toString());
                int per_price = Integer.parseInt(data_per_price.getText().toString());
                int total_price = Integer.parseInt(data_total_price.getText().toString().trim());
                String date = data_date.getText().toString().trim();
                String description = data_description.getText().toString().trim();
                update.putExtra("doucmentid",document_id);
                update.putExtra("productname",name);
                update.putExtra("productid",product_id);
                update.putExtra("product_qty",qty);
                update.putExtra("product_per_price",per_price);
                update.putExtra("product_total_price",total_price);
                update.putExtra("productdate",date);
                update.putExtra("productdescription",description);
                startActivity(update);
            }
        });
    }

    /*private void getdata() {
        db.collection("addProducts").document("n9CjsQl75Uc5JyRVmgKL").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                model_class_purchase_add_data data_view = documentSnapshot.toObject(model_class_purchase_add_data.class);

                String product_name = data_view.getProduct_name();
                data_name.setText(product_name);

                String product_id= data_view.getProduct_id();
                data_id.setText(product_id);

                int product_qty = data_view.getProduct_qty();
                data_qty.setText(String.valueOf(product_qty));

                int product_per_price = data_view.getProduct_per_price();
                data_per_price.setText(String.valueOf(product_per_price));

                String product_date= data_view.getProduct_date();
                data_date.setText(product_date);

                int product_total_price = data_view.getProduct_per_price();
                data_total_price.setText(String.valueOf(product_total_price));

                String product_description= data_view.getProduct_description();
                data_description.setText(product_description);
            }
        });
    }*/
}