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

public class sales_expense_reports extends AppCompatActivity {
    TextView select_sales_expense_reports_date, data_sales_expense_reports_name, data_sales_expense_reports_date, data_sales_expense_reports_total_price, data_sales_expense_reports_description;
    Button search_button_sales_expense_reports, select_date_button_sales_expense_reports, delete_data_button_sales_expense_reports, update_data_button_sales_expense_reports;
    Spinner spinner_list_name_sales_expense_reports;
    MaterialCardView card_view_sales_expense_reports;
    DatePickerDialog datePickerDialog;
    FirebaseFirestore fS_Expense;
    String document_id;
    ArrayList<String> values_PExpenses = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_expense_reports);

        data_sales_expense_reports_name = findViewById(R.id.data_sales_expense_reports_name);
        data_sales_expense_reports_date = findViewById(R.id.data_sales_expense_reports_date);
        data_sales_expense_reports_total_price = findViewById(R.id.data_sales_expense_reports_total_price);
        data_sales_expense_reports_description = findViewById(R.id.data_sales_expense_reports_description);

        select_sales_expense_reports_date = findViewById(R.id.sales_expense_reports_select_date);
        select_date_button_sales_expense_reports = findViewById(R.id.sales_expense_reports_select_date_button);
        spinner_list_name_sales_expense_reports = findViewById(R.id.spinner_add_sales_expense_reports);
        search_button_sales_expense_reports = findViewById(R.id.search_button_sales_expense_reports);

        delete_data_button_sales_expense_reports = findViewById(R.id.delete_sales_expense_reports_data_button);
        update_data_button_sales_expense_reports = findViewById(R.id.update_sales_expense_reports_button);
        card_view_sales_expense_reports = findViewById(R.id.card_sales_expense_reports);

        fS_Expense = FirebaseFirestore.getInstance();


        // select Date Dialog Box
        select_sales_expense_reports_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(sales_expense_reports.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                select_sales_expense_reports_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        select_date_button_sales_expense_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date_pick = select_sales_expense_reports_date.getText().toString().trim();
                Query query = fS_Expense.collection("salesExpenses").whereEqualTo("date", date_pick);
                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                model_class_sales_expenses sales_expense_model = documentSnapshot.toObject(model_class_sales_expenses.class);
                                String expense_name_sales = sales_expense_model.getName();
                                values_PExpenses.add(expense_name_sales);
                                ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<String>(sales_expense_reports.this, android.R.layout.simple_spinner_dropdown_item, values_PExpenses);
                                spinner_list_name_sales_expense_reports.setAdapter(SpinnerAdapter);
                                search_button_sales_expense_reports.setVisibility(View.VISIBLE);
                            }
                        } else {
                            card_view_sales_expense_reports.setVisibility(View.GONE);
                            search_button_sales_expense_reports.setVisibility(View.GONE);
                            spinner_list_name_sales_expense_reports.setAdapter(null);
                            Toast.makeText(sales_expense_reports.this, "No Data Found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        search_button_sales_expense_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                querydata();

                String date_pick = select_sales_expense_reports_date.getText().toString().trim();

                Query rquery = fS_Expense.collection("salesExpenses").whereEqualTo("date", date_pick);
                rquery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                model_class_sales_expenses sales_expense_reports = documentSnapshot.toObject(model_class_sales_expenses.class);
                                String sales_expense_name = sales_expense_reports.getName();
                                values_PExpenses.remove(sales_expense_name);
                                ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<String>(sales_expense_reports.this, android.R.layout.simple_spinner_dropdown_item, values_PExpenses);
                                spinner_list_name_sales_expense_reports.setAdapter(SpinnerAdapter);
                                search_button_sales_expense_reports.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(sales_expense_reports.this, "No Data Found Button", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        delete_data_button_sales_expense_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_data(document_id);
            }
        });


        update_data_button_sales_expense_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_data(document_id);
            }
        });
    }
    public void querydata(){
        String date_pick = select_sales_expense_reports_date.getText().toString().trim();
        String text = spinner_list_name_sales_expense_reports.getSelectedItem().toString();

        Query query = fS_Expense.collection("salesExpenses").whereEqualTo("date", date_pick).whereEqualTo("name", text);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                StringBuilder builder = new StringBuilder();
                if (queryDocumentSnapshots.isEmpty()) {
                    //Toast.makeText(sales_expense_reports.this, "no data found", Toast.LENGTH_SHORT).show();
                    card_view_sales_expense_reports.setVisibility(View.GONE);
                } else {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        document_id = documentSnapshot.getId();
                        model_class_sales_expenses sales_expense = documentSnapshot.toObject(model_class_sales_expenses.class);
                        card_view_sales_expense_reports.setVisibility(View.VISIBLE);

                        String product_name = sales_expense.getName();
                        data_sales_expense_reports_name.setText(product_name);

                        String product_date = sales_expense.getDate();
                        data_sales_expense_reports_date.setText(product_date);

                        int product_total_price = sales_expense.getTotal_Price();
                        data_sales_expense_reports_total_price.setText(String.valueOf(product_total_price));


                        String product_description = sales_expense.getDescription();
                        data_sales_expense_reports_description.setText(product_description);

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(sales_expense_reports.this, "Enter Both Details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void delete_data (String id)
    {
        fS_Expense.collection("salesExpenses").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(sales_expense_reports.this, "Doucment Deleted", Toast.LENGTH_SHORT).show();
                card_view_sales_expense_reports.setVisibility(View.GONE);
            }
        });
    }
    public void update_data(String id) {
        fS_Expense.collection("salesExpenses").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Intent update = new Intent(sales_expense_reports.this, update_sales_expense.class);
                String document_id = id;

                String name = data_sales_expense_reports_name.getText().toString().trim();
                int total_price = Integer.parseInt(data_sales_expense_reports_total_price.getText().toString().trim());
                String description = data_sales_expense_reports_description.getText().toString().trim();

                update.putExtra("doucmentid", document_id);
                update.putExtra("s_expense_name", name);
                update.putExtra("s_expense_total_price", total_price);
                update.putExtra("s_expense_description", description);
                startActivity(update);
            }
        });
    }
}
