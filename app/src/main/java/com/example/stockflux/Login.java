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
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private TextView register_text,forget_password;
    private Button guest_button,sign_in_button;
    private TextInputEditText email_login,password_login;
    private FirebaseAuth fAuth;
    private FirebaseUser user;
    private ProgressBar progress_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        forget_password = findViewById(R.id.forget_password);
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,activity_forget_password.class));
            }
        });
        register_text = findViewById(R.id.register_text);
        register_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(Login.this,Register.class);
                startActivity(register);
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            startActivity(new Intent(Login.this,MainActivity.class));
            finish();
        }
        email_login=findViewById(R.id.email_login_textfield);
        password_login=findViewById(R.id.password_login_textfield);
        progress_login = findViewById(R.id.progress_login);
        fAuth = FirebaseAuth.getInstance();
        sign_in_button = findViewById(R.id.login_button);
        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_string = email_login.getText().toString().trim();
                String password_string = password_login.getText().toString().trim();

                if(email_string.isEmpty())
                {
                    email_login.setError("Email is Required !!");
                    email_login.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email_string).matches())
                {
                    email_login.setError("Email is not Valid.");
                    email_login.requestFocus();
                    return;
                }
                if(password_string.isEmpty())
                {
                    password_login.setError("Password is Required !!.");
                    password_login.requestFocus();
                    return;
                }
                if(password_string.length()<6)
                {
                    password_login.setError("Password should be more than 6 characters ");
                    password_login.requestFocus();
                    return;
                }
                progress_login.setVisibility(View.VISIBLE);
                fAuth.signInWithEmailAndPassword(email_string,password_string).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progress_login.setVisibility(View.GONE);
                           startActivity(new Intent(Login.this,MainActivity.class));
                        }
                        else
                        {
                            Toast.makeText(Login.this, "Failed to Login", Toast.LENGTH_LONG).show();
                            progress_login.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}