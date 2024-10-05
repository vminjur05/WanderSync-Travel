package com.example.sprintproject;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sprintproject.databinding.ActivitySecondaryBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class SecondActivity extends AppCompatActivity {

    ActivitySecondaryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecondaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new LogisticsFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        for (int i = 0; i < bottomNavigationView.getChildCount(); i++) {
            View view = bottomNavigationView.getChildAt(i);
            view.setPadding(0, 0, 0, 0);  // Removes default padding
            view.requestLayout();  // Forces the view to re-layout
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.logisticsicon) {
                replaceFragment(new LogisticsFragment());
            } else if (item.getItemId() == R.id.accommodationsicon) {
                replaceFragment(new AccommodationsFragment());
            } else if (item.getItemId() == R.id.travel_communityicon) {
                replaceFragment(new TravelCommunityFragment());
            } else if (item.getItemId() == R.id.transportationicon) {
                replaceFragment(new TransportationFragment());
            } else if (item.getItemId() == R.id.destinationicon) {
                replaceFragment(new LogisticsFragment());
            } else if (item.getItemId() == R.id.dining_establishmentsicon) {
                replaceFragment(new DiningEstablishmentsFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();


    }
}
