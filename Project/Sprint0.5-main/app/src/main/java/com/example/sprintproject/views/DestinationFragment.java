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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.sprintproject.R;
import com.example.sprintproject.viewmodels.DestinationViewModel;

public class DestinationFragment extends Fragment {

    private DestinationViewModel viewModel;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DestinationViewModel.class);
        databaseReference = FirebaseDatabase.getInstance().getReference("travelLog");
        firebaseAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_destination, container, false);

        Button showTextFieldButton = view.findViewById(R.id.show_textfield_button);
        LinearLayout travelLogSection = view.findViewById(R.id.travel_log_section);
        EditText travelLocation = view.findViewById(R.id.travel_location);
        EditText estimatedStart = view.findViewById(R.id.estimated_start);
        EditText estimatedEnd = view.findViewById(R.id.estimated_end);
        Button cancelButton = view.findViewById(R.id.cancel_button);
        Button submitButton = view.findViewById(R.id.submit_button);

        Button calculateTravelDuration = view.findViewById(R.id.calculate_travel_button);
        LinearLayout calculateTravelSection = view.findViewById(R.id.calculate_travel_section);
        EditText startDate = view.findViewById(R.id.start_date);
        EditText endDate = view.findViewById(R.id.end_date);
        EditText duration = view.findViewById(R.id.duration);
        Button calculateFinal = view.findViewById(R.id.Calculate_button);

        LinearLayout calculationsSection = view.findViewById(R.id.calculations_section);
        TextView results = view.findViewById(R.id.result_label);
        Button resetButton = view.findViewById(R.id.reset_button);

        // Observe ViewModel
        viewModel.getDuration().observe(getViewLifecycleOwner(), durationValue -> {
            duration.setText(durationValue);
            results.setText("Duration: " + durationValue + " days");
            calculationsSection.setVisibility(View.VISIBLE);
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), message ->
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show()
        );

        showTextFieldButton.setOnClickListener(v -> {
            travelLogSection.setVisibility(travelLogSection.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            showTextFieldButton.setText(travelLogSection.getVisibility() == View.VISIBLE ? "Hide Travel Log" : "Log Travel");
        });

        calculateTravelDuration.setOnClickListener(v -> {
            calculateTravelSection.setVisibility(calculateTravelSection.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            calculateTravelDuration.setText(calculateTravelSection.getVisibility() == View.VISIBLE ? "Hide Calculations" : "Calculate Travel Duration");
        });

        calculateFinal.setOnClickListener(v -> {
            // Calculate the duration using the ViewModel method
            viewModel.calculateDuration(
                    startDate.getText().toString(),
                    endDate.getText().toString(),
                    duration.getText().toString()
            );

            // Store the values in Firebase after calculating the duration
            String start = startDate.getText().toString();
            String end = endDate.getText().toString();
            String durationText = duration.getText().toString();

            // Get current user's email and sanitize it for Firebase
            String userEmail = firebaseAuth.getCurrentUser().getEmail();
            String sanitizedEmail = userEmail.replace(".", ","); // Replace dots with commas

            // Create a unique key for each travel entry under the user's email
            DatabaseReference travelEntryRef = databaseReference.child(sanitizedEmail).push(); // Push generates a new unique key

            // Store travel details in Firebase
            travelEntryRef.child("startDate").setValue(start);
            travelEntryRef.child("endDate").setValue(end);
            travelEntryRef.child("duration").setValue(durationText)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Travel data saved successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to save travel data.", Toast.LENGTH_SHORT).show();
                    });
        });

        resetButton.setOnClickListener(v -> {
            // Trigger the ViewModel to reset the fields
            viewModel.resetFields();

            // Additionally, clear the EditText fields (optional, but ensures UI consistency)
            startDate.setText("");
            endDate.setText("");
            duration.setText("");
            travelLocation.setText("");  // Clear the travel location field if needed
            results.setText("XX");  // Reset any result display
        });

        cancelButton.setOnClickListener(v -> {
            travelLogSection.setVisibility(View.GONE);
            showTextFieldButton.setText("Log Travel");
            travelLocation.setText("");
            estimatedStart.setText("");
            estimatedEnd.setText("");
        });

        submitButton.setOnClickListener(v -> viewModel.logTravel(
                travelLocation.getText().toString(),
                estimatedStart.getText().toString(),
                estimatedEnd.getText().toString()
        ));

        return view;
    }
}
