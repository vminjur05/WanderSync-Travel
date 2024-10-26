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

import com.example.sprintproject.R;
import com.example.sprintproject.viewmodels.DestinationViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DestinationFragment extends Fragment {

    private DestinationViewModel viewModel;
    DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DestinationViewModel.class);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.setValue("travelLog").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(), "Database created", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Database not created", Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getContext(), "Database created", Toast.LENGTH_SHORT).show();
            }
        });
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

        calculateFinal.setOnClickListener(v -> viewModel.calculateDuration(
                startDate.getText().toString(),
                endDate.getText().toString(),
                duration.getText().toString()
        ));

        results.setText("XX");

        resetButton.setOnClickListener(v -> {
            viewModel.resetFields();
            startDate.setText("");
            endDate.setText("");
            duration.setText("");
            travelLocation.setText("");
            results.setText("XX");
        });

        cancelButton.setOnClickListener(v -> {
            travelLogSection.setVisibility(View.GONE);
            showTextFieldButton.setText("Log Travel");
            travelLocation.setText("");
            estimatedStart.setText("");
            estimatedEnd.setText("");
        });

        submitButton.setOnClickListener(v -> {
            String userId = "someUniqueUserId"; // Replace with actual user ID, e.g., FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference userTripsRef = databaseReference.child("users").child(userId).child("trips").push();

            String location = travelLocation.getText().toString();
            String start = estimatedStart.getText().toString();
            String end = estimatedEnd.getText().toString();

            Map<String, Object> tripData = new HashMap<>();
            tripData.put("location", location);
            tripData.put("estimatedStart", start);
            tripData.put("estimatedEnd", end);

            userTripsRef.setValue(tripData)
                    .addOnSuccessListener(unused -> Toast.makeText(getContext(), "Trip logged successfully!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to log trip", Toast.LENGTH_SHORT).show());
        });

        return view;
    }
}
