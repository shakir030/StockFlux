package com.example.stockflux;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

public class activity_supplier extends AppCompatActivity {
    public static final String TAG = "AddSuppliersData";
    TextInputEditText supplier_name,supplier_organisation,supplier_email,supplier_number,supplier_address,supplier_city,supplier_state,supplier_zipcode;
    Button submit_button,reset_button;
    FirebaseFirestore fAddsupplier;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);
        setTitle("Add Supplier's Details");

        supplier_name = findViewById(R.id.supplier_fullname);
        supplier_organisation = findViewById(R.id.supplier_organisation);
        supplier_email = findViewById(R.id.supplier_email);
        supplier_number = findViewById(R.id.supplier_number);
        supplier_address = findViewById(R.id.supplier_address);
        supplier_city = findViewById(R.id.supplier_city);
        supplier_state = findViewById(R.id.supplier_state);
        supplier_zipcode = findViewById(R.id.supplier_zipcode);
        fAddsupplier = FirebaseFirestore.getInstance();

        reset_button = findViewById(R.id.supplier_reset_button);
        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supplier_name.setText(null);
                supplier_organisation.setText(null);
                supplier_email.setText(null);
                supplier_number.setText(null);
                supplier_address.setText(null);
                supplier_city.setText(null);
                supplier_state.setText(null);
                supplier_zipcode.setText(null);
            }
        });

        submit_button = findViewById(R.id.supplier_submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = supplier_email.getText().toString().trim();
                String name = supplier_name.getText().toString().trim();
                String number = supplier_number.getText().toString().trim();

                if(name.isEmpty()){
                    supplier_name.setError("Enter Name ");
                    supplier_name.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    supplier_email.setError("Enter Correct Email");
                    supplier_email.requestFocus();
                    return;
                }
                if (number.isEmpty()){
                    supplier_number.setError("Enter Number");
                    supplier_number.requestFocus();
                    return;
                }
                if(number.length() != 10){
                    supplier_number.setError("Enter 10 Digits Number");
                    supplier_number.requestFocus();
                    return;
                }
                AddSupplier(name,email);

            }
        });

    }
    public void AddSupplier(String name,String email){
        model_supplier_add supplier_add = new model_supplier_add();
        name = supplier_name.getText().toString().trim();
        String organisation = supplier_organisation.getText().toString().trim();
        email = supplier_email.getText().toString().trim();
        String address = supplier_address.getText().toString().trim();
        String city = supplier_city.getText().toString().trim();
        String state = supplier_state.getText().toString().trim();

        int number = Integer.parseInt(supplier_number.getText().toString().trim());
        int zipcode = Integer.parseInt(supplier_zipcode.getText().toString().trim());

        supplier_add.setName(name);
        supplier_add.setOrganisation(organisation);
        supplier_add.setEmail(email);
        supplier_add.setNumber(number);
        supplier_add.setAddress(address);
        supplier_add.setCity(city);
        supplier_add.setState(state);
        supplier_add.setZipcode(zipcode);

        fAddsupplier = FirebaseFirestore.getInstance();
        fAddsupplier.collection("SuppliersData").document().set(supplier_add).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(activity_supplier.this, "Data added successfully", Toast.LENGTH_LONG).show();
                supplier_name.setText(null);
                supplier_organisation.setText(null);
                supplier_email.setText(null);
                supplier_number.setText(null);
                supplier_address.setText(null);
                supplier_city.setText(null);
                supplier_state.setText(null);
                supplier_zipcode.setText(null);
                supplier_name.requestFocus();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"onFailure: "+e.getMessage());
                Toast.makeText(activity_supplier.this, "Failed : Error is "+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}