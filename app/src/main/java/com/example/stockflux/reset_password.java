package com.example.stockflux;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class reset_password extends AppCompatActivity {
    Button reset_password_button;
    TextInputEditText password_text,reenter_password_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        password_text = findViewById(R.id.password_reset_textfield);
        reenter_password_text = findViewById(R.id.reenter_password_reset_textfield);
        reset_password_button = findViewById(R.id.reset_password_button);



        reset_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = password_text.getText().toString().trim();
                String repass = reenter_password_text.getText().toString().trim();

                if(pass.isEmpty()){
                    password_text.setError("Password Required");
                    password_text.requestFocus();
                    return;
                }
                if(repass.isEmpty()){
                    reenter_password_text.setError("Re-Password Required");
                    reenter_password_text.requestFocus();
                    return;
                }
                if(pass.length()<6){
                    password_text.setError("Enter characters more than 6");
                    password_text.requestFocus();
                    return;
                }
                if(repass.length()<6){
                    reenter_password_text.setError("Enter characters more than 6");
                    reenter_password_text.requestFocus();
                    return;
                }
                if(!pass.equals(repass)){
                    reenter_password_text.setError("Password are not same");
                    reenter_password_text.requestFocus();
                    return;
                }
            }
        });

    }
}