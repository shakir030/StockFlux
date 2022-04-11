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

public class purchase_expense_item_details extends AppCompatActivity {
    TextView data_purchase_expense_name,data_purchase_expense_price,data_purchase_expense_date,data_purchase_expense_description;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String user_id = fAuth.getCurrentUser().getUid();
    String document_id;
    Date date;
    Button delete_button_purchase_expense,update_button_purchase_expense;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_expense_item_details);

        data_purchase_expense_name = findViewById(R.id.data_purchase_expense_name);
        data_purchase_expense_price = findViewById(R.id.data_purchase_expense_price);
        data_purchase_expense_date = findViewById(R.id.data_purchase_expense_date);
        data_purchase_expense_description = findViewById(R.id.data_purchase_expense_description);
        delete_button_purchase_expense = findViewById(R.id.delete_data_purchase_expense_button);
        update_button_purchase_expense = findViewById(R.id.update_data_purchase_expense_button);

        String expense_name = getIntent().getStringExtra("expense_name");
        String expense_date = getIntent().getStringExtra("expense_date");

        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(expense_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Query query = db.collection("Users").document(user_id).collection("purchaseExpenses").whereEqualTo("purchase_expense_name",expense_name).whereEqualTo("purchase_expense_date",date);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            String purchased_expense_date;
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()){
                    Toast.makeText(purchase_expense_item_details.this, "Data is Empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        model_class_purchase_expense model_p_expense = documentSnapshot.toObject(model_class_purchase_expense.class);
                        document_id = documentSnapshot.getId();

                        data_purchase_expense_name.setText(model_p_expense.purchase_expense_name);
                        data_purchase_expense_price.setText(String.valueOf(model_p_expense.purchase_expense_total_price));

                        Date date = model_p_expense.getPurchase_expense_date();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        purchased_expense_date = formatter.format(date);

                        data_purchase_expense_date.setText(purchased_expense_date);
                        data_purchase_expense_description.setText(model_p_expense.purchase_expense_description);

                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e){
                Log.e("TAG", "onFailure: "+e.getMessage() );
                Toast.makeText(purchase_expense_item_details.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        delete_button_purchase_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Users").document(user_id).collection("purchaseExpenses").document(document_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(purchase_expense_item_details.this, "Data Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(purchase_expense_item_details.this,purchase_expense_reports.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "onFailure: "+e.getMessage() );
                        Toast.makeText(purchase_expense_item_details.this, "OnFailure :- "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        update_button_purchase_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String expense_name = data_purchase_expense_name.getText().toString().trim();
                int expnese_price = Integer.parseInt(data_purchase_expense_price.getText().toString());
                String expense_description = data_purchase_expense_description.getText().toString().trim();

                Intent update_purchase_expense = new Intent(purchase_expense_item_details.this, com.example.stockflux.update_purchase_expense.class);
                update_purchase_expense.putExtra("doucmentid", document_id);
                update_purchase_expense.putExtra("p_expense_name", expense_name);
                update_purchase_expense.putExtra("p_expense_total_price", expnese_price);
                update_purchase_expense.putExtra("p_expense_description", expense_description);
                startActivity(update_purchase_expense);

            }
        });


    }
}