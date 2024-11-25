package com.example.sprintproject.views;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.model.TravelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TravelCommunityFragment extends Fragment {

    private RecyclerView recyclerView;
    private TravelPostAdapter adapter;
    private ArrayList<TravelPost> travelPosts;
    private DatabaseReference databaseReference;

    public TravelCommunityFragment() {
        // Required empty public constructor
    }

    public static TravelCommunityFragment newInstance(String param1, String param2) {
        TravelCommunityFragment fragment = new TravelCommunityFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            if (userEmail != null) {
                String encodedEmail = userEmail.replace(".", ","); // Replace '.' with ',' for Firebase keys
                databaseReference = FirebaseDatabase.getInstance().getReference("travel_posts").child(encodedEmail);

                // Check if default trip needs to be added
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            addDefaultTrip();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Failed to check user data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getContext(), "Unable to retrieve email address.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "User not logged in!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_travel_community, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        travelPosts = new ArrayList<>();
        adapter = new TravelPostAdapter(travelPosts);
        recyclerView.setAdapter(adapter);

        // Fetch travel posts from Firebase
        fetchTravelPosts();

        // Set up "New Post" button
        Button newPostButton = view.findViewById(R.id.newPostButton);
        newPostButton.setOnClickListener(v -> showNewPostDialog());

        return view;
    }

    private void fetchTravelPosts() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                travelPosts.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    TravelPost post = postSnapshot.getValue(TravelPost.class);
                    travelPosts.add(post);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch posts: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addDefaultTrip() {
        String postId = databaseReference.push().getKey();
        if (postId != null) {
            TravelPost defaultTrip = new TravelPost(
                    postId,
                    "12/24/2024", // Start date
                    "12/25/2024", // End date
                    "CS2340",     // Destination
                    "None",       // Accommodations
                    "None",       // Dining
                    "None"        // Notes
            );

            databaseReference.child(postId).setValue(defaultTrip)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Default trip added for new user.", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to add default trip: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void showNewPostDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_post, null);
        builder.setView(dialogView);

        EditText startDateInput = dialogView.findViewById(R.id.startDateInput);
        EditText endDateInput = dialogView.findViewById(R.id.endDateInput);
        EditText destinationInput = dialogView.findViewById(R.id.destinationInput);
        EditText accommodationsInput = dialogView.findViewById(R.id.accommodationsInput);
        EditText diningInput = dialogView.findViewById(R.id.diningInput);
        EditText notesInput = dialogView.findViewById(R.id.notesInput);

        Button saveButton = dialogView.findViewById(R.id.saveButton);
        AlertDialog dialog = builder.create();

        saveButton.setOnClickListener(v -> {
            String startDate = startDateInput.getText().toString();
            String endDate = endDateInput.getText().toString();
            String destination = destinationInput.getText().toString();
            String accommodations = accommodationsInput.getText().toString();
            String dining = diningInput.getText().toString();
            String notes = notesInput.getText().toString();

            if (TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate) || TextUtils.isEmpty(destination)) {
                Toast.makeText(getContext(), "Start date, end date, and destination are required.", Toast.LENGTH_SHORT).show();
                return;
            }

            String dateFormat = "\\d{2}/\\d{2}/\\d{4}";
            if (!startDate.matches(dateFormat) || !endDate.matches(dateFormat)) {
                Toast.makeText(getContext(), "Dates must be in the format MM/dd/yyyy.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                sdf.setLenient(false);
                Date start = sdf.parse(startDate);
                Date end = sdf.parse(endDate);

                if (start == null || end == null || start.after(end)) {
                    Toast.makeText(getContext(), "Start date must be before end date.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (start.before(new Date())) {
                    Toast.makeText(getContext(), "Start date cannot be in the past.", Toast.LENGTH_SHORT).show();
                    return;
                }

            } catch (ParseException e) {
                Toast.makeText(getContext(), "Invalid date format.", Toast.LENGTH_SHORT).show();
                return;
            }

            String postId = databaseReference.push().getKey();
            TravelPost post = new TravelPost(postId, startDate, endDate, destination, accommodations, dining, notes);
            if (postId != null) {
                databaseReference.child(postId).setValue(post)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(), "Post created successfully!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to create post: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }); //new method

        dialog.show();
    }
}
