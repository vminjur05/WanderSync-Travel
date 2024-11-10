package com.example.sprintproject.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sprintproject.R;
import com.example.sprintproject.model.Reservation;
import com.google.firebase.database.DatabaseReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private final List<Reservation> reservations;
    private final DatabaseReference reservationDatabase;
    private final String sanitizedEmail;

    public ReservationAdapter(List<Reservation> reservations, DatabaseReference reservationDatabase, String sanitizedEmail) {
        this.reservations = reservations;
        this.reservationDatabase = reservationDatabase;
        this.sanitizedEmail = sanitizedEmail;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_item, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);
        holder.locationTextView.setText(reservation.getLocation());
        holder.timeTextView.setText(reservation.getReservationTime());
        holder.ratingBar.setRating(reservation.getRating());

        // Updated: Use a full datetime format for parsing and comparison
        String reservationTime = reservation.getReservationTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            Date reservationDate = dateFormat.parse(reservationTime);
            Date currentDate = new Date();
            if (reservationDate != null && reservationDate.after(currentDate)) {
                holder.statusTextView.setText("Upcoming");
            } else {
                holder.statusTextView.setText("Expired");
            }
        } catch (Exception e) {
            e.printStackTrace();
            holder.statusTextView.setText("Invalid Date");
        }

        // Directly update the rating when RatingBar is clicked
        holder.ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                reservation.setRating((int) rating);  // Set the new rating
                // Update the reservation rating in Firebase directly on the existing reservation
                String reservationId = reservation.getId(); // Assuming Reservation has an `id` field
                if (reservationId != null) {
                    reservationDatabase.child(sanitizedEmail).child(reservationId).setValue(reservation)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Update the item in the list and notify the adapter
                                    reservations.set(position, reservation);
                                    notifyItemChanged(position);
                                }
                            });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView locationTextView, timeTextView, statusTextView;
        RatingBar ratingBar;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
