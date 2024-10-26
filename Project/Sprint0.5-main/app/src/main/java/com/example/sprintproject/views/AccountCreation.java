package com.example.sprintproject.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprintproject.R;
import com.google.firebase.auth.FirebaseAuth;

public class AccountCreation extends AppCompatActivity {

    private FirebaseAuth mAuth; // Firebase Authentication instance
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_creation); //creates the stuff on layout

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Find UI elements
        emailEditText = findViewById(R.id.register_email);
        passwordEditText = findViewById(R.id.register_password);
        Button registerButton = findViewById(R.id.registerButton);
        Button loginButton = findViewById(R.id.login_button);
        Button exitButton = findViewById(R.id.exitButton);

        // Exit button logic
        exitButton.setOnClickListener(v -> finish());

        // Login button logic
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountCreation.this, LoginPage.class);
                startActivity(intent);
            }
        });

        // Register button logic
        registerButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Validate input
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(AccountCreation.this,
                        "Please enter email", Toast.LENGTH_SHORT).show();
                return;

            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(AccountCreation.this,
                        "Please enter password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Register the user using Firebase Authentication
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Registration successful - Show success message
                            Toast.makeText(AccountCreation.this,
                                    "Registration Successful", Toast.LENGTH_SHORT).show();

                            // Placeholder for next screen (To be added later)
                            // startActivity(new Intent(RegisterActivity.this, NextActivity.class));
                        } else {
                            // Registration failed - Show error message
                            Toast.makeText(AccountCreation.this, "Registration Failed: "
                                    + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

    }
}
