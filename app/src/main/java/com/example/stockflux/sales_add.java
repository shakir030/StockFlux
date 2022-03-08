package com.example.stockflux;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class sales_add extends AppCompatActivity {
    public static final String TAG = "AddSales";
    TextInputEditText product_add_name, product_add_id, product_add_qty,product_add_per_price,product_add_total_price, product_add_description;
    TextView product_add_date,total_qty;
    Spinner spinner_list_name;
    Button sales_add_submit_button, sales_add_reset_button, search_button_sales,add_name_button;
    DatePickerDialog datePickerDialog;
    FirebaseFirestore fStoreSalesData;
    String sales_document_id,product_id;
    int remaining_qty,total_price;
    ArrayList<String> sales_values = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_add);

        fStoreSalesData = FirebaseFirestore.getInstance();

        product_add_name = findViewById(R.id.sales_add_name);
        product_add_id = findViewById(R.id.sales_add_id);
        product_add_qty = findViewById(R.id.sales_add_qty);
        product_add_date = findViewById(R.id.sales_add_date);
        product_add_per_price = findViewById(R.id.sales_add_per_price);
        product_add_total_price = findViewById(R.id.sales_add_total_price);
        product_add_description = findViewById(R.id.sales_add_description);
        spinner_list_name = findViewById(R.id.spinner_add_view_purchase);
        search_button_sales = findViewById(R.id.search_button_sales);
        total_qty = findViewById(R.id.total_qty);
        add_name_button = findViewById(R.id.search_name_product);

        add_name_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
                String currentDateandTime = sdf.format(new Date());*/

                fStoreSalesData.collection("addProducts").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                model_class_purchase_add_data data_view = documentSnapshot.toObject(model_class_purchase_add_data.class);
                                String product_name = data_view.getProduct_name();
                                sales_values.add(product_name);
                                ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<String>(sales_add.this, android.R.layout.simple_spinner_dropdown_item, sales_values);
                                spinner_list_name.setAdapter(SpinnerAdapter);
                                search_button_sales.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(sales_add.this, "No data Found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        search_button_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                querydata();
                fStoreSalesData.collection("addProducts").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                                model_class_purchase_add_data data_view = documentSnapshot.toObject(model_class_purchase_add_data.class);
                                String product_name = data_view.getProduct_name();
                                sales_values.remove(product_name);
                                ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<String>(sales_add.this, android.R.layout.simple_spinner_dropdown_item, sales_values);
                                spinner_list_name.setAdapter(SpinnerAdapter);
                                search_button_sales.setVisibility(View.GONE);
                            }
                        }
                        else
                        {
                            Toast.makeText(sales_add.this, "No Data Found Button", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        sales_add_reset_button = findViewById(R.id.sales_add_reset_button);
        //clear the values entered in textfield
        sales_add_reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //product_add_name.setText(null);
                product_add_id.setText(null);
                product_add_qty.setText(null);
                product_add_date.setText(null);
                product_add_per_price.setText(null);
                product_add_description.setText(null);
            }
        });

        sales_add_submit_button = findViewById(R.id.sales_add_add_button);
        sales_add_submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validating no empty textfield


                String sales_add_id = product_add_id.getText().toString().trim();
                String sales_add_qty = product_add_qty.getText().toString().trim();
                int sales_qty_int = Integer.parseInt(product_add_qty.getText().toString());
                int total_qty_string = Integer.parseInt(total_qty.getText().toString());
                remaining_qty = total_qty_string - sales_qty_int;
                int sales_add_per_price = Integer.parseInt(product_add_per_price.getText().toString());
                total_price = sales_qty_int * sales_add_per_price;
                String per_price_string = product_add_per_price.getText().toString().trim();
                String sales_add_date = product_add_date.getText().toString().trim();

                if (sales_add_id.isEmpty()) {
                    product_add_id.setError("Enter ID !!");
                    product_add_id.requestFocus();
                    return;
                }
                if (sales_add_qty.isEmpty()) {
                    product_add_qty.setError("Enter Quantity !!");
                    product_add_qty.requestFocus();
                    return;
                }
                if(per_price_string.isEmpty()){
                    product_add_per_price.setError("Enter Per/price");
                    product_add_per_price.requestFocus();
                    return;
                }
                if (sales_add_date.isEmpty()) {
                    product_add_date.setError("Enter Date !!");
                    product_add_date.requestFocus();
                    return;
                }
                if(sales_qty_int > total_qty_string ){
                    product_add_qty.setError("Not Enough Quanity");
                    product_add_qty.requestFocus();
                    return;
                }


                Query check_id = fStoreSalesData.collection("AddSalesData").whereEqualTo("id",sales_add_id);
                check_id.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            product_add_id.setError("Enter Another ID");
                            product_add_id.requestFocus();
                        }
                        else {
                            String purchase_add_name = product_add_name.getText().toString().trim();
                            String date_pick = product_add_date.getText().toString().trim();
                            Query nquery = fStoreSalesData.collection("AddSalesData").whereEqualTo("date", date_pick).whereEqualTo("name", purchase_add_name);
                            nquery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        product_add_name.setError("Entry of this product is already done");
                                        product_add_name.requestFocus();
                                    } else {
                                        AddData(sales_document_id); //adds data to firestore databasee
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

        product_add_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(sales_add.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                product_add_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

    }
    private void querydata() {

        String text = spinner_list_name.getSelectedItem().toString();

        Query query = fStoreSalesData.collection("addProducts").whereEqualTo("product_name", text);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                StringBuilder builder = new StringBuilder();
                if (queryDocumentSnapshots.isEmpty()) {
                    Toast.makeText(sales_add.this, "no data found", Toast.LENGTH_SHORT).show();
                } else {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        sales_document_id = documentSnapshot.getId();
                        model_class_purchase_add_data data_view = documentSnapshot.toObject(model_class_purchase_add_data.class);
                        product_id = data_view.getProduct_id();
                        String product_name = data_view.getProduct_name();
                        product_add_name.setText(product_name);
                        product_add_name.setEnabled(false);

                        int product_qty = data_view.getProduct_qty();
                        total_qty.setText(String.valueOf(product_qty));

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(sales_add.this, "Enter Both Details", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void AddData(String id) {

        model_class_sales_add add_data = new model_class_sales_add();
        String sales_add_name = product_add_name.getText().toString().trim();
        String sales_add_id = product_add_id.getText().toString().trim();
        String sales_add_date = product_add_date.getText().toString().trim();
        String sales_add_description = product_add_description.getText().toString().trim();
        int sales_add_qty = Integer.parseInt(product_add_qty.getText().toString());
        int sales_per_price_add = Integer.parseInt(product_add_per_price.getText().toString().trim());
        int sales_add_total_price = add_data.getTotal_Price();

        add_data.setName(sales_add_name);
        add_data.setID(sales_add_id);
        add_data.setQuantity(sales_add_qty);
        add_data.setPer_price(sales_per_price_add);
        add_data.setTotal_Price(sales_add_total_price);
        add_data.setDate(sales_add_date);
        add_data.setDescription(sales_add_description);

        fStoreSalesData.collection("AddSalesData").document().set(add_data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(sales_add.this, "Data added successfully", Toast.LENGTH_LONG).show();
                product_add_name.setText(null);
                product_add_id.setText(null);
                product_add_qty.setText(null);
                total_qty.setText(null);
                product_add_date.setText(null);
                product_add_total_price.setText(null);
                product_add_description.setText(null);

                fStoreSalesData.collection("addProducts").document(id).update("product_qty", remaining_qty).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(sales_add.this, "qty updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"onFailure: "+e.getMessage());
                        Toast.makeText(sales_add.this, "Error is "+e.getMessage(), Toast.LENGTH_LONG).show();
                        sales_add_submit_button.setEnabled(false);
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: " + e.getMessage());
                Toast.makeText(sales_add.this, "Failed : Error is " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}