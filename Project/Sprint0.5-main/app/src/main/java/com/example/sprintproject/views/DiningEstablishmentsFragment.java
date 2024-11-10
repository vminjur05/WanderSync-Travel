package com.example.sprintproject.views;

import android.os.Bundle;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.sprintproject.model.Reservation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import com.example.sprintproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiningEstablishmentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiningEstablishmentsFragment extends Fragment {

    private DatabaseReference reservationDatabase;
    private TextView locationTextView, websiteTextView, reviewsTextView, reservationTimeTextView;
    private EditText inputLocation, inputWebsite, inputReservationTime;
    private Button submitReservationButton;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public DiningEstablishmentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DiningEstablishmentsFragment.
     */
    public static DiningEstablishmentsFragment newInstance(String param1, String param2) {
        DiningEstablishmentsFragment fragment = new DiningEstablishmentsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reservationDatabase = FirebaseDatabase.getInstance().getReference("reservation");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dining_establishments, container, false);

        // Initialize display TextViews
        locationTextView = view.findViewById(R.id.locationTextView);
        websiteTextView = view.findViewById(R.id.websiteTextView);
        reviewsTextView = view.findViewById(R.id.reviewsTextView);
        reservationTimeTextView = view.findViewById(R.id.reservationTimeTextView);

        // Initialize input fields and button
        inputLocation = view.findViewById(R.id.inputLocation);
        inputWebsite = view.findViewById(R.id.inputWebsite);
        inputReservationTime = view.findViewById(R.id.inputReservationTime);
        submitReservationButton = view.findViewById(R.id.submitReservationButton);

        // Load data from Firebase
        loadDataFromDatabase();

        // Set OnClickListener for the add reservation button
        submitReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewReservation();
            }
        });

        return view;
    }

    private void addNewReservation() {
        // Retrieve input values
        String location = inputLocation.getText().toString().trim();
        String website = inputWebsite.getText().toString().trim();
        String reservationTime = inputReservationTime.getText().toString().trim();

        if (location.isEmpty() || website.isEmpty() || reservationTime.isEmpty()) {
            // Show an error message if any field is empty
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new reservation entry
        String reservationId = reservationDatabase.push().getKey();
        Reservation newReservation = new Reservation(location, website, reservationTime);

        // Save to Firebase under a unique key
        if (reservationId != null) {
            reservationDatabase.child(reservationId).setValue(newReservation)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Reservation added", Toast.LENGTH_SHORT).show();
                            inputLocation.setText("");
                            inputWebsite.setText("");
                            inputReservationTime.setText("");
                        } else {
                            Toast.makeText(getContext(), "Failed to add reservation", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void loadDataFromDatabase() {
        reservationDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot establishmentSnapshot : dataSnapshot.getChildren()) {
                    String location = establishmentSnapshot.child("location").getValue(String.class);
                    String website = establishmentSnapshot.child("website").getValue(String.class);
                    Double reviews = establishmentSnapshot.child("reviews").getValue(Double.class);
                    String reservationTime = establishmentSnapshot.child("reservationTime").getValue(String.class);

                    // Display the retrieved information in the TextViews, handling potential null values
                    locationTextView.setText(location != null ? location : "N/A");
                    websiteTextView.setText(website != null ? website : "N/A");
                    reviewsTextView.setText(reviews != null ? String.valueOf(reviews) : "N/A");
                    reservationTimeTextView.setText(reservationTime != null ? reservationTime : "N/A");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

}