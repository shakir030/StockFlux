package com.example.stockflux;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.AlphabeticIndex;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        Button guest_button = findViewById(R.id.guest_button);
        guest_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent guest = new Intent(Login.this,MainActivity.class);
                startActivity(guest);
            }
        });
        TextView register_text = findViewById(R.id.register_text);
        register_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(Login.this,Register.class);
                startActivity(register);
            }
        });

    }
}