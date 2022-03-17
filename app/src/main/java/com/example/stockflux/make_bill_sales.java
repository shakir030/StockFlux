package com.example.stockflux;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class make_bill_sales extends AppCompatActivity {
    TextView mobile_text,organisation_text,costumer_text,date_text,product_name_text,product_qty_text,product_per_price_text,product_total_price_text,product_total_bill_amount;
    FirebaseFirestore fMakeBill;
    private DatabaseReference reference;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String user_id = fAuth.getCurrentUser().getUid();
    String product_name,costumer_name;
    int product_qty,product_total_price,product_per_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_bill_sales);

        mobile_text = findViewById(R.id.mobile_number_layout);
        organisation_text = findViewById(R.id.organisation_name_layout);
        costumer_text = findViewById(R.id.bill_costumer_layout);
        date_text = findViewById(R.id.data_date_bill);
        product_name_text = findViewById(R.id.bill_product_name);
        product_qty_text = findViewById(R.id.bill_product_qty);
        product_per_price_text = findViewById(R.id.bill_product_per_price);
        product_total_price_text = findViewById(R.id.bill_product_total_price);
        product_total_bill_amount = findViewById(R.id.total_bill_amount);
        fMakeBill = FirebaseFirestore.getInstance();

        costumer_name = getIntent().getStringExtra("costumer_name");
        product_name = getIntent().getStringExtra("product_name");
        product_qty = getIntent().getIntExtra("product_qty",0);
        product_per_price = getIntent().getIntExtra("product_per_price",0);
        product_total_price = getIntent().getIntExtra("product_total_price",0);


        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        date_text.setText("Date :- "+date);
        costumer_text.setText("Costumer Name :- "+costumer_name);

        //get number and organisation name from realtime database
       Query query = fMakeBill.collection("Users").document(user_id).collection("UsersData");
       query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
           @Override
           public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
               if(queryDocumentSnapshots.isEmpty()){
                   Toast.makeText(make_bill_sales.this, "No Data Found", Toast.LENGTH_SHORT).show();
               }
               else {
                   for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                       user user_profile = documentSnapshot.toObject(user.class);
                       String number = user_profile.getNumber();
                       String organistaion_name = user_profile.getBusinessname();
                       mobile_text.setText("Mobile Number :- " + number);
                       organisation_text.setText(organistaion_name);
                   }
               }
           }
       });


        //get data of products

         product_name_text.setText(product_name);
         product_qty_text.setText(String.valueOf(product_qty));
         product_per_price_text.setText(String.valueOf(product_per_price));
         product_total_price_text.setText(String.valueOf(product_total_price));
         product_total_bill_amount.setText(String.valueOf(product_total_price));




    }
}