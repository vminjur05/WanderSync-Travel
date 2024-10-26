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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DestinationFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public DestinationFragment() {
        // Required empty public constructor
    }

    public static DestinationFragment newInstance(String param1, String param2) {
        DestinationFragment fragment = new DestinationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        // Toggle the visibility of the travel log section
        showTextFieldButton.setOnClickListener(v -> {
            if (travelLogSection.getVisibility() == View.VISIBLE) {
                travelLogSection.setVisibility(View.GONE);
                showTextFieldButton.setText("Log Travel");
            } else {
                travelLogSection.setVisibility(View.VISIBLE);
                showTextFieldButton.setText("Hide Travel Log");
            }
        });

        calculateTravelDuration.setOnClickListener(v -> {
            if (calculateTravelSection.getVisibility() == View.VISIBLE) {
                calculateTravelSection.setVisibility(View.GONE);
                calculateTravelDuration.setText("Calculate Travel Duration");
            } else {
                calculateTravelSection.setVisibility(View.VISIBLE);
                calculateTravelDuration.setText("Hide Calculations");
            }
        });

        calculateFinal.setOnClickListener(v -> {
            String start = startDate.getText().toString();
            String end = endDate.getText().toString();
            String durationText = duration.getText().toString();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            try {
                if (!start.isEmpty() && !end.isEmpty()) {
                    // Calculate duration based on start and end dates
                    Date startDateParsed = dateFormat.parse(start);
                    Date endDateParsed = dateFormat.parse(end);
                    long diffInMillis = endDateParsed.getTime() - startDateParsed.getTime();
                    long days = diffInMillis / (1000 * 60 * 60 * 24);
                    results.setText("Duration: " + days + " days");
                    duration.setText(String.valueOf(days));
                } else if (!start.isEmpty() && !durationText.isEmpty()) {
                    // Calculate end date based on start date and duration
                    Date startDateParsed = dateFormat.parse(start);
                    int days = Integer.parseInt(durationText);
                    long endInMillis = startDateParsed.getTime() + days * (1000 * 60 * 60 * 24);
                    Date endDateCalculated = new Date(endInMillis);
                    endDate.setText(dateFormat.format(endDateCalculated));
                    results.setText("Duration: " + days + " days");
                } else if (!end.isEmpty() && !durationText.isEmpty()) {
                    // Calculate start date based on end date and duration
                    Date endDateParsed = dateFormat.parse(end);
                    int days = Integer.parseInt(durationText);
                    long startInMillis = endDateParsed.getTime() - days * (1000 * 60 * 60 * 24);
                    Date startDateCalculated = new Date(startInMillis);
                    startDate.setText(dateFormat.format(startDateCalculated));
                    results.setText("Duration: " + days + " days");
                } else {
                    Toast.makeText(getContext(), "Please provide at least two values", Toast.LENGTH_SHORT).show();
                }
                calculationsSection.setVisibility(View.VISIBLE);
            } catch (ParseException e) {
                Toast.makeText(getContext(), "Invalid date format. Please use yyyy-MM-dd.", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid duration value.", Toast.LENGTH_SHORT).show();
            }
        });

        resetButton.setOnClickListener(v -> {
            // Clear the input fields
            startDate.setText("");
            endDate.setText("");
            duration.setText("");

            // Clear the results text
            results.setText("XX");

        });

        // Handle the "Cancel" button click
        cancelButton.setOnClickListener(v -> {
            // Hide the travel log section and clear the fields
            travelLogSection.setVisibility(View.GONE);
            showTextFieldButton.setText("Log Travel");

            // Clear the input fields
            travelLocation.setText("");
            estimatedStart.setText("");
            estimatedEnd.setText("");
        });

        // Handle the "Submit" button click
        submitButton.setOnClickListener(v -> {
            String location = travelLocation.getText().toString();
            String start = estimatedStart.getText().toString();
            String end = estimatedEnd.getText().toString();

            // Here you can handle form validation and data submission
            if (location.isEmpty() || start.isEmpty() || end.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Travel logged: " + location, Toast.LENGTH_SHORT).show();

                // Hide the travel log section and reset the fields
                travelLogSection.setVisibility(View.GONE);
                showTextFieldButton.setText("Log Travel");
                travelLocation.setText("");
                estimatedStart.setText("");
                estimatedEnd.setText("");
            }
        });

        return view;
    }
}

