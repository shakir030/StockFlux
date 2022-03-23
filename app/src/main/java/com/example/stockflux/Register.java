package com.example.stockflux;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register extends AppCompatActivity {
    public static final String TAG = "REGISTER";
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    //String user_id = fAuth.getCurrentUser().getUid();
    TextInputEditText txt_name, txt_email, txt_number, txt_password, txt_repassword, txt_bussiness_name;
    ProgressBar progress;
    FirebaseFirestore fRegister;

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

        fRegister = FirebaseFirestore.getInstance();

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
                            String user_id = fAuth.getCurrentUser().getUid();
                            user user_data = new user();
                            String name = txt_name.getText().toString().trim();
                            String email = txt_email.getText().toString().trim();
                            String number = txt_number.getText().toString().trim();
                            String bussiness_name = txt_bussiness_name.getText().toString().trim();
                            user_data.setFullname(name);
                            user_data.setEmail(email);
                            user_data.setNumber(number);
                            user_data.setBusinessname(bussiness_name);
                            fRegister.collection("Users").document(user_id).collection("UsersData").document().set(user_data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user_verify = fAuth.getCurrentUser();
                                    user_verify.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(Register.this, "E-Mail Verification has been sent to your E-Mail", Toast.LENGTH_LONG).show();
                                            Intent register = new Intent(Register.this,Login.class);
                                            startActivity(register);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "onFailure: Email not sent"+e.getMessage());

                                        }
                                    });
                                    progress.setVisibility(View.GONE);
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