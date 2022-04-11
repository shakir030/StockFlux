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
import com.google.android.material.textfield.TextInputLayout;
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

public class purchase_view extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<product_view_model_class> productArrayList;
    product_view_adapter_class productViewAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String user_id = fAuth.getCurrentUser().getUid();
    ProgressDialog progressDialog;
    TextView heading_range;
    LinearLayout from_layout,to_layout;
    TextInputLayout from_date_layout,to_date_layout;
    TextInputEditText from_date_range,to_date_range;
    DatePickerDialog datePickerDialog;
    Button submit_range_date;
    Date from_date,to_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_view);

        from_date_range = findViewById(R.id.from_date_data);
        to_date_range = findViewById(R.id.to_date_data);
        submit_range_date = findViewById(R.id.submit_range_date);
        from_layout = findViewById(R.id.from_linear);
        heading_range = findViewById(R.id.range_heading);
        to_layout = findViewById(R.id.to_linear);
        from_date_layout = findViewById(R.id.from_date_layout);
        to_date_layout = findViewById(R.id.to_date_layout);

        from_layout.setVisibility(View.GONE);
        to_layout.setVisibility(View.GONE);
        submit_range_date.setVisibility(View.GONE);



        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data ..");
        progressDialog.show();


        recyclerView = findViewById(R.id.view_recycle);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productArrayList = new ArrayList<product_view_model_class>();



        EventChangeListner();



    }

    private void EventChangeListner() {
        db.collection("Users").document(user_id).collection("addProducts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(purchase_view.this, "No Data Found", Toast.LENGTH_SHORT).show();
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
                        productArrayList.add(dc.getDocument().toObject(product_view_model_class.class));
                    }

                    productViewAdapter = new product_view_adapter_class(purchase_view.this,productArrayList);
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
                order_by_price_ascending();
                return true;

            case R.id.price_descending:
                order_by_price_descending();
                return true;

            case R.id.qty_ascending:
                order_by_qty_ascending();
                return true;

            case R.id.qty_descending:
                order_by_qty_descending();
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




    // Function Name Ascending
    private void order_by_name_descending() {
        productArrayList.clear();
        db.collection("Users").document(user_id).collection("addProducts").orderBy("product_name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    Log.e("TAG", "Database Error "+ error.getMessage());
                    return;

                }
                for (DocumentChange dc :value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        productArrayList.add(dc.getDocument().toObject(product_view_model_class.class));
                    }

                    productViewAdapter = new product_view_adapter_class(purchase_view.this,productArrayList);
                    recyclerView.setAdapter(productViewAdapter);
                    productViewAdapter.notifyDataSetChanged();
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    // Function Name Descending
    private void order_by_name_ascending() {
        recyclerView.setVisibility(View.VISIBLE);

        heading_range.setVisibility(View.GONE);
        from_layout.setVisibility(View.GONE);
        to_layout.setVisibility(View.GONE);
        submit_range_date.setVisibility(View.GONE);

        productArrayList.clear();
        db.collection("Users").document(user_id).collection("addProducts").orderBy("product_name", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error != null){

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    Log.e("TAG", "Database Error "+ error.getMessage());
                    return;

                }
                for (DocumentChange dc :value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        productArrayList.add(dc.getDocument().toObject(product_view_model_class.class));
                    }

                    productViewAdapter = new product_view_adapter_class(purchase_view.this,productArrayList);
                    recyclerView.setAdapter(productViewAdapter);
                    productViewAdapter.notifyDataSetChanged();
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    // Function Total Price Low-High
    private void order_by_price_ascending() {
        recyclerView.setVisibility(View.VISIBLE);

        heading_range.setVisibility(View.GONE);
        from_layout.setVisibility(View.GONE);
        to_layout.setVisibility(View.GONE);
        submit_range_date.setVisibility(View.GONE);


        productArrayList.clear();
        db.collection("Users").document(user_id).collection("addProducts").orderBy("product_total_price", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error != null){

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    Log.e("TAG", "Database Error "+ error.getMessage());
                    return;

                }
                for (DocumentChange dc :value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        productArrayList.add(dc.getDocument().toObject(product_view_model_class.class));
                    }

                    productViewAdapter = new product_view_adapter_class(purchase_view.this,productArrayList);
                    recyclerView.setAdapter(productViewAdapter);
                    productViewAdapter.notifyDataSetChanged();
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    // Function Total Price High-Low
    private void order_by_price_descending() {
        recyclerView.setVisibility(View.VISIBLE);

        heading_range.setVisibility(View.GONE);
        from_layout.setVisibility(View.GONE);
        to_layout.setVisibility(View.GONE);
        submit_range_date.setVisibility(View.GONE);


        productArrayList.clear();
        db.collection("Users").document(user_id).collection("addProducts").orderBy("product_total_price", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error != null){

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    Log.e("TAG", "Database Error "+ error.getMessage());
                    return;

                }
                for (DocumentChange dc :value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        productArrayList.add(dc.getDocument().toObject(product_view_model_class.class));
                    }

                    productViewAdapter = new product_view_adapter_class(purchase_view.this,productArrayList);
                    recyclerView.setAdapter(productViewAdapter);
                    productViewAdapter.notifyDataSetChanged();
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    //Order By Quantity Low-High
    private void order_by_qty_ascending() {
        recyclerView.setVisibility(View.VISIBLE);

        heading_range.setVisibility(View.GONE);
        from_layout.setVisibility(View.GONE);
        to_layout.setVisibility(View.GONE);
        submit_range_date.setVisibility(View.GONE);


        productArrayList.clear();
        db.collection("Users").document(user_id).collection("addProducts").orderBy("product_qty", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    Log.e("TAG", "Database Error "+ error.getMessage());
                    return;

                }
                for (DocumentChange dc :value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        productArrayList.add(dc.getDocument().toObject(product_view_model_class.class));
                    }

                    productViewAdapter = new product_view_adapter_class(purchase_view.this,productArrayList);
                    recyclerView.setAdapter(productViewAdapter);
                    productViewAdapter.notifyDataSetChanged();
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    //Order By Quantity High-Low
    private void order_by_qty_descending() {

        recyclerView.setVisibility(View.VISIBLE);

        heading_range.setVisibility(View.GONE);
        from_layout.setVisibility(View.GONE);
        to_layout.setVisibility(View.GONE);
        submit_range_date.setVisibility(View.GONE);

        productArrayList.clear();
        db.collection("Users").document(user_id).collection("addProducts").orderBy("product_qty", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    Log.e("TAG", "Database Error "+ error.getMessage());
                    return;

                }
                for (DocumentChange dc :value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        productArrayList.add(dc.getDocument().toObject(product_view_model_class.class));
                    }

                    productViewAdapter = new product_view_adapter_class(purchase_view.this,productArrayList);
                    recyclerView.setAdapter(productViewAdapter);
                    productViewAdapter.notifyDataSetChanged();
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    //Order By Date Ascending
    private void order_by_date_ascending() {

        recyclerView.setVisibility(View.VISIBLE);

        heading_range.setVisibility(View.GONE);
        from_layout.setVisibility(View.GONE);
        to_layout.setVisibility(View.GONE);
        submit_range_date.setVisibility(View.GONE);

        productArrayList.clear();
        db.collection("Users").document(user_id).collection("addProducts").orderBy("product_date", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    Log.e("TAG", "Database Error " + error.getMessage());
                    return;

                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        productArrayList.add(dc.getDocument().toObject(product_view_model_class.class));
                    }

                    productViewAdapter = new product_view_adapter_class(purchase_view.this, productArrayList);
                    recyclerView.setAdapter(productViewAdapter);
                    productViewAdapter.notifyDataSetChanged();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    //Order By Date Descending
    private void order_by_date_descending() {

        recyclerView.setVisibility(View.VISIBLE);

        heading_range.setVisibility(View.GONE);
        from_layout.setVisibility(View.GONE);
        to_layout.setVisibility(View.GONE);
        submit_range_date.setVisibility(View.GONE);

        productArrayList.clear();
        db.collection("Users").document(user_id).collection("addProducts").orderBy("product_date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    Log.e("TAG", "Database Error " + error.getMessage());
                    return;

                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        productArrayList.add(dc.getDocument().toObject(product_view_model_class.class));
                    }

                    productViewAdapter = new product_view_adapter_class(purchase_view.this, productArrayList);
                    recyclerView.setAdapter(productViewAdapter);
                    productViewAdapter.notifyDataSetChanged();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    public void order_by_date_range() {
        recyclerView.setVisibility(View.GONE);
        heading_range.setVisibility(View.VISIBLE);
        from_layout.setVisibility(View.VISIBLE);
        to_layout.setVisibility(View.VISIBLE);
        submit_range_date.setVisibility(View.VISIBLE);


        from_date_range.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(purchase_view.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                from_date_range.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        to_date_range.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(purchase_view.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                to_date_range.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        submit_range_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                to_date_range.setError(null);
                if(String.valueOf(from_date_range.getText()).isEmpty()){
                    from_date_range.setError("Enter Date");
                    from_date_range.requestFocus();
                    return;
                }
                if(String.valueOf(to_date_range.getText()).isEmpty()){
                    to_date_range.setError("Enter Date");
                    to_date_range.requestFocus();
                    return;
                }

                productArrayList.clear();


                String from_date_string = from_date_range.getText().toString();
                String to_date_string = to_date_range.getText().toString();

                try {
                    from_date = new SimpleDateFormat("dd/MM/yyyy").parse(from_date_string);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try{
                    to_date = new SimpleDateFormat("dd/MM/yyyy").parse(to_date_string);
                } catch (ParseException e){
                    e.printStackTrace();
                }

                if (to_date.before(from_date)) {
                    to_date_range.setError("Select Appropriate Date");
                    to_date_range.requestFocus();
                    recyclerView.setAdapter(null);
                }

                db.collection("Users").document(user_id).collection("addProducts").whereGreaterThanOrEqualTo("product_date",from_date).whereLessThanOrEqualTo("product_date",to_date).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            progressDialog.dismiss();
                            Toast.makeText(purchase_view.this, "Data not Found", Toast.LENGTH_SHORT).show();
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
                                    productArrayList.add(dc.getDocument().toObject(product_view_model_class.class));
                                }

                                productViewAdapter = new product_view_adapter_class(purchase_view.this, productArrayList);
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