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

public class activity_expense_sales extends AppCompatActivity {
    public static final String TAG = "AddSalesExpenses";
    TextInputEditText sales_expense_name, sales_expense_total_price, sales_expense_description;
    TextView sales_expense_date;
    Button sales_expense_submit, sales_expense_reset;
    DatePickerDialog datePickerDialog;
    FirebaseFirestore fSalesExpenses;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String user_id = fAuth.getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_sales);

        sales_expense_date = findViewById(R.id.expense_sales_date);
        sales_expense_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(activity_expense_sales.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                sales_expense_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        sales_expense_name = findViewById(R.id.expense_sales_name);
        sales_expense_total_price = findViewById(R.id.expense_sales_total_price);
        sales_expense_description = findViewById(R.id.expense_sales_description);

        sales_expense_reset = findViewById(R.id.expense_sales_reset_button);
        sales_expense_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sales_expense_name.setText(null);
                sales_expense_total_price.setText(null);
                sales_expense_description.setText(null);
            }
        });

        sales_expense_submit = findViewById(R.id.expense_sales_add_button);
        sales_expense_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String expense_sales_name = sales_expense_name.getText().toString().trim();
                String expense_sales_date = sales_expense_date.getText().toString().trim();
                String expense_sales_total_price = sales_expense_total_price.getText().toString().trim();

                if (expense_sales_name.isEmpty()) {
                    sales_expense_name.setError("Enter Name of Expense");
                    sales_expense_name.requestFocus();
                    return;
                }
                if (expense_sales_date.isEmpty()) {
                    sales_expense_date.setError("Enter Date");
                    sales_expense_date.requestFocus();
                    return;
                }
                if (expense_sales_total_price.isEmpty()) {
                    sales_expense_total_price.setError("Enter Total Amount");
                    sales_expense_total_price.requestFocus();
                    return;
                }
                fSalesExpenses = FirebaseFirestore.getInstance();

                Query query = fSalesExpenses.collection("Users").document(user_id).collection("salesExpenses").whereEqualTo("date", expense_sales_date).whereEqualTo("name", expense_sales_name);
                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            sales_expense_name.setError("Enter Another Name");
                            sales_expense_name.requestFocus();
                            return;
                        } else {
                            AddExpenseData(expense_sales_name);
                        }
                    }
                });
            }
        });
    }

    public void AddExpenseData(String expense_sales_name) {
        model_class_sales_expenses add_sales_expense = new model_class_sales_expenses();
        expense_sales_name = sales_expense_name.getText().toString().trim();
        String expense_date = sales_expense_date.getText().toString().trim();
        String expense_description = sales_expense_description.getText().toString().trim();
        int expense_total_price = Integer.parseInt(sales_expense_total_price.getText().toString().trim());

        add_sales_expense.setName(expense_sales_name);
        add_sales_expense.setDate(expense_date);
        add_sales_expense.setTotal_Price(expense_total_price);
        add_sales_expense.setDescription(expense_description);


        fSalesExpenses.collection("Users").document(user_id).collection("salesExpenses").document().set(add_sales_expense).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(activity_expense_sales.this, "Data added successfully", Toast.LENGTH_LONG).show();
                sales_expense_name.setText(null);
                sales_expense_date.setText(null);
                sales_expense_total_price.setText(null);
                sales_expense_description.setText(null);
                sales_expense_name.requestFocus();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: " + e.getMessage());
                Toast.makeText(activity_expense_sales.this, "Failed : Error is " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}