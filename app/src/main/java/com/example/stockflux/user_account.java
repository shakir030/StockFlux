package com.example.stockflux;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class user_account extends AppCompatActivity {
    TextInputEditText first_name_profile,number_profile,business_profile;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String user_id = fAuth.getCurrentUser().getUid();
    FirebaseFirestore fProfile = FirebaseFirestore.getInstance();
    Button reset_profile,update_profile;
    String profile_document_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        first_name_profile = findViewById(R.id.profile_first_name);
        number_profile = findViewById(R.id.profile_number);
        business_profile = findViewById(R.id.profile_business_name);

        Query query = fProfile.collection("Users").document(user_id).collection("UsersData");
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){
                    Toast.makeText(user_account.this, "No Records Found", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                    {
                        profile_document_id = documentSnapshot.getId();
                        user user_profile = documentSnapshot.toObject(user.class);
                        String name = user_profile.getFullname();
                        String number = user_profile.getNumber();
                        String business_name = user_profile.getBusinessname();

                        first_name_profile.setText(name);
                        number_profile.setText(number);
                        business_profile.setText(business_name);

                    }
                }
            }
        });

        reset_profile = findViewById(R.id.profile_reset_button);
        reset_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                first_name_profile.setText(null);
                number_profile.setText(null);
                business_profile.setText(null);
            }
        });

        update_profile = findViewById(R.id.profile_update_button);
        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String update_name = first_name_profile.getText().toString().trim();
                String update_number = number_profile.getText().toString().trim();
                String update_business = business_profile.getText().toString().trim();


                if(update_name.isEmpty()){
                    first_name_profile.setError("Enter Name !!");
                    first_name_profile.requestFocus();
                    return;
                }
                if(update_number.isEmpty()){
                    number_profile.setError("Enter Number !!");
                    number_profile.requestFocus();
                    return;
                }
                if(update_business.isEmpty()){
                    business_profile.setError("Enter Number !!");
                    business_profile.requestFocus();
                    return;
                }
                if(update_number.length() != 10){
                    number_profile.setError("Enter 10 Digits number !!");
                    number_profile.requestFocus();
                    return;

                }

                fProfile.collection("Users").document(user_id).collection("UsersData").document(profile_document_id).update("fullname",update_name,"number",update_number,"businessname",update_business).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(user_account.this, "Details Updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(user_account.this, "OnFailure :- "+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
}