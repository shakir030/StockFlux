package com.example.stockflux;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class product_reports extends AppCompatActivity {
    RecyclerView recycle_view;
    ProgressDialog progress;
    Button create_pdf;
    ArrayList<model_products_reports> product_list;
    product_reports_adapter adapter;
    FirebaseFirestore fGetProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_reports);

        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Fetching Data");
        progress.show();

        recycle_view = findViewById(R.id.recycle_view_reports);
        fGetProduct = FirebaseFirestore.getInstance();


        recycle_view.setHasFixedSize(true);
        recycle_view.setLayoutManager(new LinearLayoutManager(this));
        //adapter = new product_reports_adapter(this,getList());
        // recycle_view.setAdapter(adapter);
        product_list = new ArrayList<model_products_reports>();
        adapter = new product_reports_adapter(product_reports.this, product_list);
        recycle_view.setAdapter(adapter);
        EventChangeListner();


    }

    private void EventChangeListner() {
        fGetProduct.collection("addProducts").orderBy("product_name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

                    if (progress.isShowing())
                        progress.dismiss();
                    Log.e("Firestore Error", error.getMessage());
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        product_list.add(dc.getDocument().toObject(model_products_reports.class));
                    }
                    adapter.notifyDataSetChanged();
                    if (progress.isShowing())
                        progress.dismiss();
                }
            }
        });
    }
}