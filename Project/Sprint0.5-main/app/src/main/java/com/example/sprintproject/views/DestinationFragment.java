package com.example.sprintproject.views;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sprintproject.R;
import com.example.sprintproject.viewmodels.DestinationViewModel;
import com.example.sprintproject.viewmodels.DestinationViewModel.DurationResult;
import com.example.sprintproject.model.FirebaseDatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DestinationFragment extends Fragment {

    private static DestinationFragment instance;
    private DestinationViewModel viewModel;
    private DatabaseReference destinationsReference;
    private DatabaseReference travelLogReference;
    private FirebaseAuth firebaseAuth;

    // Private constructor to prevent instantiation
    private DestinationFragment() { }

    // Thread-safe method to get the single instance of DestinationFragment
    public static synchronized DestinationFragment getInstance() {
        if (instance == null) {
            instance = new DestinationFragment();
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewModel with singleton instance
        viewModel = DestinationViewModel.getInstance(requireActivity().getApplication());

        // Initialize Firebase references using singleton FirebaseDatabaseHelper
        destinationsReference = FirebaseDatabaseHelper.getInstance().getDestinationsReference();
        travelLogReference = FirebaseDatabaseHelper.getInstance().getTravelLogReference();

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
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
        LinearLayout recentTripsLayout = view.findViewById(R.id.recent_trips_layout);
        // Layout for displaying recent trips
        loadRecentTrips(recentTripsLayout); // Load recent trips when the fragment is created

        // Travel Log Section Toggle
        showTextFieldButton.setOnClickListener(v -> {
            travelLogSection.setVisibility(travelLogSection.getVisibility()
                    == View.VISIBLE ? View.GONE : View.VISIBLE);
            showTextFieldButton.setText(travelLogSection.getVisibility()
                    == View.VISIBLE ? "Hide Travel Log" : "Log Travel");
        });

        // Submit Button for Travel Log
        submitButton.setOnClickListener(v -> {
            String location = travelLocation.getText().toString().trim();
            String start = estimatedStart.getText().toString().trim();
            String end = estimatedEnd.getText().toString().trim();

            if (location.isEmpty() || start.isEmpty() || end.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields",
                        Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "Travel log saved successfully!",
                                Toast.LENGTH_SHORT).show();
                        travelLocation.setText("");
                        estimatedStart.setText("");
                        estimatedEnd.setText("");
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to save travel log.",
                                Toast.LENGTH_SHORT).show();
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
            calculateTravelSection.setVisibility(calculateTravelSection.getVisibility()
                    == View.VISIBLE ? View.GONE : View.VISIBLE);
            calculateTravelDuration.setText(calculateTravelSection.getVisibility()
                    == View.VISIBLE ? "Hide Calculations" : "Calculate Travel Duration");
        });

        // Calculate Final Duration Button
        calculateFinal.setOnClickListener(v -> {
            String start = startDate.getText().toString().trim();
            String end = endDate.getText().toString().trim();
            String durationStr = duration.getText().toString().trim();

            DurationResult result = null;

            if (!start.isEmpty() && !end.isEmpty() && durationStr.isEmpty()
                    || !start.isEmpty() && !end.isEmpty() && !durationStr.isEmpty()) {
                // Case 1: Start and End are provided; calculate duration (or if all are provided)
                result = viewModel.calculateDuration(start, end, "");

            } else if (start.isEmpty() && !end.isEmpty() && !durationStr.isEmpty()) {
                // Case 2: End and Duration are provided; calculate start date
                result = viewModel.calculateDuration("", end, durationStr);

            } else if (!start.isEmpty() && end.isEmpty() && !durationStr.isEmpty()) {
                // Case 3: Start and Duration are provided; calculate end date
                result = viewModel.calculateDuration(start, "", durationStr);

            } else {
                // Invalid input: prompt user to fill exactly 2 fields
                Toast.makeText(getContext(), "Please fill in at least 2 out of 3 fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            // Calculate the duration and display it

            if (result.getDuration() <= 0) {
                Toast.makeText(getContext(), "Start date should not be after the end date!",
                        Toast.LENGTH_SHORT).show();
                //enforces the start date being before the end date
                return;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            // Format startDate and endDate for display
            String formattedStartDate = dateFormat.format(result.getStartDate());
            String formattedEndDate = dateFormat.format(result.getEndDate());

            startDate.setText(String.valueOf(formattedStartDate));
            endDate.setText(String.valueOf(formattedEndDate));
            duration.setText(String.valueOf(result.getDuration()));
            results.setText("Duration: " + result.getDuration() + " days");
            calculationsSection.setVisibility(View.VISIBLE);

            // Save calculation result under travelLog in Firebase
            String userEmail = firebaseAuth.getCurrentUser().getEmail();
            String sanitizedEmail = userEmail.replace(".", ",");
            DatabaseReference destinationEntryRef = travelLogReference.child(sanitizedEmail).push();

            destinationEntryRef.child("startDate").setValue(formattedStartDate);
            destinationEntryRef.child("endDate").setValue(formattedEndDate);
            destinationEntryRef.child("duration").setValue(String.valueOf(result.getDuration()))
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Travel duration saved successfully!",
                                Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to save travel duration.",
                                Toast.LENGTH_SHORT).show();
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
                .limitToLast(5)
                .get()
                .addOnSuccessListener(dataSnapshot -> {
                    recentTripsLayout.removeAllViews();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot tripSnapshot : dataSnapshot.getChildren()) {
                            String location = tripSnapshot.child("location").getValue(String.class);
                            String start = tripSnapshot.child("estimatedStart")
                                    .getValue(String.class);
                            String end = tripSnapshot.child("estimatedEnd")
                                    .getValue(String.class);

                            TextView tripView = new TextView(getContext());
                            tripView.setText("Location: " + location + "\nStart: "
                                    + start + "\nEnd: " + end);
                            tripView.setPadding(0, 10, 0, 10);
                            recentTripsLayout.addView(tripView);
                        }
                    } else {
                        TextView noTripsView = new TextView(getContext());
                        noTripsView.setText("No recent trips found.");
                        recentTripsLayout.addView(noTripsView);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load recent trips.",
                            Toast.LENGTH_SHORT).show(); //case if trips is null
                });
    }
}