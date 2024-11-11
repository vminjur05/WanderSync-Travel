package com.example.sprintproject.views;

import com.google.firebase.auth.FirebaseAuth;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import android.widget.Button;
import android.widget.EditText;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;
import com.example.sprintproject.R;

public class DiningEstablishmentsFragment extends Fragment {

    private RecyclerView reservationsRecyclerView;
    private ReservationAdapter reservationAdapter;
    private List<Reservation> reservationList = new ArrayList<>();
    private DatabaseReference reservationDatabase;
    private EditText inputLocation, inputWebsite, inputReservationTime;
    private Button submitReservationButton, sortReservationsButton;
    private String sanitizedEmail;
    private boolean sortOrderAscending = true; // Boolean to track sort order

    public DiningEstablishmentsFragment() {
        // Required empty public constructor
    }

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

        // Retrieve the user's email for Firebase key compatibility
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (userEmail != null) {
            sanitizedEmail = userEmail.replace(".", ",");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dining_establishments, container, false);

        reservationsRecyclerView = view.findViewById(R.id.reservationsRecyclerView);
        reservationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reservationAdapter = new ReservationAdapter(reservationList, reservationDatabase, sanitizedEmail);
        reservationsRecyclerView.setAdapter(reservationAdapter);

        // Initialize input fields and button
        inputLocation = view.findViewById(R.id.inputLocation);
        inputWebsite = view.findViewById(R.id.inputWebsite);
        inputReservationTime = view.findViewById(R.id.inputReservationTime);
        submitReservationButton = view.findViewById(R.id.submitReservationButton);
        sortReservationsButton = view.findViewById(R.id.sortByDateButton);

        // Load data from Firebase, sorted by earliest date by default
        loadDataFromDatabase();

        // Set OnClickListener for the add reservation button
        submitReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewReservation();
            }
        });

        // Set OnClickListener for the sort reservations button
        sortReservationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSortOrder();
            }
        });

        return view;
    }

    private void addNewReservation() {
        // Retrieve input values and normalize them
        String location = inputLocation.getText().toString().trim().toLowerCase();
        String website = inputWebsite.getText().toString().trim().toLowerCase();
        String reservationTimeInput = inputReservationTime.getText().toString().trim();

        if (location.isEmpty() || website.isEmpty() || reservationTimeInput.isEmpty()) {
            // Show an error message if any field is empty
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse and format reservationTime input to ensure it's in the format `yyyy-MM-dd HH:mm`
        try {
            // Hardcode the year to 2024
            int currentYear = 2024;

            // Adjust the input to match the required format with AM/PM
            String dateWithYear = currentYear + "-" + reservationTimeInput;

            // Parse and format the input with AM/PM
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault()); // Changed to 12-hour format with AM/PM
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()); // 24-hour format for storage

            Date parsedDate = inputFormat.parse(dateWithYear);
            String formattedReservationTime = outputFormat.format(parsedDate);

            // Check for duplicate location, time, and website
            checkForDuplicateReservation(location, formattedReservationTime, website, () -> {
                // No duplicate found, proceed with adding the reservation
                String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                if (userEmail == null) {
                    Toast.makeText(getContext(), "Error: User not logged in.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String sanitizedEmail = userEmail.replace(".", ",");
                String reservationId = reservationDatabase.child(sanitizedEmail).push().getKey();

                if (reservationId != null) {
                    Reservation newReservation = new Reservation(reservationId, location, website, formattedReservationTime);
                    reservationDatabase.child(sanitizedEmail).child(reservationId).setValue(newReservation)
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
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), "Invalid date format. Please use MM-dd hh:mm a (AM/PM)", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void checkForDuplicateReservation(String location, String reservationTime, String website, Runnable onNoDuplicate) {
        String normalizedLocation = location.trim().toLowerCase();
        String normalizedReservationTime = reservationTime.trim();
        String normalizedWebsite = website.trim().toLowerCase();

        reservationDatabase.child(sanitizedEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        boolean duplicateFound = false;

                        for (DataSnapshot reservationSnapshot : snapshot.getChildren()) {
                            Reservation existingReservation = reservationSnapshot.getValue(Reservation.class);
                            if (existingReservation != null) {
                                String existingLocation = existingReservation.getLocation().trim().toLowerCase();
                                String existingTime = existingReservation.getReservationTime().trim();
                                String existingWebsite = existingReservation.getWebsite().trim().toLowerCase();

                                if (existingLocation.equals(normalizedLocation) &&
                                        existingTime.equals(normalizedReservationTime) &&
                                        existingWebsite.equals(normalizedWebsite)) {
                                    duplicateFound = true;
                                    break;
                                }
                            }
                        }

                        if (duplicateFound) {
                            Toast.makeText(getContext(), "Reservation at this location, time, and website already exists.", Toast.LENGTH_SHORT).show();
                        } else {
                            onNoDuplicate.run();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(getContext(), "Error checking duplicates: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadDataFromDatabase() {
        reservationDatabase.child(sanitizedEmail).addValueEventListener(new ValueEventListener() { // better syntax
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { // hi
                reservationList.clear();
                for (DataSnapshot reservationSnapshot : dataSnapshot.getChildren()) {
                    Reservation reservation = reservationSnapshot.getValue(Reservation.class);
                    if (reservation != null) {
                        reservation.setId(reservationSnapshot.getKey()); // Ensure the reservation has its ID set
                        reservationList.add(reservation);
                    }
                }
                // Sort by earliest date by default
                sortReservationsByDateTime(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleSortOrder() {
        sortOrderAscending = !sortOrderAscending; // Toggle the sort order
        sortReservationsByDateTime(sortOrderAscending);
    }

    private void sortReservationsByDateTime(boolean ascending) {
        // Sort reservations by date and time
        Collections.sort(reservationList, new Comparator<Reservation>() {
            @Override
            public int compare(Reservation r1, Reservation r2) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                    Date date1 = dateFormat.parse(r1.getReservationTime());
                    Date date2 = dateFormat.parse(r2.getReservationTime());
                    return ascending ? date1.compareTo(date2) : date2.compareTo(date1);
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
        reservationAdapter.notifyDataSetChanged();
    }
}
