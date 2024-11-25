package com.example.sprintproject.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.model.TravelPost;

import java.util.ArrayList;

public class TravelPostAdapter extends RecyclerView.Adapter<TravelPostAdapter.TravelPostViewHolder> {

    private final ArrayList<TravelPost> travelPosts;
    // helps implement travel post into fragment
    public TravelPostAdapter(ArrayList<TravelPost> travelPosts) {
        this.travelPosts = travelPosts;
    }

    @NonNull
    @Override
    public TravelPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.travel_post, parent, false);
        return new TravelPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelPostViewHolder holder, int position) {
        TravelPost post = travelPosts.get(position);
        holder.destinationTextView.setText(post.destination);
        holder.durationTextView.setText(String.format("%s to %s", post.startDate, post.endDate));
        holder.notesTextView.setText(post.notes != null ? post.notes : "No notes available");
    }

    @Override
    public int getItemCount() {
        return travelPosts.size();
    }

    static class TravelPostViewHolder extends RecyclerView.ViewHolder {
        TextView destinationTextView, durationTextView, notesTextView;

        public TravelPostViewHolder(@NonNull View itemView) {
            super(itemView);
            // UI implementation through TextView
            destinationTextView = itemView.findViewById(R.id.destinationTextView);
            durationTextView = itemView.findViewById(R.id.durationTextView);
            notesTextView = itemView.findViewById(R.id.notesTextView);
        }
    }
} // made some changes bigneshbignur
