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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class activity_expense extends AppCompatActivity {
    public static final String TAG = "AddPurchaseExpenses";
    TextInputEditText purchase_expense_name,purchase_expense_total_price,purchase_expense_description;
    TextView purchase_expense_date;
    Button purchase_expense_submit,purchase_expense_reset;
    DatePickerDialog datePickerDialog;
    FirebaseFirestore fAddExpense;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String user_id = fAuth.getCurrentUser().getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        // Select Date
        purchase_expense_date = findViewById(R.id.expense_purchase_date);
        purchase_expense_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(activity_expense.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                purchase_expense_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        purchase_expense_name = findViewById(R.id.expense_purchase_name);
        purchase_expense_total_price = findViewById(R.id.expense_purchase_total_price);
        purchase_expense_description = findViewById(R.id.expense_purchase_description);

        purchase_expense_reset = findViewById(R.id.expense_purchase_reset_button);
        purchase_expense_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                purchase_expense_name.setText(null);
                purchase_expense_total_price.setText(null);
                purchase_expense_description.setText(null);
            }
        });

        purchase_expense_submit = findViewById(R.id.expense_purchase_add_button);
        purchase_expense_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String expense_name = purchase_expense_name.getText().toString().trim();
                String expense_date = purchase_expense_date.getText().toString().trim();
                String expense_total_price = purchase_expense_total_price.getText().toString().trim();

                if (expense_name.isEmpty()) {
                    purchase_expense_name.setError("Enter Name of Expense");
                    purchase_expense_name.requestFocus();
                    return;
                }
                if (expense_date.isEmpty()) {
                    purchase_expense_date.setError("Enter Date");
                    purchase_expense_date.requestFocus();
                    return;
                }
                if (expense_total_price.isEmpty()) {
                    purchase_expense_total_price.setError("Enter Total Amount");
                    purchase_expense_total_price.requestFocus();
                }

                fAddExpense = FirebaseFirestore.getInstance();

                Query query = fAddExpense.collection("Users").document(user_id).collection("purchaseExpenses").whereEqualTo("purchase_expense_date", expense_date).whereEqualTo("purchase_expense_name",expense_name);
                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            purchase_expense_name.setError("Enter Another Name");
                            purchase_expense_name.requestFocus();
                        } else {
                            AddExpenseData(expense_name);
                        }
                    }
                });
            }
        });
    }
    public void AddExpenseData(String expense_name){
        model_class_purchase_expense add_data = new model_class_purchase_expense();
        expense_name = purchase_expense_name.getText().toString().trim();
        String expense_date = purchase_expense_date.getText().toString().trim();
        String expense_description = purchase_expense_description.getText().toString().trim();
        int expense_total_price = Integer.parseInt(purchase_expense_total_price.getText().toString().trim());

        add_data.setPurchase_expense_name(expense_name);
        add_data.setPurchase_expense_date(expense_date);
        add_data.setPurchase_expense_total_price(expense_total_price);
        add_data.setPurchase_expense_description(expense_description);


        fAddExpense.collection("Users").document(user_id).collection("purchaseExpenses").document().set(add_data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(activity_expense.this, "Data added successfully", Toast.LENGTH_LONG).show();
                purchase_expense_name.setText(null);
                purchase_expense_date.setText(null);
                purchase_expense_total_price.setText(null);
                purchase_expense_description.setText(null);
                purchase_expense_name.requestFocus();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"onFailure: "+e.getMessage());
                Toast.makeText(activity_expense.this, "Failed : Error is "+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}