package com.example.sprintproject.views;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.sprintproject.R;
import com.example.sprintproject.model.FirebaseDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class AccommodationsFragment extends Fragment {

    private DatabaseReference accommodationsReference;
    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase references
        accommodationsReference = FirebaseDatabaseHelper.getInstance().getAccommodationsReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_accommodations, container, false);

        FloatingActionButton fabPopup = view.findViewById(R.id.fab_popup);
        fabPopup.setOnClickListener(v -> showPopupDialog());

        return view;
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

            String userEmail = firebaseAuth.getCurrentUser().getEmail();
            String sanitizedEmail = userEmail.replace(".", ",");

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
        });

        dialog.show();
    }
}
