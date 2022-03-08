package com.example.stockflux;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

public class activity_sales_view extends AppCompatActivity {
    EditText costumer_name;
    TextView select_sales_date, data_sales_name, data_sales_id, data_sales_qty,data_sales_per_price,data_sales_date, data_sales_total_price, data_sales_description;
    Button search_button_sales, select_date_button_sales, delete_data_button_sales, update_data_button_sales, bill_generate_button,continue_button;
    Spinner spinner_list_name_product_sales;
    MaterialCardView card_view_sales;
    DatePickerDialog datePickerDialog;
    FirebaseFirestore fSalesView;
    String document_id;
    ArrayList<String> values_store_sales = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_view);

        data_sales_name = findViewById(R.id.data_sales_view_name);
        data_sales_id = findViewById(R.id.data_sales_view_id);
        data_sales_qty = findViewById(R.id.data_sales_view_qty);
        data_sales_per_price = findViewById(R.id.data_sales_view_per_price);
        data_sales_date = findViewById(R.id.data_sales_view_date);
        data_sales_total_price = findViewById(R.id.data_sales_view_total_price);
        data_sales_description = findViewById(R.id.data_sales_view_description);
        select_sales_date = findViewById(R.id.date_select_sales_view);
        search_button_sales = findViewById(R.id.search_button_sales_view);
        select_date_button_sales = findViewById(R.id.select_sales_date_button);
        spinner_list_name_product_sales = findViewById(R.id.spinner_add_view_sales);
        card_view_sales = findViewById(R.id.card_sales_view);
        bill_generate_button = findViewById(R.id.make_bill);
        costumer_name = findViewById(R.id.costumer_name);
        continue_button = findViewById(R.id.bill_continue);


        fSalesView = FirebaseFirestore.getInstance(); // get firestore data instance

        // select date and values to spinner data view
        select_date_button_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date_pick = select_sales_date.getText().toString().trim();
                Query query = fSalesView.collection("AddSalesData").whereEqualTo("date", date_pick);
                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                model_class_sales_add sales_view = documentSnapshot.toObject(model_class_sales_add.class);
                                String sales_name = sales_view.getName();
                                values_store_sales.add(sales_name);
                                ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<String>(activity_sales_view.this, android.R.layout.simple_spinner_dropdown_item, values_store_sales);
                                spinner_list_name_product_sales.setAdapter(SpinnerAdapter);
                                search_button_sales.setVisibility(View.VISIBLE);
                            }
                        } else {
                            card_view_sales.setVisibility(View.GONE);
                            search_button_sales.setVisibility(View.GONE);
                            costumer_name.setVisibility(View.GONE);
                            continue_button.setVisibility(View.GONE);
                            spinner_list_name_product_sales.setAdapter(null);
                            Toast.makeText(activity_sales_view.this, "No Data Found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        search_button_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                querydata();

                String date_pick = select_sales_date.getText().toString().trim();

                Query rquery = fSalesView.collection("AddSalesData").whereEqualTo("date", date_pick);
                rquery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                model_class_sales_add data_sales_view = documentSnapshot.toObject(model_class_sales_add.class);
                                String product_name = data_sales_view.getName();
                                values_store_sales.remove(product_name);
                                ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<String>(activity_sales_view.this, android.R.layout.simple_spinner_dropdown_item, values_store_sales);
                                spinner_list_name_product_sales.setAdapter(SpinnerAdapter);
                                search_button_sales.setVisibility(View.GONE);
                                costumer_name.setVisibility(View.GONE);
                                continue_button.setVisibility(View.GONE);
                            }
                        } else {
                            search_button_sales.setVisibility(View.GONE);
                            costumer_name.setVisibility(View.GONE);
                            continue_button.setVisibility(View.GONE);
                            Toast.makeText(activity_sales_view.this, "No Data Found Button", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        select_sales_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                costumer_name.setVisibility(View.GONE);
                continue_button.setVisibility(View.GONE);
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(activity_sales_view.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                select_sales_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        delete_data_button_sales = findViewById(R.id.delete_sales_data_button);
        delete_data_button_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                costumer_name.setVisibility(View.GONE);
                continue_button.setVisibility(View.GONE);
                delete_data(document_id);
            }
        });

        costumer_name = findViewById(R.id.costumer_name);
        continue_button = findViewById(R.id.bill_continue);
        bill_generate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                costumer_name.setVisibility(View.VISIBLE);
                continue_button.setVisibility(View.VISIBLE);

                String costumer_name_text = costumer_name.getText().toString().trim();
                String name = data_sales_name.getText().toString().trim();
                int qty = Integer.parseInt(data_sales_qty.getText().toString());
                int per_price = Integer.parseInt(data_sales_per_price.getText().toString());
                int total_price = Integer.parseInt(data_sales_total_price.getText().toString().trim());
                continue_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent continue_bill = new Intent(activity_sales_view.this,make_bill_sales.class);
                        continue_bill.putExtra("costumer_name",costumer_name_text);
                        continue_bill.putExtra("product_name",name);
                        continue_bill.putExtra("product_qty",qty);
                        continue_bill.putExtra("product_per_price",per_price);
                        continue_bill.putExtra("product_total_price",total_price);
                        startActivity(continue_bill);
                    }
                });
            }
        });

        update_data_button_sales = findViewById(R.id.update_sales_add_button);
        update_data_button_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_data(document_id);
            }
        });
    }


        public void querydata(){
            String date_pick = select_sales_date.getText().toString().trim();
            String text = spinner_list_name_product_sales.getSelectedItem().toString();

            costumer_name.setVisibility(View.GONE);
            continue_button.setVisibility(View.GONE);

            Query query = fSalesView.collection("AddSalesData").whereEqualTo("date", date_pick).whereEqualTo("name", text);
            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    StringBuilder builder = new StringBuilder();
                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(activity_sales_view.this, "no data found", Toast.LENGTH_SHORT).show();
                        card_view_sales.setVisibility(View.GONE);
                    } else {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            document_id = documentSnapshot.getId();
                            model_class_sales_add data_sales_view = documentSnapshot.toObject(model_class_sales_add.class);
                            String product_name = data_sales_view.getName();
                            card_view_sales.setVisibility(View.VISIBLE);
                            data_sales_name.setText(product_name);

                            String product_id = data_sales_view.getID();
                            data_sales_id.setText(product_id);

                            int product_qty = data_sales_view.getQuantity();
                            data_sales_qty.setText(String.valueOf(product_qty));

                            int product_per_price = data_sales_view.getPer_price();
                            data_sales_per_price.setText(String.valueOf(product_per_price));

                            int product_total_price = data_sales_view.getTotal_Price();
                            data_sales_total_price.setText(String.valueOf(product_total_price));


                            String product_date = data_sales_view.getDate();
                            data_sales_date.setText(product_date);


                            String product_description = data_sales_view.getDescription();
                            data_sales_description.setText(product_description);

                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(activity_sales_view.this, "Enter Both Details", Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void delete_data (String id)
        {
            fSalesView.collection("AddSalesData").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(activity_sales_view.this, "Doucment Deleted", Toast.LENGTH_SHORT).show();
                    card_view_sales.setVisibility(View.GONE);
                }
            });
        }
        public void update_data(String id)
        {
            fSalesView.collection("AddSalesData").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Intent update = new Intent(activity_sales_view.this,update_sales_details.class);
                    String document_id = id.toString().trim();
                    String name = data_sales_name.getText().toString().trim();
                    String sales_id = data_sales_id.getText().toString();
                    int qty = Integer.parseInt(data_sales_qty.getText().toString());
                    int per_price = Integer.parseInt(data_sales_per_price.getText().toString());
                    int total_price = Integer.parseInt(data_sales_total_price.getText().toString().trim());
                    String date = data_sales_date.getText().toString().trim();
                    String description = data_sales_description.getText().toString().trim();
                    update.putExtra("doucmentid",document_id);
                    update.putExtra("salesname",name);
                    update.putExtra("salesid",sales_id);
                    update.putExtra("sales_qty",qty);
                    update.putExtra("sales_per_price",per_price);
                    update.putExtra("sales_total_price",total_price);
                    update.putExtra("salesdate",date);
                    update.putExtra("salesdescription",description);
                    startActivity(update);
                }
            });
        }

    /*private void getdata() {
        db.collection("AddSalesData").document("n9CjsQl75Uc5JyRVmgKL").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                model_class_purchase_add_data data_sales_view = documentSnapshot.toObject(model_class_purchase_add_data.class);

                String sales_name = data_sales_view.getsales_name();
                data_name.setText(sales_name);

                String sales_id= data_sales_view.getsales_id();
                data_id.setText(sales_id);

                int sales_qty = data_sales_view.getsales_qty();
                data_qty.setText(String.valueOf(sales_qty));

                int sales_per_price = data_sales_view.getsales_per_price();
                data_per_price.setText(String.valueOf(sales_per_price));

                String sales_date= data_sales_view.getsales_date();
                data_date.setText(sales_date);

                int sales_total_price = data_sales_view.getsales_per_price();
                data_total_price.setText(String.valueOf(sales_total_price));

                String sales_description= data_sales_view.getsales_description();
                data_description.setText(sales_description);
            }
        });
    }*/
}