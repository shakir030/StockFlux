package com.example.stockflux;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class make_bill_sales extends AppCompatActivity {
    TextView bill_no,mobile_text,organisation_text,costumer_text,date_text,product_name_text,product_qty_text,product_per_price_text,product_total_price_text,product_total_bill_amount;
    FirebaseFirestore fMakeBill;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String user_id = fAuth.getCurrentUser().getUid();
    String product_name,costumer_name,Bill_no;
    PdfDocument document;
    Button create_pdf;
    int product_qty,product_total_price,product_per_price;
    Bitmap bmp,scaledbmp;
    int pageWidth = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_bill_sales);


        mobile_text = findViewById(R.id.mobile_number_layout);
        bill_no = findViewById(R.id.bill_no);
        organisation_text = findViewById(R.id.organisation_name_layout);
        costumer_text = findViewById(R.id.bill_costumer_layout);
        date_text = findViewById(R.id.data_date_bill);
        product_name_text = findViewById(R.id.bill_product_name);
        product_qty_text = findViewById(R.id.bill_product_qty);
        product_per_price_text = findViewById(R.id.bill_product_per_price);
        product_total_price_text = findViewById(R.id.bill_product_total_price);
        product_total_bill_amount = findViewById(R.id.total_bill_amount);
        fMakeBill = FirebaseFirestore.getInstance();
        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.grey);
        scaledbmp = Bitmap.createScaledBitmap(bmp,1200,350,false);

        create_pdf = findViewById(R.id.create_pdf);

        costumer_name = getIntent().getStringExtra("costumer_name");
        product_name = getIntent().getStringExtra("product_name");
        Bill_no = getIntent().getStringExtra("Bill_id");
        product_qty = getIntent().getIntExtra("product_qty", 0);
        product_per_price = getIntent().getIntExtra("product_per_price", 0);
        product_total_price = getIntent().getIntExtra("product_total_price", 0);


        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        bill_no.setText("Bill ID :- " + Bill_no);

        date_text.setText("Date :- " + date);
        costumer_text.setText("Costumer Name :- " + costumer_name);

        //get number and organisation name from realtime database
        Query query = fMakeBill.collection("Users").document(user_id).collection("UsersData");
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    Toast.makeText(make_bill_sales.this, "No Data Found", Toast.LENGTH_SHORT).show();
                } else {
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

        create_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPDF();
            }
        });

    }

    private void createPDF() {

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        document = new PdfDocument();
        Paint myPaint = new Paint();
        Paint titlePaint = new Paint();

        PdfDocument.PageInfo mypageinfo1 = new PdfDocument.PageInfo.Builder(1200,2010,1).create();
        PdfDocument.Page myPage1 = document.startPage(mypageinfo1);
        Canvas canvas = myPage1.getCanvas();

        canvas.drawBitmap(scaledbmp,0,0,myPaint);
        String name = organisation_text.getText().toString().trim();
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setColor(Color.WHITE);
        organisation_text.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        titlePaint.setTextSize(70);
        canvas.drawText(name,pageWidth/2,270,titlePaint);

        String number = mobile_text.getText().toString().trim();

        myPaint.setColor(Color.WHITE);
        myPaint.setTextSize(30);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(number,1160,40,myPaint);


        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(35f);
        myPaint.setColor(Color.BLACK);
        canvas.drawText("Coustomer Name :- "+costumer_name,20,540,myPaint);

        myPaint.setTextAlign(Paint.Align.RIGHT);
        String bill_id = bill_no.getText().toString().trim();
        canvas.drawText(bill_id,pageWidth-20,540,myPaint);

        String date = date_text.getText().toString().trim();
        canvas.drawText(date,pageWidth-20,590,myPaint);

        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeWidth(2);
        canvas.drawRect(20,780,pageWidth-20,860,myPaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setStyle(Paint.Style.FILL);
        canvas.drawText("Product Name ",40,830,myPaint);
        canvas.drawText("QTY",700,830,myPaint);
        canvas.drawText("PER/Price",800,830,myPaint);
        canvas.drawText("Total Price",1000,830,myPaint);

        canvas.drawLine(650,790,650,840,myPaint);
        canvas.drawLine(790,790,790,840,myPaint);
        canvas.drawLine(980,790,980,840,myPaint);

        canvas.drawText(product_name,100,950,myPaint);
        canvas.drawText(String.valueOf(product_qty),720,950,myPaint);
        canvas.drawText(String.valueOf(product_per_price),850,950,myPaint);
        canvas.drawText(String.valueOf(product_total_price),1050,950,myPaint);

        canvas.drawLine(680,1200,pageWidth-20,1200,myPaint);
        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setColor(Color.GRAY);
        canvas.drawRect(680,1250,pageWidth-20,1400,myPaint);
        myPaint.setColor(Color.WHITE);
        myPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        myPaint.setTextSize(50f);
        myPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Total",700,1330,myPaint);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.valueOf(product_total_price),pageWidth-40,1330,myPaint);

        document.finishPage(myPage1);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"invoice_bill.pdf");

        try{
            document.writeTo(new FileOutputStream(file));

        }catch(IOException e){
            e.printStackTrace();
        }
        document.close();
        Toast.makeText(this, "PDF is Downloaded to your Downloads folder", Toast.LENGTH_SHORT).show();

    }
}