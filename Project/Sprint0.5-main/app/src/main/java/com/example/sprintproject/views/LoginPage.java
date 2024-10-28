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

public class LoginPage extends AppCompatActivity {

    private FirebaseAuth mAuth; // Firebase Authentication instance
    private EditText usernameEditText; // Renamed to match XML
    private EditText passwordEditText; // Renamed to match XML
    private String email;

    public LoginPage() {
        email = "";
    }

    public String getEmail() {
        return email;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page); // Make sure this matches your XML layout file name

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Match variable names with XML IDs
        // Matches XML ID for the username field
        usernameEditText = findViewById(R.id.username);
        // Matches XML ID for the password field
        passwordEditText = findViewById(R.id.password);
        // Matches XML ID for the login button
        Button loginButton = findViewById(R.id.login_button);
        // Matches XML ID for the "Create an Account" button
        Button createAccountButton = findViewById(R.id.btn_createacc);
        // Matches XML ID for the exit button
        Button exitButton = findViewById(R.id.exitButton);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        // Create Account button logic
        createAccountButton.setOnClickListener(v -> {
            // Start the AccountCreation activity when "Create an Account" is clicked
            // Replace with your account creation activity
            startActivity(new Intent(LoginPage.this, AccountCreation.class));
        });

        // Login button logic
        loginButton.setOnClickListener(v -> {
            // Get user input
            email = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Validate input
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(LoginPage.this, "Please enter email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(LoginPage.this, "Please enter password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Firebase authentication to check if user exists
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Login successful - Show success message
                            Toast.makeText(LoginPage.this, "Login Successful", Toast.LENGTH_SHORT)
                                    .show();

                            // Placeholder for next screen (e.g., homepage or dashboard)
                            startActivity(new Intent(LoginPage.this, SecondActivity.class));

                        } else {
                            // Login failed - Show error message
                            Toast.makeText(LoginPage.this, "Login Failed: "
                                    + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
