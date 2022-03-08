package com.example.stockflux;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class profit_and_loss extends AppCompatActivity {
    RecyclerView recycle_view_pl;
    TextView date_pl;
    Button select_date,get_profit_loss;
    DatePickerDialog datePickerDialog;
    ProgressDialog progress_pl;
    ArrayList<model_profit_and_loss>product_list_pl;
    product_reports_adapter adapter_pl;
    FirebaseFirestore fGetProduct_pl;
    //List<profit_and_loss> purchase_name = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit_and_loss);

        /*progress_pl = new ProgressDialog(this);
        progress_pl.setCancelable(false);
        progress_pl.setMessage("Fetching Data");
        progress_pl.show();

        //date_pl = findViewById(R.id.date_select_pl);
        //get_profit_loss = findViewById(R.id.get_profit_loss);

        recycle_view_pl = findViewById(R.id.recycle_view_pl);
        fGetProduct_pl = FirebaseFirestore.getInstance();


        recycle_view_pl.setHasFixedSize(true);
        recycle_view_pl.setLayoutManager(new LinearLayoutManager(this));

        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(profit_and_loss.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                date_pl.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });



        //adapter = new product_reports_adapter(this,getList());
        // recycle_view.setAdapter(adapter);
        product_list_pl = new ArrayList<model_profit_and_loss>();
        adapter_pl = new product_reports_adapter(profit_and_loss.this, product_list_pl);
        recycle_view_pl.setAdapter(adapter_pl);
        EventChangeListner();


    }

    private void EventChangeListner() {
        fGetProduct_pl.collection("addProducts").orderBy("product_name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
        });*/
    }
}