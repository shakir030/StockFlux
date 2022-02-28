package com.example.stockflux;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class activity_forget_password extends AppCompatActivity {
    private TextInputEditText emailTextField;
    private Button reset_password_button;
    private ProgressBar progress_forget_password;
    private FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_passowrd);
        setTitle("Forget Password");
        fAuth = FirebaseAuth.getInstance();
        emailTextField = findViewById(R.id.forget_email_textfield);
        reset_password_button = findViewById(R.id.forget_reset_button);
        progress_forget_password = findViewById(R.id.progress_reset);
        reset_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_string = emailTextField.getText().toString().trim();
                if(email_string.isEmpty()){
                    emailTextField.setError("Email is required ");
                    emailTextField.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email_string).matches())
                {
                    emailTextField.setError("Enter Valid Email ");
                    emailTextField.requestFocus();
                    return;
                }
                progress_forget_password.setVisibility(View.VISIBLE);
                fAuth.sendPasswordResetEmail(email_string).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(activity_forget_password.this, "Check your email to reset your password!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(activity_forget_password.this, Login.class));
                        }
                        else
                        {
                            Toast.makeText(activity_forget_password.this, "Try Again Something wrong Happened", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        }
}
