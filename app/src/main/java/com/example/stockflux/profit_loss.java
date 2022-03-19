package com.example.stockflux;

import android.app.DatePickerDialog;
import android.graphics.Color;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class profit_loss extends AppCompatActivity {
    TextView select_date_pl,particular_data,purchase_data_pl,sales_data_pl,profit_loss_data;
    DatePickerDialog datePickerDialog;
    Button select_date_button_pl,search_button_pl;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String user_id = fAuth.getCurrentUser().getUid();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Spinner spinner_list_name_pl;
    ArrayList<String> values_store_pl = new ArrayList<String>();
    String purchase_id;
    int sales_qty,sales_per_price,purchase_per_price,result_pl,purchase_price,sales_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit_loss);

        select_date_button_pl = findViewById(R.id.select_date_button_pl);
        select_date_pl = findViewById(R.id.date_select_pl);
        spinner_list_name_pl = findViewById(R.id.spinner_add_view_pl);
        search_button_pl = findViewById(R.id.search_button_pl);
        particular_data = findViewById(R.id.particulars_data);
        purchase_data_pl = findViewById(R.id.purchase_data);
        sales_data_pl = findViewById(R.id.sales_data);
        profit_loss_data = findViewById(R.id.profit_loss_data);

        //select date code
        select_date_pl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(profit_loss.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                select_date_pl.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        select_date_button_pl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date_pick = select_date_pl.getText().toString().trim();
                Query query = db.collection("Users").document(user_id).collection("AddSalesData").whereEqualTo("date", date_pick);
                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                model_class_sales_add sales_view = documentSnapshot.toObject(model_class_sales_add.class);
                                String sales_name = sales_view.getName();
                                values_store_pl.add(sales_name);
                                ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<String>(profit_loss.this, android.R.layout.simple_spinner_dropdown_item, values_store_pl);
                                spinner_list_name_pl.setAdapter(SpinnerAdapter);
                                search_button_pl.setVisibility(View.VISIBLE);
                            }
                        } else {
                            search_button_pl.setVisibility(View.GONE);
                            particular_data.setText(null);
                            purchase_data_pl.setText(null);
                            sales_data_pl.setText(null);
                            profit_loss_data.setText(null);
                            spinner_list_name_pl.setAdapter(null);
                            Toast.makeText(profit_loss.this, "No Data Found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        search_button_pl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                particular_data.setText(spinner_list_name_pl.getSelectedItem().toString());

                querydata();

                String date_pick = select_date_pl.getText().toString().trim();

                Query rquery = db.collection("Users").document(user_id).collection("AddSalesData").whereEqualTo("date", date_pick);
                rquery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                model_class_sales_add data_sales_view = documentSnapshot.toObject(model_class_sales_add.class);
                                String product_name = data_sales_view.getName();
                                values_store_pl.remove(product_name);
                                ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<String>(profit_loss.this, android.R.layout.simple_spinner_dropdown_item, values_store_pl);
                                spinner_list_name_pl.setAdapter(SpinnerAdapter);
                                search_button_pl.setVisibility(View.GONE);
                            }
                        } else {
                            search_button_pl.setVisibility(View.GONE);
                            particular_data.setText(null);
                            purchase_data_pl.setText(null);
                            sales_data_pl.setText(null);
                            profit_loss_data.setText(null);
                            spinner_list_name_pl.setAdapter(null);
                            Toast.makeText(profit_loss.this, "No Data Found Button", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        /*item_name = findViewById(R.id.item_name_data);
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
        });*/

    }

    private void querydata() {
        String date = select_date_pl.getText().toString().trim();
        String name_spinner = spinner_list_name_pl.getSelectedItem().toString();


        Query squery = db.collection("Users").document(user_id).collection("AddSalesData").whereEqualTo( "date",date).whereEqualTo("name",name_spinner);
        squery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    Toast.makeText(profit_loss.this, "Database is Empty", Toast.LENGTH_SHORT).show();
                } else {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        model_class_sales_add sales_data = documentSnapshot.toObject(model_class_sales_add.class);
                        purchase_id = sales_data.getPurchase_ID();
                        sales_qty = sales_data.getQuantity();
                        sales_per_price = sales_data.getPer_price();
                        sales_price = sales_qty * sales_per_price;


                        Query pquery = db.collection("Users").document(user_id).collection("addProducts").whereEqualTo("product_id", purchase_id);
                        pquery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots.isEmpty()) {
                                    Toast.makeText(profit_loss.this, "Database is purchase", Toast.LENGTH_SHORT).show();
                                } else {
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        model_class_purchase_add_data purchase_data = documentSnapshot.toObject(model_class_purchase_add_data.class);
                                        purchase_per_price = purchase_data.getProduct_per_price();
                                        purchase_price = sales_qty * purchase_per_price;
                                        result_pl = sales_price - purchase_price;
                                        purchase_data_pl.setText(String.valueOf(purchase_price));
                                        sales_data_pl.setText(String.valueOf(sales_price));
                                        if(sales_price > purchase_price){
                                            profit_loss_data.setText(String.valueOf(result_pl));
                                            profit_loss_data.setTextColor(Color.GREEN);
                                        }
                                        else
                                        {
                                            profit_loss_data.setText(String.valueOf(result_pl));
                                            profit_loss_data.setTextColor(Color.RED);
                                        }
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(profit_loss.this, "NO Data Found", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profit_loss.this, "No Data Found", Toast.LENGTH_SHORT).show();
            }
        });








    }
}