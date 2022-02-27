package com.example.stockflux;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    FirebaseAuth fAuth;
    TextInputEditText txt_name, txt_email, txt_number, txt_password, txt_repassword, txt_bussiness_name;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView login_text = findViewById(R.id.login_text);
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(Register.this, Login.class);
                startActivity(login);
            }
        });


        txt_name = findViewById(R.id.name_register);
        txt_email = findViewById(R.id.email_register);
        txt_number = findViewById(R.id.number_register);
        txt_password = findViewById(R.id.password_register);
        txt_repassword = findViewById(R.id.repassword_register);
        txt_bussiness_name = findViewById(R.id.business_register);
        progress = findViewById(R.id.progress_bar);
        fAuth = FirebaseAuth.getInstance();
        Button register_button = findViewById(R.id.Register_submit);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txt_email.getText().toString().trim();
                String password = txt_password.getText().toString().trim();
                String name = txt_name.getText().toString().trim();
                String number = txt_number.getText().toString().trim();
                String repassword = txt_repassword.getText().toString().trim();
                String bussiness_name = txt_bussiness_name.getText().toString().trim();
                if (name.isEmpty()) {
                    txt_name.setError("Name is Required");
                    txt_name.requestFocus();
                    return;
                }
                if (email.isEmpty()) {
                    txt_email.setError("Email is Required");
                    txt_email.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    txt_email.setError("Provide Correct E-mail");
                    txt_email.requestFocus();
                    return;
                }
                if (number.isEmpty()) {
                    txt_number.setError("Number is Required");
                    txt_number.requestFocus();
                    return;
                }
                if (number.length() != 10) {
                    txt_number.setError("Enter 10 Digits Number ");
                    txt_number.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    txt_password.setError("Password is Required");
                    txt_password.requestFocus();
                    return;
                }
                if (password.length() < 6) {
                    txt_password.setError("Enter Passowrd more than 6 characters");
                    txt_password.requestFocus();
                    return;
                }
                if (repassword.isEmpty()) {
                    txt_repassword.setError("Re-Enter passwod is reqired");
                    txt_repassword.requestFocus();
                    return;
                }
                if (!password.equals(repassword)) {
                    txt_repassword.setError("Passwords are not Same.");
                    txt_repassword.requestFocus();
                    return;
                }

                if (bussiness_name.isEmpty()) {
                    txt_bussiness_name.setError("Bussiness Name is Required");
                    txt_bussiness_name.requestFocus();
                    return;
                }
                progress.setVisibility(View.VISIBLE);
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            user user = new user(name,email,number,bussiness_name);
                            FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        startActivity(new Intent(Register.this,Login.class));
                                        Toast.makeText(Register.this, "User has been Registered Successfull", Toast.LENGTH_SHORT).show();
                                        progress.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        Toast.makeText(Register.this, "Failed to Register try again", Toast.LENGTH_SHORT).show();
                                        progress.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(Register.this, "Failed to register user try again", Toast.LENGTH_SHORT).show();
                            progress.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}