package com.example.sprintproject.views;

import android.os.Bundle;
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

public class DestinationFragment extends Fragment {

    private DestinationViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DestinationViewModel.class);
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
            // Clear the results text
            results.setText("XX");

        resetButton.setOnClickListener(v -> viewModel.resetFields());

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
