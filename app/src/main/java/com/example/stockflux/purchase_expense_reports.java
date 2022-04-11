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

public class purchase_expense_reports extends AppCompatActivity {
    RecyclerView recyclerView;
    TextInputEditText p_expense_from_date,p_expense_to_date;
    TextView p_expense_range_heading;
    Button p_expense_submit_range_date;
    DatePickerDialog datePickerDialog;
    LinearLayout p_expense_from_linear,p_expense_to_linear;
    ArrayList<purchase_expense_model> purchase_expense_list;
    purchase_expense_adapter purchase_expense_adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String user_id = fAuth.getCurrentUser().getUid();
    ProgressDialog progressDialog;
    Date p_from_date,p_to_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_expense_reports);

        p_expense_from_date = findViewById(R.id.p_expense_from_date_data);
        p_expense_range_heading = findViewById(R.id.range_heading_expense_purchase);
        p_expense_to_date = findViewById(R.id.p_expense_to_date_data);
        p_expense_submit_range_date = findViewById(R.id.p_expense_submit_range_date);
        p_expense_from_linear = findViewById(R.id.from_p_expense_linear);
        p_expense_to_linear = findViewById(R.id.to_p_expense_linear);


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data ..");
        progressDialog.show();


        recyclerView = findViewById(R.id.purchase_expense_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        purchase_expense_list = new ArrayList<purchase_expense_model>();



        EventChangeListner();



    }

    private void EventChangeListner() {
        db.collection("Users").document(user_id).collection("purchaseExpenses").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(purchase_expense_reports.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }
                if(error != null){

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    Log.e("TAG", "Database Error "+ error.getMessage());
                    return;

                }
                for (DocumentChange dc :value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        purchase_expense_list.add(dc.getDocument().toObject(purchase_expense_model.class));
                    }

                    purchase_expense_adapter = new purchase_expense_adapter(purchase_expense_reports.this,purchase_expense_list);
                    recyclerView.setAdapter(purchase_expense_adapter);
                    purchase_expense_adapter.notifyDataSetChanged();

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
                purchase_expense_adapter.getFilter().filter(s);
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
        purchase_expense_list.clear();
        p_expense_range_heading.setVisibility(View.GONE);
        p_expense_from_linear.setVisibility(View.GONE);
        p_expense_to_linear.setVisibility(View.GONE);
        p_expense_submit_range_date.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        db.collection("Users").document(user_id).collection("purchaseExpenses").orderBy("purchase_expense_name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(purchase_expense_reports.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }
                if(error != null){

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    Log.e("TAG", "Database Error "+ error.getMessage());
                    return;

                }
                for (DocumentChange dc :value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        purchase_expense_list.add(dc.getDocument().toObject(purchase_expense_model.class));
                    }

                    purchase_expense_adapter = new purchase_expense_adapter(purchase_expense_reports.this,purchase_expense_list);
                    recyclerView.setAdapter(purchase_expense_adapter);
                    purchase_expense_adapter.notifyDataSetChanged();

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
        purchase_expense_list.clear();
        p_expense_range_heading.setVisibility(View.GONE);
        p_expense_from_linear.setVisibility(View.GONE);
        p_expense_to_linear.setVisibility(View.GONE);
        p_expense_submit_range_date.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        db.collection("Users").document(user_id).collection("purchaseExpenses").orderBy("purchase_expense_name", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(purchase_expense_reports.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }
                if(error != null){

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    Log.e("TAG", "Database Error "+ error.getMessage());
                    return;

                }
                for (DocumentChange dc :value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        purchase_expense_list.add(dc.getDocument().toObject(purchase_expense_model.class));
                    }

                    purchase_expense_adapter = new purchase_expense_adapter(purchase_expense_reports.this,purchase_expense_list);
                    recyclerView.setAdapter(purchase_expense_adapter);
                    purchase_expense_adapter.notifyDataSetChanged();

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
        purchase_expense_list.clear();
        p_expense_range_heading.setVisibility(View.GONE);
        p_expense_from_linear.setVisibility(View.GONE);
        p_expense_to_linear.setVisibility(View.GONE);
        p_expense_submit_range_date.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        db.collection("Users").document(user_id).collection("purchaseExpenses").orderBy("purchase_expense_total_price", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(purchase_expense_reports.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }
                if(error != null){

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    Log.e("TAG", "Database Error "+ error.getMessage());
                    return;

                }
                for (DocumentChange dc :value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        purchase_expense_list.add(dc.getDocument().toObject(purchase_expense_model.class));
                    }

                    purchase_expense_adapter = new purchase_expense_adapter(purchase_expense_reports.this,purchase_expense_list);
                    recyclerView.setAdapter(purchase_expense_adapter);
                    purchase_expense_adapter.notifyDataSetChanged();

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
        purchase_expense_list.clear();
        p_expense_range_heading.setVisibility(View.GONE);
        p_expense_from_linear.setVisibility(View.GONE);
        p_expense_to_linear.setVisibility(View.GONE);
        p_expense_submit_range_date.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        db.collection("Users").document(user_id).collection("purchaseExpenses").orderBy("purchase_expense_total_price", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(purchase_expense_reports.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }
                if(error != null){

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    Log.e("TAG", "Database Error "+ error.getMessage());
                    return;

                }
                for (DocumentChange dc :value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        purchase_expense_list.add(dc.getDocument().toObject(purchase_expense_model.class));
                    }

                    purchase_expense_adapter = new purchase_expense_adapter(purchase_expense_reports.this,purchase_expense_list);
                    recyclerView.setAdapter(purchase_expense_adapter);
                    purchase_expense_adapter.notifyDataSetChanged();

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
        purchase_expense_list.clear();
        p_expense_range_heading.setVisibility(View.GONE);
        p_expense_from_linear.setVisibility(View.GONE);
        p_expense_to_linear.setVisibility(View.GONE);
        p_expense_submit_range_date.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        db.collection("Users").document(user_id).collection("purchaseExpenses").orderBy("purchase_expense_date", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(purchase_expense_reports.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }
                if(error != null){

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    Log.e("TAG", "Database Error "+ error.getMessage());
                    return;

                }
                for (DocumentChange dc :value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        purchase_expense_list.add(dc.getDocument().toObject(purchase_expense_model.class));
                    }

                    purchase_expense_adapter = new purchase_expense_adapter(purchase_expense_reports.this,purchase_expense_list);
                    recyclerView.setAdapter(purchase_expense_adapter);
                    purchase_expense_adapter.notifyDataSetChanged();

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
        purchase_expense_list.clear();
        p_expense_range_heading.setVisibility(View.GONE);
        p_expense_from_linear.setVisibility(View.GONE);
        p_expense_to_linear.setVisibility(View.GONE);
        p_expense_submit_range_date.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        db.collection("Users").document(user_id).collection("purchaseExpenses").orderBy("purchase_expense_date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(purchase_expense_reports.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }
                if(error != null){

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    Log.e("TAG", "Database Error "+ error.getMessage());
                    return;

                }
                for (DocumentChange dc :value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        purchase_expense_list.add(dc.getDocument().toObject(purchase_expense_model.class));
                    }

                    purchase_expense_adapter = new purchase_expense_adapter(purchase_expense_reports.this,purchase_expense_list);
                    recyclerView.setAdapter(purchase_expense_adapter);
                    purchase_expense_adapter.notifyDataSetChanged();

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }


    //Sort By Date Range
    private void order_by_date_expense_range() {

        purchase_expense_list.clear();
        recyclerView.setVisibility(View.GONE);
        p_expense_range_heading.setVisibility(View.VISIBLE);
        p_expense_from_linear.setVisibility(View.VISIBLE);
        p_expense_to_linear.setVisibility(View.VISIBLE);
        p_expense_submit_range_date.setVisibility(View.VISIBLE);


        p_expense_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(purchase_expense_reports.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                p_expense_from_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        p_expense_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(purchase_expense_reports.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                p_expense_to_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        p_expense_submit_range_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p_expense_to_date.setError(null);
                if (String.valueOf(p_expense_from_date.getText()).isEmpty()) {
                    p_expense_from_date.setError("Enter Date");
                    p_expense_from_date.requestFocus();
                    return;
                }
                if (String.valueOf(p_expense_to_date.getText()).isEmpty()) {
                    p_expense_to_date.setError("Enter Date");
                    p_expense_to_date.requestFocus();
                    return;
                }

                purchase_expense_list.clear();


                String from_date_string = p_expense_from_date.getText().toString();
                String to_date_string = p_expense_to_date.getText().toString();

                try {
                    p_from_date = new SimpleDateFormat("dd/MM/yyyy").parse(from_date_string);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    p_to_date = new SimpleDateFormat("dd/MM/yyyy").parse(to_date_string);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (p_to_date.before(p_from_date)) {
                    p_expense_to_date.setError("Select Appropriate Date");
                    p_expense_to_date.requestFocus();
                    recyclerView.setAdapter(null);
                }
                db.collection("Users").document(user_id).collection("purchaseExpenses").whereGreaterThanOrEqualTo("purchase_expense_date",p_from_date).whereLessThanOrEqualTo("purchase_expense_date",p_to_date).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (value.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            progressDialog.dismiss();
                            Toast.makeText(purchase_expense_reports.this, "No Data Found", Toast.LENGTH_SHORT).show();
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            if (error != null) {

                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                Log.e("TAG", "Database Error " + error.getMessage());
                                return;

                            }
                            for (DocumentChange dc : value.getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.ADDED) {
                                    purchase_expense_list.add(dc.getDocument().toObject(purchase_expense_model.class));
                                }

                                purchase_expense_adapter = new purchase_expense_adapter(purchase_expense_reports.this, purchase_expense_list);
                                recyclerView.setAdapter(purchase_expense_adapter);
                                purchase_expense_adapter.notifyDataSetChanged();

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

