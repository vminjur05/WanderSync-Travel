package com.example.sprintproject.views;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sprintproject.R;
import com.example.sprintproject.model.Accommodation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AccommodationAdapter extends
        RecyclerView.Adapter<AccommodationAdapter.AccommodationViewHolder> {

    private final List<Accommodation> accommodations;

    public AccommodationAdapter(List<Accommodation> accommodations) {
        this.accommodations = accommodations;
    }

    @NonNull
    @Override
    public AccommodationViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                      int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.accommodation_item, parent, false);
        return new AccommodationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccommodationViewHolder holder, int position) {
        Accommodation accommodation = accommodations.get(position);
        holder.locationTextView.setText(accommodation.getLocation());
        holder.checkInTextView.setText(accommodation.getCheckInDate());
        holder.checkOutTextView.setText(accommodation.getCheckOutDate());
        holder.roomTypeTextView.setText(accommodation.getRoomType());
        holder.numberOfRoomsTextView.setText(accommodation.getNumberOfRooms());

        // Date format for parsing check-out date
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        Date today = new Date();
        try {
            Date checkOutDate = sdf.parse(accommodation.getCheckOutDate());

            if (checkOutDate != null && checkOutDate.before(today)) {
                // Expired reservation
                holder.statusTextView.setText("Expired");
                holder.statusTextView.setTextColor(Color.RED);
                holder.itemView.setBackgroundColor(Color.LTGRAY);
            } else {
                // Upcoming reservation
                holder.statusTextView.setText("Upcoming");
                holder.statusTextView.setTextColor(Color.GREEN);
                holder.itemView.setBackgroundColor(Color.WHITE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return accommodations.size();
    }

    public void updateList(List<Accommodation> newList) {
        accommodations.clear();
        accommodations.addAll(newList);
        notifyDataSetChanged();
    }

    public void setAccommodations(List<Accommodation> accommodations) {
        notifyDataSetChanged();
    }

    public static class AccommodationViewHolder extends RecyclerView.ViewHolder {
        private TextView locationTextView;
        private TextView checkInTextView;
        private TextView checkOutTextView;
        private TextView roomTypeTextView;
        private TextView numberOfRoomsTextView;
        private TextView statusTextView;

        public AccommodationViewHolder(@NonNull View itemView) {
            super(itemView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            checkInTextView = itemView.findViewById(R.id.checkInTextView);
            checkOutTextView = itemView.findViewById(R.id.checkOutTextView);
            roomTypeTextView = itemView.findViewById(R.id.roomTypeTextView);
            numberOfRoomsTextView = itemView.findViewById(R.id.numberOfRoomsTextView);
            statusTextView = itemView.findViewById(R
                    .id.statusTextView); // Initialize statusTextView
        }
    }
}
