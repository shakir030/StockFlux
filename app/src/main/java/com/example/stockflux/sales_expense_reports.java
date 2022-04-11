package com.example.stockflux;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class sales_expense_reports extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<model_class_sales_expenses> sales_expense_list;
    TextInputEditText s_expense_from_date,s_expense_to_date;
    TextView s_expense_range_heading;
    Button s_expense_submit_range_date;
    DatePickerDialog datePickerDialog;
    LinearLayout s_expense_from_linear,s_expense_to_linear;
    sales_expense_adapter sales_expense_adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String user_id = fAuth.getCurrentUser().getUid();
    ProgressDialog progressDialog;
    Date s_from_date,s_to_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_expense_reports);

        s_expense_from_date = findViewById(R.id.s_expense_from_date_data);
        s_expense_range_heading = findViewById(R.id.range_heading_expense_sales);
        s_expense_to_date = findViewById(R.id.s_expense_to_date_data);
        s_expense_submit_range_date = findViewById(R.id.s_expense_submit_range_date);
        s_expense_from_linear = findViewById(R.id.from_s_expense_linear);
        s_expense_to_linear = findViewById(R.id.to_s_expense_linear);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data ..");
        progressDialog.show();


        recyclerView = findViewById(R.id.sales_expense_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sales_expense_list = new ArrayList<model_class_sales_expenses>();


        EventChangeListner();



    }

    private void EventChangeListner() {
        db.collection("Users").document(user_id).collection("salesExpenses").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(sales_expense_reports.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }{
                }

                if(error != null){

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    Log.e("TAG", "Database Error "+ error.getMessage());
                    Toast.makeText(sales_expense_reports.this,"Database Error "+ error.getMessage() , Toast.LENGTH_SHORT).show();
                    return;

                }
                for (DocumentChange dc :value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        sales_expense_list.add(dc.getDocument().toObject(model_class_sales_expenses.class));
                    }

                    sales_expense_adapter = new sales_expense_adapter(sales_expense_reports.this,sales_expense_list);
                    recyclerView.setAdapter(sales_expense_adapter);
                    sales_expense_adapter.notifyDataSetChanged();

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu,menu);
        MenuItem item=menu.findItem(R.id.search);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Search Here.. !!");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                sales_expense_adapter.getFilter().filter(s);
                return false;
            }
        });
        getMenuInflater().inflate(R.menu.filter_data_expense,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.expense_name_ascending:
                order_by_name_expense_ascending();
                return true;

            case R.id.expense_name_descending:
                order_by_name_expense_descending();
                return true;

            case R.id.expense_price_ascending:
                order_by_price_expense_low_high();
                return true;

            case R.id.expense_price_descending:
                order_by_price_expense_high_low();
                return true;

            case R.id.expense_date_ascending:
                order_by_date_expense_ascending();
                return true;

            case R.id.expense_date_descending:
                order_by_date_expense_descending();
                return true;

            case R.id.expense_date_range:
                order_by_date_expense_range();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Sort By Name Ascending
    private void order_by_name_expense_ascending()
    {
        sales_expense_list.clear();
        s_expense_range_heading.setVisibility(View.GONE);
        s_expense_from_linear.setVisibility(View.GONE);
        s_expense_to_linear.setVisibility(View.GONE);
        s_expense_submit_range_date.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        db.collection("Users").document(user_id).collection("salesExpenses").orderBy("name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(sales_expense_reports.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }{
                }

                if(error != null){

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    Log.e("TAG", "Database Error "+ error.getMessage());
                    Toast.makeText(sales_expense_reports.this,"Database Error "+ error.getMessage() , Toast.LENGTH_SHORT).show();
                    return;

                }
                for (DocumentChange dc :value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        sales_expense_list.add(dc.getDocument().toObject(model_class_sales_expenses.class));
                    }

                    sales_expense_adapter = new sales_expense_adapter(sales_expense_reports.this,sales_expense_list);
                    recyclerView.setAdapter(sales_expense_adapter);
                    sales_expense_adapter.notifyDataSetChanged();

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    //Sort By Name Descending
    private void order_by_name_expense_descending()
    {
        sales_expense_list.clear();
        s_expense_range_heading.setVisibility(View.GONE);
        s_expense_from_linear.setVisibility(View.GONE);
        s_expense_to_linear.setVisibility(View.GONE);
        s_expense_submit_range_date.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        db.collection("Users").document(user_id).collection("salesExpenses").orderBy("name", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(sales_expense_reports.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }{
                }

                if(error != null){

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    Log.e("TAG", "Database Error "+ error.getMessage());
                    Toast.makeText(sales_expense_reports.this,"Database Error "+ error.getMessage() , Toast.LENGTH_SHORT).show();
                    return;

                }
                for (DocumentChange dc :value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        sales_expense_list.add(dc.getDocument().toObject(model_class_sales_expenses.class));
                    }

                    sales_expense_adapter = new sales_expense_adapter(sales_expense_reports.this,sales_expense_list);
                    recyclerView.setAdapter(sales_expense_adapter);
                    sales_expense_adapter.notifyDataSetChanged();

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    //Sort By Price Low to High
    private void order_by_price_expense_low_high()
    {
        sales_expense_list.clear();
        s_expense_range_heading.setVisibility(View.GONE);
        s_expense_from_linear.setVisibility(View.GONE);
        s_expense_to_linear.setVisibility(View.GONE);
        s_expense_submit_range_date.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        db.collection("Users").document(user_id).collection("salesExpenses").orderBy("total_Price", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(sales_expense_reports.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }{
                }

                if(error != null){

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    Log.e("TAG", "Database Error "+ error.getMessage());
                    Toast.makeText(sales_expense_reports.this,"Database Error "+ error.getMessage() , Toast.LENGTH_SHORT).show();
                    return;

                }
                for (DocumentChange dc :value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        sales_expense_list.add(dc.getDocument().toObject(model_class_sales_expenses.class));
                    }

                    sales_expense_adapter = new sales_expense_adapter(sales_expense_reports.this,sales_expense_list);
                    recyclerView.setAdapter(sales_expense_adapter);
                    sales_expense_adapter.notifyDataSetChanged();

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    //Sort By Price High to Low
    private void order_by_price_expense_high_low()
    {
        sales_expense_list.clear();
        s_expense_range_heading.setVisibility(View.GONE);
        s_expense_from_linear.setVisibility(View.GONE);
        s_expense_to_linear.setVisibility(View.GONE);
        s_expense_submit_range_date.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        db.collection("Users").document(user_id).collection("salesExpenses").orderBy("total_Price", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(sales_expense_reports.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }{
                }

                if(error != null){

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    Log.e("TAG", "Database Error "+ error.getMessage());
                    Toast.makeText(sales_expense_reports.this,"Database Error "+ error.getMessage() , Toast.LENGTH_SHORT).show();
                    return;

                }
                for (DocumentChange dc :value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        sales_expense_list.add(dc.getDocument().toObject(model_class_sales_expenses.class));
                    }

                    sales_expense_adapter = new sales_expense_adapter(sales_expense_reports.this,sales_expense_list);
                    recyclerView.setAdapter(sales_expense_adapter);
                    sales_expense_adapter.notifyDataSetChanged();

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    //Sort By Date Ascending
    private void order_by_date_expense_ascending()
    {
        sales_expense_list.clear();
        s_expense_range_heading.setVisibility(View.GONE);
        s_expense_from_linear.setVisibility(View.GONE);
        s_expense_to_linear.setVisibility(View.GONE);
        s_expense_submit_range_date.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        db.collection("Users").document(user_id).collection("salesExpenses").orderBy("date", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(sales_expense_reports.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }{
                }

                if(error != null){

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    Log.e("TAG", "Database Error "+ error.getMessage());
                    Toast.makeText(sales_expense_reports.this,"Database Error "+ error.getMessage() , Toast.LENGTH_SHORT).show();
                    return;

                }
                for (DocumentChange dc :value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        sales_expense_list.add(dc.getDocument().toObject(model_class_sales_expenses.class));
                    }

                    sales_expense_adapter = new sales_expense_adapter(sales_expense_reports.this,sales_expense_list);
                    recyclerView.setAdapter(sales_expense_adapter);
                    sales_expense_adapter.notifyDataSetChanged();

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    //Sort By Date Descending
    private void order_by_date_expense_descending()
    {
        sales_expense_list.clear();
        s_expense_range_heading.setVisibility(View.GONE);
        s_expense_from_linear.setVisibility(View.GONE);
        s_expense_to_linear.setVisibility(View.GONE);
        s_expense_submit_range_date.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        db.collection("Users").document(user_id).collection("salesExpenses").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(sales_expense_reports.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }{
                }

                if(error != null){

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    Log.e("TAG", "Database Error "+ error.getMessage());
                    Toast.makeText(sales_expense_reports.this,"Database Error "+ error.getMessage() , Toast.LENGTH_SHORT).show();
                    return;

                }
                for (DocumentChange dc :value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        sales_expense_list.add(dc.getDocument().toObject(model_class_sales_expenses.class));
                    }

                    sales_expense_adapter = new sales_expense_adapter(sales_expense_reports.this,sales_expense_list);
                    recyclerView.setAdapter(sales_expense_adapter);
                    sales_expense_adapter.notifyDataSetChanged();

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }


    //Sort By Date Range
    private void order_by_date_expense_range() {

        sales_expense_list.clear();
        recyclerView.setVisibility(View.GONE);
        s_expense_range_heading.setVisibility(View.VISIBLE);
        s_expense_from_linear.setVisibility(View.VISIBLE);
        s_expense_to_linear.setVisibility(View.VISIBLE);
        s_expense_submit_range_date.setVisibility(View.VISIBLE);



        s_expense_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(sales_expense_reports.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                s_expense_from_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        s_expense_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(sales_expense_reports.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                s_expense_to_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        s_expense_submit_range_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s_expense_to_date.setError(null);
                if (String.valueOf(s_expense_from_date.getText()).isEmpty()) {
                    s_expense_from_date.setError("Enter Date");
                    s_expense_from_date.requestFocus();
                    return;
                }
                if (String.valueOf(s_expense_to_date.getText()).isEmpty()) {
                    s_expense_to_date.setError("Enter Date");
                    s_expense_to_date.requestFocus();
                    return;
                }

                String from_date_string = s_expense_from_date.getText().toString();
                String to_date_string = s_expense_to_date.getText().toString();

                try {
                    s_from_date = new SimpleDateFormat("dd/MM/yyyy").parse(from_date_string);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    s_to_date = new SimpleDateFormat("dd/MM/yyyy").parse(to_date_string);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (s_to_date.before(s_from_date)) {
                    s_expense_to_date.setError("Select Appropriate Date");
                    s_expense_to_date.requestFocus();
                    recyclerView.setAdapter(null);
                }
                db.collection("Users").document(user_id).collection("salesExpenses").whereGreaterThanOrEqualTo("date",s_from_date).whereLessThanOrEqualTo("date",s_to_date).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (value.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            progressDialog.dismiss();
                            Toast.makeText(sales_expense_reports.this, "No Data Found", Toast.LENGTH_SHORT).show();
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            if (error != null) {

                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                Log.e("TAG", "Database Error " + error.getMessage());
                                Toast.makeText(sales_expense_reports.this, "Database Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                return;

                            }
                            for (DocumentChange dc : value.getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.ADDED) {
                                    sales_expense_list.add(dc.getDocument().toObject(model_class_sales_expenses.class));
                                }

                                sales_expense_adapter = new sales_expense_adapter(sales_expense_reports.this, sales_expense_list);
                                recyclerView.setAdapter(sales_expense_adapter);
                                sales_expense_adapter.notifyDataSetChanged();

                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            }
                        }
                    }
                });
            }
        });
    }
}
