package com.example.sprintproject.views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sprintproject.R;
import com.example.sprintproject.databinding.ActivitySecondaryBinding;


public class SecondActivity extends AppCompatActivity {

    private ActivitySecondaryBinding binding;
    //getting error here, not sure why. app still works though.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ActivityCheck", "onCreate called in SecondActivity");
        super.onCreate(savedInstanceState);
        binding = ActivitySecondaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new LogisticsFragment());

        ImageButton accommodationsButton = findViewById(R.id.accommodationsicon);
        accommodationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new AccommodationsFragment());
            }
        });

        ImageButton travelButton = findViewById(R.id.communityicon);
        travelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new TravelCommunityFragment());
            }
        });

        ImageButton transportationButton = findViewById(R.id.transportationicon);
        transportationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new TransportationFragment());
            }
        });

        ImageButton logisticsButton = findViewById(R.id.logisticsicon);
        logisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new LogisticsFragment());
            }
        });

        ImageButton diningButton = findViewById(R.id.dining_establishmentsicon);
        diningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new DiningEstablishmentsFragment());
            }
        });

        ImageButton destinationButton = findViewById(R.id.destinationicon);
        destinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(DestinationFragment.getInstance());
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
