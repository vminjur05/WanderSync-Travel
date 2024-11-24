package com.example.sprintproject.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sprintproject.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ActivityCheck", "onCreate called in MainActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //hi
        Button startButton = findViewById(R.id.btnStart);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginPage.class);
                startActivity(intent);
            }
        });
        Button quitButton = findViewById(R.id.btnQuit); //button
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    } //RIP
} //comment because i didnt properly pull code last time ( ･w･)つ━☆・*
// im just adding more comments...
// just adding more comments...