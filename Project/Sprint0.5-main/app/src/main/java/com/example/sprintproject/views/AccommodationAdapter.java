package com.example.sprintproject.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sprintproject.R;
import com.example.sprintproject.model.Accommodation;
import java.util.List;

public class AccommodationAdapter extends RecyclerView.Adapter<AccommodationAdapter.AccommodationViewHolder> {

    private final List<Accommodation> accommodations;

    public AccommodationAdapter(List<Accommodation> accommodations) {
        this.accommodations = accommodations;
    }

    @NonNull
    @Override
    public AccommodationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accommodation_item, parent, false);
        return new AccommodationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccommodationViewHolder holder, int position) {
        Accommodation accommodation = accommodations.get(position);
        holder.locationTextView.setText(accommodation.getLocation());
        holder.checkInTextView.setText(accommodation.getCheckInDate());
        holder.checkOutTextView.setText(accommodation.getCheckOutDate());
        holder.roomTypeTextView.setText(accommodation.getRoomType());
    }

    @Override
    public int getItemCount() {
        return accommodations.size();
    }

    public static class AccommodationViewHolder extends RecyclerView.ViewHolder {
        TextView locationTextView, checkInTextView, checkOutTextView, roomTypeTextView;

        public AccommodationViewHolder(@NonNull View itemView) {
            super(itemView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            checkInTextView = itemView.findViewById(R.id.checkInTextView);
            checkOutTextView = itemView.findViewById(R.id.checkOutTextView);
            roomTypeTextView = itemView.findViewById(R.id.roomTypeTextView);
        }
    }
}
