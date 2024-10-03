package com.example.sprintproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AccountCreation extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_creation);

        Button RegisterButton = findViewById(R.id.btnRegister);
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountCreation.this, DiningEstablishments.class);
                startActivity(intent);
            }
        });

        Button LoginButton = findViewById(R.id.btnLogin);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountCreation.this, DiningEstablishments.class);
                startActivity(intent);
            }
        });

    }
}
