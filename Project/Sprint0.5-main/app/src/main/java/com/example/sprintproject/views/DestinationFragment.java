package com.example.sprintproject.views;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.sprintproject.R;
import com.example.sprintproject.viewmodels.DestinationViewModel;

public class DestinationFragment extends Fragment {

    private DestinationViewModel viewModel;
    private DatabaseReference destinationsReference;
    private DatabaseReference travelLogReference;
    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DestinationViewModel.class);
        destinationsReference = FirebaseDatabase.getInstance().getReference("destinations");
        travelLogReference = FirebaseDatabase.getInstance().getReference("travelLog");
        firebaseAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_destination, container, false);

        // UI Elements for Travel Log
        Button showTextFieldButton = view.findViewById(R.id.show_textfield_button);
        LinearLayout travelLogSection = view.findViewById(R.id.travel_log_section);
        EditText travelLocation = view.findViewById(R.id.travel_location);
        EditText estimatedStart = view.findViewById(R.id.estimated_start);
        EditText estimatedEnd = view.findViewById(R.id.estimated_end);
        Button submitButton = view.findViewById(R.id.submit_button);
        Button cancelButton = view.findViewById(R.id.cancel_button);

        // UI Elements for Travel Duration Calculation
        Button calculateTravelDuration = view.findViewById(R.id.calculate_travel_button);
        LinearLayout calculateTravelSection = view.findViewById(R.id.calculate_travel_section);
        EditText startDate = view.findViewById(R.id.start_date);
        EditText endDate = view.findViewById(R.id.end_date);
        EditText duration = view.findViewById(R.id.duration);
        Button calculateFinal = view.findViewById(R.id.Calculate_button);
        Button resetButton = view.findViewById(R.id.reset_button);
        TextView results = view.findViewById(R.id.result_label);
        LinearLayout calculationsSection = view.findViewById(R.id.calculations_section);

        // Recent Trips Layout
        LinearLayout recentTripsLayout = view.findViewById(R.id.recent_trips_layout); // Layout for displaying recent trips
        loadRecentTrips(recentTripsLayout); // Load recent trips when the fragment is created

        // Travel Log Section Toggle
        showTextFieldButton.setOnClickListener(v -> {
            travelLogSection.setVisibility(travelLogSection.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            showTextFieldButton.setText(travelLogSection.getVisibility() == View.VISIBLE ? "Hide Travel Log" : "Log Travel");
        });

        // Submit Button for Travel Log
        submitButton.setOnClickListener(v -> {
            String location = travelLocation.getText().toString().trim();
            String start = estimatedStart.getText().toString().trim();
            String end = estimatedEnd.getText().toString().trim();

            if (location.isEmpty() || start.isEmpty() || end.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get current user's email and sanitize it for Firebase
            String userEmail = firebaseAuth.getCurrentUser().getEmail();
            String sanitizedEmail = userEmail.replace(".", ","); // Replace dots with commas

            // Store travel log details in Firebase under destinations
            DatabaseReference travelEntryRef = destinationsReference.child(sanitizedEmail).push();
            travelEntryRef.child("location").setValue(location);
            travelEntryRef.child("estimatedStart").setValue(start);
            travelEntryRef.child("estimatedEnd").setValue(end)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Travel log saved successfully!", Toast.LENGTH_SHORT).show();
                        travelLocation.setText("");
                        estimatedStart.setText("");
                        estimatedEnd.setText("");
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to save travel log.", Toast.LENGTH_SHORT).show();
                    });
        });

        // Cancel Button for Travel Log
        cancelButton.setOnClickListener(v -> {
            travelLogSection.setVisibility(View.GONE);
            showTextFieldButton.setText("Log Travel");
            travelLocation.setText("");
            estimatedStart.setText("");
            estimatedEnd.setText("");
        });

        // Calculate Travel Duration Section Toggle
        calculateTravelDuration.setOnClickListener(v -> {
            calculateTravelSection.setVisibility(calculateTravelSection.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            calculateTravelDuration.setText(calculateTravelSection.getVisibility() == View.VISIBLE ? "Hide Calculations" : "Calculate Travel Duration");
        });

        // Calculate Final Duration Button
        calculateFinal.setOnClickListener(v -> {
            String start = startDate.getText().toString().trim();
            String end = endDate.getText().toString().trim();

            if (start.isEmpty() || end.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in both dates", Toast.LENGTH_SHORT).show();
                return;
            }

            // Calculate the duration and display it
            long durationInDays = viewModel.calculateDuration(start, end);
            duration.setText(String.valueOf(durationInDays));
            results.setText("Duration: " + durationInDays + " days");
            calculationsSection.setVisibility(View.VISIBLE);

            // Save calculation result under travelLog in Firebase
            String userEmail = firebaseAuth.getCurrentUser().getEmail();
            String sanitizedEmail = userEmail.replace(".", ",");
            DatabaseReference destinationEntryRef = travelLogReference.child(sanitizedEmail).push();

            destinationEntryRef.child("startDate").setValue(start);
            destinationEntryRef.child("endDate").setValue(end);
            destinationEntryRef.child("duration").setValue(String.valueOf(durationInDays))
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Travel duration saved successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to save travel duration.", Toast.LENGTH_SHORT).show();
                    });
        });

        // Reset Button for Calculation Section
        resetButton.setOnClickListener(v -> {
            startDate.setText("");
            endDate.setText("");
            duration.setText("");
            results.setText("");
            calculationsSection.setVisibility(View.GONE);
        });

        return view;
    }

    // Method to load and display recent trips
    private void loadRecentTrips(LinearLayout recentTripsLayout) {
        String userEmail = firebaseAuth.getCurrentUser().getEmail();
        String sanitizedEmail = userEmail.replace(".", ",");

        destinationsReference.child(sanitizedEmail)
                .orderByKey()
                .limitToLast(5) // Fetch last 5 trips
                .get()
                .addOnSuccessListener(dataSnapshot -> {
                    recentTripsLayout.removeAllViews(); // Clear any existing views
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot tripSnapshot : dataSnapshot.getChildren()) {
                            String location = tripSnapshot.child("location").getValue(String.class);
                            String start = tripSnapshot.child("estimatedStart").getValue(String.class);
                            String end = tripSnapshot.child("estimatedEnd").getValue(String.class);

                            // Create TextView for each trip
                            TextView tripView = new TextView(getContext());
                            tripView.setText("Location: " + location + "\nStart: " + start + "\nEnd: " + end);
                            tripView.setPadding(0, 10, 0, 10); // Optional styling
                            recentTripsLayout.addView(tripView);
                        }
                    } else {
                        TextView noTripsView = new TextView(getContext());
                        noTripsView.setText("No recent trips found.");
                        recentTripsLayout.addView(noTripsView);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load recent trips.", Toast.LENGTH_SHORT).show();
                });
    }
}
