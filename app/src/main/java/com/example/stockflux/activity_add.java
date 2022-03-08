package com.example.stockflux;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class activity_add extends AppCompatActivity {
    public static final String TAG = "AddPurchase";
    private TextInputEditText product_add_name,product_add_id,product_add_qty,product_add_per_price,product_add_total_price,product_add_description;
    private TextView product_add_date ;
    private Button add_purchase_submit_button,add_purchase_reset_button;
    private DatePickerDialog datePickerDialog;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        db = FirebaseFirestore.getInstance();

        product_add_name = findViewById(R.id.purchase_add_name);
        product_add_id = findViewById(R.id.purchase_add_product_id);
        product_add_qty = findViewById(R.id.purchase_add_qty);
        product_add_per_price = findViewById(R.id.purchase_add_per_price);
        product_add_date = findViewById(R.id.purchase_add_date);
        product_add_total_price = findViewById(R.id.purchase_add_totalprice);
        product_add_description = findViewById(R.id.purchase_add_description);

        add_purchase_reset_button = findViewById(R.id.purchase_add_reset_button);
        //clear the values entered in textfield
        add_purchase_reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                product_add_name.setText(null);
                product_add_id.setText(null);
                product_add_qty.setText(null);
                product_add_per_price.setText(null);
                product_add_date.setText(null);
                product_add_total_price.setText(null);
                product_add_description.setText(null);
            }
        });

        add_purchase_submit_button = findViewById(R.id.purchase_add_submit_button);
        add_purchase_submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validating no empty textfield

                String purchase_add_name = product_add_name.getText().toString().trim();
                String purchase_add_id = product_add_id.getText().toString().trim();
                String purchase_add_qty = product_add_qty.getText().toString().trim();
                String purchase_add_per_price = product_add_per_price.getText().toString().trim();
                String purchase_add_date = product_add_date.getText().toString().trim();

                if (purchase_add_name.isEmpty()) {
                    product_add_name.setError("Enter Name !!");
                    product_add_name.requestFocus();
                    return;
                }
                if (purchase_add_id.isEmpty()) {
                    product_add_id.setError("Enter ID !!");
                    product_add_id.requestFocus();
                    return;
                }
                if (purchase_add_qty.isEmpty()) {
                    product_add_qty.setError("Enter Quantity !!");
                    product_add_qty.requestFocus();
                    return;
                }
                if (purchase_add_per_price.isEmpty()) {
                    product_add_per_price.setError("Enter Per Price !!");
                    product_add_per_price.requestFocus();
                    return;
                }
                if (purchase_add_date.isEmpty()) {
                    product_add_date.setError("Enter Date !!");
                    product_add_date.requestFocus();
                }

                //checking name is already presented in same date ..

                Query idquery = db.collection("addProducts").whereEqualTo("product_id",purchase_add_id);
                idquery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            product_add_id.setError("Enter Another ID");
                            product_add_id.requestFocus();
                        }
                        else {
                            String date_pick = product_add_date.getText().toString().trim();
                            Query nquery = db.collection("addProducts").whereEqualTo("product_date", date_pick).whereEqualTo("product_name", purchase_add_name);
                            nquery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        product_add_name.setError("Enter another name");
                                        product_add_name.requestFocus();
                                    } else {
                                        AddData(); //adds data to firestore databasee
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
                datePickerDialog = new DatePickerDialog(activity_add.this,
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
    private void AddData()
    {

        model_class_purchase_add_data add_data = new model_class_purchase_add_data();
        String purchase_add_name = product_add_name.getText().toString().trim();
        String purchase_add_id = product_add_id.getText().toString().trim();
        String purchase_add_date = product_add_date.getText().toString().trim();
        String purchase_add_description = product_add_description.getText().toString().trim();
        int purchase_add_qty = Integer.parseInt(product_add_qty.getText().toString());
        int purchase_add_per_price = Integer.parseInt(product_add_per_price.getText().toString());
        int purchase_add_total_price = add_data.getProduct_total_price();

        add_data.setProduct_name(purchase_add_name);
        add_data.setProduct_id(purchase_add_id);
        add_data.setProduct_qty(purchase_add_qty);
        add_data.setProduct_per_price(purchase_add_per_price);
        add_data.setDate(purchase_add_date);
        add_data.setProduct_total_price(purchase_add_total_price);
        add_data.setProduct_description(purchase_add_description);

        db.collection("addProducts").document().set(add_data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity_add.this, "Data added successfully", Toast.LENGTH_LONG).show();
                product_add_name.setText(null);
                product_add_id.setText(null);
                product_add_qty.setText(null);
                product_add_per_price.setText(null);
                product_add_date.setText(null);
                product_add_total_price.setText(null);
                product_add_description.setText(null);
                product_add_name.requestFocus();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"onFailure: "+e.getMessage());
                Toast.makeText(activity_add.this, "Failed : Error is "+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}