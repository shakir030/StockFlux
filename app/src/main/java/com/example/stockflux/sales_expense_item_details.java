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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class sales_expense_item_details extends AppCompatActivity {
    TextView data_sales_expense_name, data_sales_expense_price, data_sales_expense_date, data_sales_expense_description;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String user_id = fAuth.getCurrentUser().getUid();
    String document_id;
    Date date;
    Button delete_button_sales_expense, update_button_sales_expense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_expense_item_details);

        data_sales_expense_name = findViewById(R.id.data_sales_expense_name);
        data_sales_expense_price = findViewById(R.id.data_sales_expense_price);
        data_sales_expense_date = findViewById(R.id.data_sales_expense_date);
        data_sales_expense_description = findViewById(R.id.data_sales_expense_description);
        delete_button_sales_expense = findViewById(R.id.delete_data_sales_expense_button);
        update_button_sales_expense = findViewById(R.id.update_data_sales_expense_button);

        String expense_name = getIntent().getStringExtra("expense_name");
        String expense_date = getIntent().getStringExtra("expense_date");
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(expense_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Query query = db.collection("Users").document(user_id).collection("salesExpenses").whereEqualTo("name", expense_name).whereEqualTo("date", date);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    Toast.makeText(sales_expense_item_details.this, "Data is Empty", Toast.LENGTH_SHORT).show();
                } else {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        model_class_sales_expenses model_s_expense = documentSnapshot.toObject(model_class_sales_expenses.class);
                        document_id = documentSnapshot.getId();

                        data_sales_expense_name.setText(model_s_expense.getName());
                        data_sales_expense_price.setText(String.valueOf(model_s_expense.getTotal_Price()));

                        Date date = model_s_expense.getDate();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        String date_string = formatter.format(date);

                        data_sales_expense_date.setText(date_string);
                        data_sales_expense_description.setText(model_s_expense.getDescription());

                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "onFailure: " + e.getMessage());
                Toast.makeText(sales_expense_item_details.this, "OnFailure :- " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        delete_button_sales_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Users").document(user_id).collection("salesExpenses").document(document_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(sales_expense_item_details.this, "Data Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(sales_expense_item_details.this, sales_expense_reports.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "onFailure: " + e.getMessage());
                        Toast.makeText(sales_expense_item_details.this, "OnFailure :- " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        update_button_sales_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String expense_name = data_sales_expense_name.getText().toString().trim();
                int expense_price = Integer.parseInt(data_sales_expense_price.getText().toString());
                String expense_description = data_sales_expense_description.getText().toString().trim();

                Intent update_sales_expense = new Intent(sales_expense_item_details.this, update_sales_expense.class);
                update_sales_expense.putExtra("doucmentid", document_id);
                update_sales_expense.putExtra("s_expense_name", expense_name);
                update_sales_expense.putExtra("s_expense_total_price", expense_price);
                update_sales_expense.putExtra("s_expense_description", expense_description);
                startActivity(update_sales_expense);

            }
        });


    }
}