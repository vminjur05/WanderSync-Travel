package com.example.sprintproject.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sprintproject.R;
import com.example.sprintproject.model.Accommodation;
import com.example.sprintproject.viewmodels.AccommodationViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AccommodationsFragment extends Fragment {

    private DatabaseReference accommodationsReference;
    private FirebaseAuth firebaseAuth;
    private List<Accommodation> accommodationList;
    private AccommodationAdapter accommodationAdapter;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private AccommodationViewModel accommodationViewModel;
    // Adjust format if needed

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accommodationsReference = FirebaseDatabase.getInstance().getReference("accommodations");
        firebaseAuth = FirebaseAuth.getInstance();
        accommodationList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accommodations, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.accommodationsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        accommodationAdapter = new AccommodationAdapter(accommodationList);
        recyclerView.setAdapter(accommodationAdapter);

        FloatingActionButton fabPopup = view.findViewById(R.id.fab_popup);
        fabPopup.setOnClickListener(v -> showPopupDialog());

        // Get sort buttons
        Button sortByCheckInButton = view.findViewById(R.id.sortByCheckInButton);
        Button sortByCheckOutButton = view.findViewById(R.id.sortByCheckOutButton);
        Button filterByDateButton = view.findViewById(R.id.filterByDateButton);

        // Sort by Check-in Date
        sortByCheckInButton.setOnClickListener(v -> sortAccommodationsByCheckInDate());

        // Sort by Check-out Date
        sortByCheckOutButton.setOnClickListener(v -> sortAccommodationsByCheckOutDate());

        // Filter by Date
        filterByDateButton.setOnClickListener(v -> showDateFilterDialog());

        loadAccommodations();

        accommodationViewModel = new ViewModelProvider(this).get(AccommodationViewModel.class);

        // Observe LiveData from ViewModel
        accommodationViewModel.getAccommodations().observe(getViewLifecycleOwner(), new Observer<List<Accommodation>>() {

            public void onChanged(List<Accommodation> accommodations) {
                // Update RecyclerView when data changes
                accommodationAdapter.setAccommodations(accommodations);
            }
        });

        return view;
    }

    //method
    private void sortAccommodationsByCheckInDate() {
        // Sorting by Check-in Date
        accommodationList.sort((a1, a2) -> {
            try {
                Date checkIn1 = dateFormat.parse(a1.getCheckInDate());
                Date checkIn2 = dateFormat.parse(a2.getCheckInDate());
                return checkIn1.compareTo(checkIn2);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        });
        accommodationAdapter.notifyDataSetChanged();
    }

    //method
    private void sortAccommodationsByCheckOutDate() {
        // Sorting by Check-out Date
        accommodationList.sort((a1, a2) -> {
            try {
                Date checkOut1 = dateFormat.parse(a1.getCheckOutDate());
                Date checkOut2 = dateFormat.parse(a2.getCheckOutDate());
                return checkOut1.compareTo(checkOut2);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        });
        accommodationAdapter.notifyDataSetChanged();
    }

    private void loadAccommodations() {
        String userEmail = firebaseAuth.getCurrentUser().getEmail();
        String sanitizedEmail = userEmail.replace(".", ",");
        accommodationsReference.child(sanitizedEmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                accommodationList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Accommodation accommodation = dataSnapshot.getValue(Accommodation.class);
                    accommodationList.add(accommodation);
                }
                accommodationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load accommodations.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPopupDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.accommodations_popup);
        dialog.setCancelable(true);

        EditText checkInField = dialog.findViewById(R.id.check_in_field);
        EditText checkOutField = dialog.findViewById(R.id.check_out_field);
        EditText locationField = dialog.findViewById(R.id.location_field);
        Spinner roomsSpinner = dialog.findViewById(R.id.rooms_spinner);
        Spinner roomTypeSpinner = dialog.findViewById(R.id.room_type_spinner);
        Button submitButton = dialog.findViewById(R.id.submit_button);

        submitButton.setOnClickListener(v -> {
            String checkIn = checkInField.getText().toString().trim();
            String checkOut = checkOutField.getText().toString().trim();
            String location = locationField.getText().toString().trim();
            String numberOfRooms = roomsSpinner.getSelectedItem().toString();
            String roomType = roomTypeSpinner.getSelectedItem().toString();

            if (checkIn.isEmpty() || checkOut.isEmpty() || location.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate the date range
            try {
                Date checkInDate = dateFormat.parse(checkIn);
                Date checkOutDate = dateFormat.parse(checkOut);
                if (checkInDate.after(checkOutDate)) {
                    Toast.makeText(getContext(), "Check-in date cannot be after check-out date.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (ParseException e) {
                Toast.makeText(getContext(), "Invalid date format.", Toast.LENGTH_SHORT).show();
                return;
            }

            String userEmail = firebaseAuth.getCurrentUser().getEmail();
            String sanitizedEmail = userEmail.replace(".", ",");

            // Check for duplicates
            accommodationsReference.child(sanitizedEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean isDuplicate = false;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String existingCheckIn = dataSnapshot.child("checkInDate").getValue(String.class);
                        String existingCheckOut = dataSnapshot.child("checkOutDate").getValue(String.class);
                        String existingLocation = dataSnapshot.child("location").getValue(String.class);
                        String existingNumberOfRooms = dataSnapshot.child("numberOfRooms").getValue(String.class);
                        String existingRoomType = dataSnapshot.child("roomType").getValue(String.class);

                        if (checkIn.equals(existingCheckIn) &&
                                checkOut.equals(existingCheckOut) &&
                                location.equals(existingLocation) &&
                                numberOfRooms.equals(existingNumberOfRooms) &&
                                roomType.equals(existingRoomType)) {
                            isDuplicate = true;
                            break;
                        }
                    }

                    if (isDuplicate) {
                        Toast.makeText(getContext(), "This accommodation already exists.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Save the new accommodation
                        DatabaseReference accommodationEntryRef = accommodationsReference.child(sanitizedEmail).push();
                        accommodationEntryRef.child("checkInDate").setValue(checkIn);
                        accommodationEntryRef.child("checkOutDate").setValue(checkOut);
                        accommodationEntryRef.child("location").setValue(location);
                        accommodationEntryRef.child("numberOfRooms").setValue(numberOfRooms);
                        accommodationEntryRef.child("roomType").setValue(roomType)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(getContext(), "Accommodation saved successfully!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Failed to save accommodation.", Toast.LENGTH_SHORT).show();
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Failed to check for duplicates.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }
    private void showDateFilterDialog() {
        // Create Date Pickers for Start and End Dates
        DatePickerDialog startDateDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            String startDate = (month + 1) + "/" + dayOfMonth + "/" + year;
            DatePickerDialog endDateDialog = new DatePickerDialog(getContext(), (view1, year1, month1, dayOfMonth1) -> {
                String endDate = (month1 + 1) + "/" + dayOfMonth1 + "/" + year1;
                filterAccommodationsByDateRange(startDate, endDate);
            }, year, month, dayOfMonth);
            endDateDialog.show();
        }, 2024, 0, 1);

        startDateDialog.show();
    }
    private void filterAccommodationsByDateRange(String startDate, String endDate) {
        try {
            // Use the same format as your database (MM-dd-yyyy)
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

            // Parse the start and end date from the dialog
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);

            List<Accommodation> filteredList = new ArrayList<>();

            for (Accommodation accommodation : accommodationList) {
                Date checkInDate = dateFormat.parse(accommodation.getCheckInDate());
                Date checkOutDate = dateFormat.parse(accommodation.getCheckOutDate());

                // Check if either the check-in date or check-out date falls within the selected range
                if ((checkInDate.equals(start) || checkInDate.after(start)) &&
                        (checkInDate.before(end) || checkInDate.equals(end)) ||
                        (checkOutDate.equals(start) || checkOutDate.after(start)) &&
                                (checkOutDate.before(end) || checkOutDate.equals(end))) {
                    // Add to the filtered list if it matches the criteria
                    filteredList.add(accommodation);
                }
            }

            // Update the adapter with the filtered list
            accommodationAdapter.updateList(filteredList);

            // If no accommodations match, show a message
            if (filteredList.isEmpty()) {
                Toast.makeText(getContext(), "No accommodations found for the selected dates.",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
