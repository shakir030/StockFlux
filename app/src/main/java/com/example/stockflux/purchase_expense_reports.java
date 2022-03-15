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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class purchase_expense_reports extends AppCompatActivity {
    TextView select_purchase_expense_reports_date, data_purchase_expense_reports_name, data_purchase_expense_reports_date, data_purchase_expense_reports_total_price, data_purchase_expense_reports_description;
    Button search_button_purchase_expense_reports, select_date_button_purchase_expense_reports, delete_data_button_purchase_expense_reports, update_data_button_purchase_expense_reports;
    Spinner spinner_list_name_purchase_expense_reports;
    MaterialCardView card_view_purchase_expense_reports;
    DatePickerDialog datePickerDialog;
    FirebaseFirestore fPExpense;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String user_id = fAuth.getCurrentUser().getUid();
    String document_id;
    ArrayList<String> values_PExpenses = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_expense_reports);

        data_purchase_expense_reports_name = findViewById(R.id.data_purchase_expense_reports_name);
        data_purchase_expense_reports_date = findViewById(R.id.data_purchase_expense_reports_date);
        data_purchase_expense_reports_total_price = findViewById(R.id.data_purchase_expense_reports_total_price);
        data_purchase_expense_reports_description = findViewById(R.id.data_purchase_expense_reports_description);

        select_purchase_expense_reports_date = findViewById(R.id.purchase_expense_reports_select_date);
        select_date_button_purchase_expense_reports = findViewById(R.id.purchase_expense_reports_select_date_button);
        spinner_list_name_purchase_expense_reports = findViewById(R.id.spinner_add_purchase_expense_reports);
        search_button_purchase_expense_reports = findViewById(R.id.search_button_purchase_expense_reports);

        delete_data_button_purchase_expense_reports = findViewById(R.id.delete_purchase_expense_reports_data_button);
        update_data_button_purchase_expense_reports = findViewById(R.id.update_purchase_expense_reports_button);
        card_view_purchase_expense_reports = findViewById(R.id.card_purchase_expense_reports);

        fPExpense = FirebaseFirestore.getInstance();


        // select Date Dialog Box
        select_purchase_expense_reports_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(purchase_expense_reports.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                select_purchase_expense_reports_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        select_date_button_purchase_expense_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date_pick = select_purchase_expense_reports_date.getText().toString().trim();
                Query query = fPExpense.collection("Users").document(user_id).collection("purchaseExpenses").whereEqualTo("purchase_expense_date", date_pick);
                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                model_class_purchase_expense purchase_expense_model = documentSnapshot.toObject(model_class_purchase_expense.class);
                                String expense_name_purchase = purchase_expense_model.getPurchase_expense_name();
                                values_PExpenses.add(expense_name_purchase);
                                ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<String>(purchase_expense_reports.this, android.R.layout.simple_spinner_dropdown_item, values_PExpenses);
                                spinner_list_name_purchase_expense_reports.setAdapter(SpinnerAdapter);
                                search_button_purchase_expense_reports.setVisibility(View.VISIBLE);
                            }
                        } else {
                            card_view_purchase_expense_reports.setVisibility(View.GONE);
                            search_button_purchase_expense_reports.setVisibility(View.GONE);
                            spinner_list_name_purchase_expense_reports.setAdapter(null);
                            Toast.makeText(purchase_expense_reports.this, "No Data Found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        search_button_purchase_expense_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                querydata();

                String date_pick = select_purchase_expense_reports_date.getText().toString().trim();

                Query rquery = fPExpense.collection("Users").document(user_id).collection("purchaseExpenses").whereEqualTo("purchase_expense_date", date_pick);
                rquery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                               model_class_purchase_expense purchase_expense_reports = documentSnapshot.toObject(model_class_purchase_expense.class);
                                String purchase_expense_name = purchase_expense_reports.getPurchase_expense_name();
                                values_PExpenses.remove(purchase_expense_name);
                                ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<String>(purchase_expense_reports.this, android.R.layout.simple_spinner_dropdown_item, values_PExpenses);
                                spinner_list_name_purchase_expense_reports.setAdapter(SpinnerAdapter);
                                search_button_purchase_expense_reports.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(purchase_expense_reports.this, "No Data Found Button", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        delete_data_button_purchase_expense_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_data(document_id);
            }
        });


        update_data_button_purchase_expense_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_data(document_id);
            }
        });
    }
    public void querydata(){
        String date_pick = select_purchase_expense_reports_date.getText().toString().trim();
        String text = spinner_list_name_purchase_expense_reports.getSelectedItem().toString();

        Query query = fPExpense.collection("Users").document(user_id).collection("purchaseExpenses").whereEqualTo("purchase_expense_date", date_pick).whereEqualTo("purchase_expense_name", text);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                StringBuilder builder = new StringBuilder();
                if (queryDocumentSnapshots.isEmpty()) {
                    //Toast.makeText(purchase_expense_reports.this, "no data found", Toast.LENGTH_SHORT).show();
                    card_view_purchase_expense_reports.setVisibility(View.GONE);
                } else {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        document_id = documentSnapshot.getId();
                        model_class_purchase_expense purchase_expense = documentSnapshot.toObject(model_class_purchase_expense.class);
                        card_view_purchase_expense_reports.setVisibility(View.VISIBLE);

                        String product_name = purchase_expense.getPurchase_expense_name();
                        data_purchase_expense_reports_name.setText(product_name);

                        String product_date = purchase_expense.getPurchase_expense_date();
                        data_purchase_expense_reports_date.setText(product_date);

                        int product_total_price = purchase_expense.getPurchase_expense_total_price();
                        data_purchase_expense_reports_total_price.setText(String.valueOf(product_total_price));


                        String product_description = purchase_expense.getPurchase_expense_description();
                        data_purchase_expense_reports_description.setText(product_description);

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(purchase_expense_reports.this, "Enter Both Details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void delete_data (String id)
    {
        fPExpense.collection("Users").document(user_id).collection("purchaseExpenses").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(purchase_expense_reports.this, "Doucment Deleted", Toast.LENGTH_SHORT).show();
                card_view_purchase_expense_reports.setVisibility(View.GONE);
            }
        });
    }
    public void update_data(String id) {
        fPExpense.collection("Users").document(user_id).collection("purchaseExpenses").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Intent update = new Intent(purchase_expense_reports.this, update_purchase_expense.class);
                String document_id = id;

                String name = data_purchase_expense_reports_name.getText().toString().trim();
                int total_price = Integer.parseInt(data_purchase_expense_reports_total_price.getText().toString().trim());
                String description = data_purchase_expense_reports_description.getText().toString().trim();

                update.putExtra("doucmentid", document_id);
                update.putExtra("p_expense_name", name);
                update.putExtra("p_expense_total_price", total_price);
                update.putExtra("p_expense_description", description);
                startActivity(update);
            }
        });
    }
}
