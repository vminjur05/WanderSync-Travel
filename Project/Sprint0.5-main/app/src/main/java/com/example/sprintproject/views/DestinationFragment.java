package com.example.sprintproject.views;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sprintproject.R;

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

