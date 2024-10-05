package com.example.sprintproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountCreation extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_creation);

        auth = FirebaseAuth.getInstance();  // Initialize Firebase Auth

        emailEditText = findViewById(R.id.etUsername);
        passwordEditText = findViewById(R.id.etPassword);

        Button registerButton = findViewById(R.id.btnRegister);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    createAccount(email, password);
                } else {
                    Toast.makeText(AccountCreation.this, "Email or Password cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button loginButton = findViewById(R.id.btnLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountCreation.this, LoginPage.class);
                startActivity(intent);
            }
        });
    }

    private void createAccount(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        Toast.makeText(AccountCreation.this, "Account Created", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AccountCreation.this, SecondActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(AccountCreation.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
