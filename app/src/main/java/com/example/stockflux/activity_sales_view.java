package com.example.stockflux;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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

public class activity_sales_view extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView heading_sales_text;
    TextInputEditText from_sales_date,to_sales_date;
    LinearLayout from_sales_linear,to_sales_linear;
    Button submit_date_sales_range;
    ArrayList<sales_view_model> productArrayList;
    sales_view_adapter productViewAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String user_id = fAuth.getCurrentUser().getUid();
    ProgressDialog progressDialog;
    DatePickerDialog datePickerDialog;
    Date sales_from_date,sales_to_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_view);

        heading_sales_text = findViewById(R.id.sales_range_heading);
        from_sales_linear = findViewById(R.id.sales_from_linear);
        to_sales_linear = findViewById(R.id.sales_to_linear);
        from_sales_date = findViewById(R.id.sales_from_date_data);
        to_sales_date = findViewById(R.id.sales_to_date_data);
        submit_date_sales_range = findViewById(R.id.submit_sales_range_date);



        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data ..");
        progressDialog.show();


        recyclerView = findViewById(R.id.view_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productArrayList = new ArrayList<sales_view_model>();



        EventChangeListner();



    }

    private void EventChangeListner() {
        db.collection("Users").document(user_id).collection("AddSalesData").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(activity_sales_view.this, "No Data Found", Toast.LENGTH_SHORT).show();
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
                        productArrayList.add(dc.getDocument().toObject(sales_view_model.class));
                    }
                    productViewAdapter = new sales_view_adapter(activity_sales_view.this,productArrayList);
                    recyclerView.setAdapter(productViewAdapter);
                    productViewAdapter.notifyDataSetChanged();

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
                productViewAdapter.getFilter().filter(s);
                return false;
            }
        });

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter_data,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.name_ascending:
                order_by_name_ascending();
                return true;
            case R.id.name_descending:
                order_by_name_descending();
                return true;

            case R.id.price_ascending:
                order_by_price_low_high();
                return true;

            case R.id.price_descending:
                order_by_price_high_low();
                return true;

            case R.id.qty_ascending:
                order_by_qty_low_high();
                return true;

            case R.id.qty_descending:
                order_by_qty_high_low();
                return true;

            case R.id.date_ascending:
                order_by_date_ascending();
                return true;
            case R.id.date_descending:
                order_by_date_descending();
                return true;
            case R.id.date_range:
                order_by_date_range();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }




    private void order_by_name_ascending()
    {
        productArrayList.clear();
        heading_sales_text.setVisibility(View.GONE);
        from_sales_linear.setVisibility(View.GONE);
        to_sales_linear.setVisibility(View.GONE);
        submit_date_sales_range.setVisibility(View.GONE);

        db.collection("Users").document(user_id).collection("AddSalesData").orderBy("name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                
                if(value.isEmpty()){
                    Toast.makeText(activity_sales_view.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }
                
                if(value.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(activity_sales_view.this, "No Data Found", Toast.LENGTH_SHORT).show();
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
                        productArrayList.add(dc.getDocument().toObject(sales_view_model.class));
                    }
                    productViewAdapter = new sales_view_adapter(activity_sales_view.this,productArrayList);
                    recyclerView.setAdapter(productViewAdapter);
                    productViewAdapter.notifyDataSetChanged();

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    private void order_by_name_descending()
    {
        productArrayList.clear();
        heading_sales_text.setVisibility(View.GONE);
        from_sales_linear.setVisibility(View.GONE);
        to_sales_linear.setVisibility(View.GONE);
        submit_date_sales_range.setVisibility(View.GONE);

        db.collection("Users").document(user_id).collection("AddSalesData").orderBy("name", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.isEmpty()){
                    Toast.makeText(activity_sales_view.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }

                if(value.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(activity_sales_view.this, "No Data Found", Toast.LENGTH_SHORT).show();
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
                        productArrayList.add(dc.getDocument().toObject(sales_view_model.class));
                    }
                    productViewAdapter = new sales_view_adapter(activity_sales_view.this,productArrayList);
                    recyclerView.setAdapter(productViewAdapter);
                    productViewAdapter.notifyDataSetChanged();

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    private void order_by_price_low_high()
    {
        productArrayList.clear();
        heading_sales_text.setVisibility(View.GONE);
        from_sales_linear.setVisibility(View.GONE);
        to_sales_linear.setVisibility(View.GONE);
        submit_date_sales_range.setVisibility(View.GONE);

        db.collection("Users").document(user_id).collection("AddSalesData").orderBy("total_Price", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.isEmpty()){
                    Toast.makeText(activity_sales_view.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }

                if(value.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(activity_sales_view.this, "No Data Found", Toast.LENGTH_SHORT).show();
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
                        productArrayList.add(dc.getDocument().toObject(sales_view_model.class));
                    }
                    productViewAdapter = new sales_view_adapter(activity_sales_view.this,productArrayList);
                    recyclerView.setAdapter(productViewAdapter);
                    productViewAdapter.notifyDataSetChanged();

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    private void order_by_price_high_low()
    {
        productArrayList.clear();
        heading_sales_text.setVisibility(View.GONE);
        from_sales_linear.setVisibility(View.GONE);
        to_sales_linear.setVisibility(View.GONE);
        submit_date_sales_range.setVisibility(View.GONE);

        db.collection("Users").document(user_id).collection("AddSalesData").orderBy("total_Price", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.isEmpty()){
                    Toast.makeText(activity_sales_view.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }

                if(value.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(activity_sales_view.this, "No Data Found", Toast.LENGTH_SHORT).show();
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
                        productArrayList.add(dc.getDocument().toObject(sales_view_model.class));
                    }
                    productViewAdapter = new sales_view_adapter(activity_sales_view.this,productArrayList);
                    recyclerView.setAdapter(productViewAdapter);
                    productViewAdapter.notifyDataSetChanged();

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    private void order_by_qty_low_high()
    {
        productArrayList.clear();
        heading_sales_text.setVisibility(View.GONE);
        from_sales_linear.setVisibility(View.GONE);
        to_sales_linear.setVisibility(View.GONE);
        submit_date_sales_range.setVisibility(View.GONE);

        db.collection("Users").document(user_id).collection("AddSalesData").orderBy("quantity", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.isEmpty()){
                    Toast.makeText(activity_sales_view.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }

                if(value.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(activity_sales_view.this, "No Data Found", Toast.LENGTH_SHORT).show();
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
                        productArrayList.add(dc.getDocument().toObject(sales_view_model.class));
                    }
                    productViewAdapter = new sales_view_adapter(activity_sales_view.this,productArrayList);
                    recyclerView.setAdapter(productViewAdapter);
                    productViewAdapter.notifyDataSetChanged();

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    private void order_by_qty_high_low()
    {
        productArrayList.clear();
        heading_sales_text.setVisibility(View.GONE);
        from_sales_linear.setVisibility(View.GONE);
        to_sales_linear.setVisibility(View.GONE);
        submit_date_sales_range.setVisibility(View.GONE);

        db.collection("Users").document(user_id).collection("AddSalesData").orderBy("total_Price", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.isEmpty()){
                    Toast.makeText(activity_sales_view.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }

                if(value.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(activity_sales_view.this, "No Data Found", Toast.LENGTH_SHORT).show();
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
                        productArrayList.add(dc.getDocument().toObject(sales_view_model.class));
                    }
                    productViewAdapter = new sales_view_adapter(activity_sales_view.this,productArrayList);
                    recyclerView.setAdapter(productViewAdapter);
                    productViewAdapter.notifyDataSetChanged();

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    private void order_by_date_ascending()
    {
        productArrayList.clear();
        heading_sales_text.setVisibility(View.GONE);
        from_sales_linear.setVisibility(View.GONE);
        to_sales_linear.setVisibility(View.GONE);
        submit_date_sales_range.setVisibility(View.GONE);

        db.collection("Users").document(user_id).collection("AddSalesData").orderBy("date", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.isEmpty()){
                    Toast.makeText(activity_sales_view.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }

                if(value.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(activity_sales_view.this, "No Data Found", Toast.LENGTH_SHORT).show();
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
                        productArrayList.add(dc.getDocument().toObject(sales_view_model.class));
                    }
                    productViewAdapter = new sales_view_adapter(activity_sales_view.this,productArrayList);
                    recyclerView.setAdapter(productViewAdapter);
                    productViewAdapter.notifyDataSetChanged();

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    private void order_by_date_descending()
    {
        productArrayList.clear();
        heading_sales_text.setVisibility(View.GONE);
        from_sales_linear.setVisibility(View.GONE);
        to_sales_linear.setVisibility(View.GONE);
        submit_date_sales_range.setVisibility(View.GONE);

        db.collection("Users").document(user_id).collection("AddSalesData").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.isEmpty()){
                    Toast.makeText(activity_sales_view.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }

                if(value.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(activity_sales_view.this, "No Data Found", Toast.LENGTH_SHORT).show();
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
                        productArrayList.add(dc.getDocument().toObject(sales_view_model.class));
                    }
                    productViewAdapter = new sales_view_adapter(activity_sales_view.this,productArrayList);
                    recyclerView.setAdapter(productViewAdapter);
                    productViewAdapter.notifyDataSetChanged();

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    private void order_by_date_range()
    {
        productArrayList.clear();
        recyclerView.setVisibility(View.GONE);
        heading_sales_text.setVisibility(View.VISIBLE);
        from_sales_linear.setVisibility(View.VISIBLE);
        to_sales_linear.setVisibility(View.VISIBLE);
        submit_date_sales_range.setVisibility(View.VISIBLE);


        from_sales_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(activity_sales_view.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                from_sales_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        to_sales_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(activity_sales_view.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                to_sales_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        submit_date_sales_range.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                to_sales_date.setError(null);
                if(String.valueOf(from_sales_date.getText()).isEmpty()){
                    from_sales_date.setError("Enter Date");
                    from_sales_date.requestFocus();
                    return;
                }
                if(String.valueOf(to_sales_date.getText()).isEmpty()){
                    to_sales_date.setError("Enter Date");
                    to_sales_date.requestFocus();
                    return;
                }

                productArrayList.clear();

                String from_date_string = from_sales_date.getText().toString();
                String to_date_string = to_sales_date.getText().toString();

                try {
                    sales_from_date = new SimpleDateFormat("dd/MM/yyyy").parse(from_date_string);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try{
                    sales_to_date = new SimpleDateFormat("dd/MM/yyyy").parse(to_date_string);
                } catch (ParseException e){
                    e.printStackTrace();
                }

                if (sales_to_date.before(sales_from_date)) {
                    to_sales_date.setError("Select Appropriate Date");
                    to_sales_date.requestFocus();
                    recyclerView.setAdapter(null);
                }


                db.collection("Users").document(user_id).collection("AddSalesData").whereGreaterThanOrEqualTo("date",sales_from_date).whereLessThanOrEqualTo("date",sales_to_date).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(value.isEmpty()){
                                progressDialog.dismiss();
                                recyclerView.setVisibility(View.GONE);
                                Toast.makeText(activity_sales_view.this, "No Data Found", Toast.LENGTH_SHORT).show();
                            }
                            else {
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
                                        productArrayList.add(dc.getDocument().toObject(sales_view_model.class));
                                    }

                                    productViewAdapter = new sales_view_adapter(activity_sales_view.this, productArrayList);
                                    recyclerView.setAdapter(productViewAdapter);
                                    productViewAdapter.notifyDataSetChanged();
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