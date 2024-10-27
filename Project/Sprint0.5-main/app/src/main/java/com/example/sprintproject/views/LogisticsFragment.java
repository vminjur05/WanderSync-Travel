package com.example.sprintproject.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sprintproject.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.content.ContextCompat;


import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogisticsFragment extends Fragment {




    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String userInputText = "";
    private String mParam1;
    private String mParam2;

    private PieChart pieChart;
    private DatabaseReference databaseReference;

    public LogisticsFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LogisticsFragment.
     */
    public static LogisticsFragment newInstance(String param1, String param2) {
        LogisticsFragment fragment = new LogisticsFragment();
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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_logistics, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference("travelLog");

        // Use view.findViewById to access the PieChart
        pieChart = view.findViewById(R.id.logistics_graph);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Find views in the fragment's layout
        Button graphButton = view.findViewById(R.id.logistics_graph_button);
        //LinearLayout graphContainer = view.findViewById(R.id.graph_container);
        TextView title = view.findViewById(R.id.logistics_graph_title);
        // Set up button click listener
        graphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the graph container
                //graphContainer.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);
                loadDataFromFirebase();
            }
        });
        FloatingActionButton userInviteButton = view.findViewById(R.id.floating_invite_button);

        userInviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter Your Text");

        final EditText input = new EditText(getContext());
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            userInputText = input.getText().toString();
            Toast.makeText(getContext(), "Input stored: " + userInputText, Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void loadDataFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<PieEntry> entries = new ArrayList<>();

                // Retrieve the main "allotted" duration if it's a separate key at the root
                Integer allotted = dataSnapshot.child("duration").getValue(Integer.class);
                if (allotted != null) {
                    entries.add(new PieEntry(allotted, "Allotted Duration"));
                }

                // Loop through each child in the data snapshot
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Integer days = snapshot.child("duration").getValue(Integer.class);
                    String label = snapshot.child("name").getValue(String.class); // Assuming each child has a "name" field

                    if (days != null && label != null) {
                        entries.add(new PieEntry(days, label));
                    }
                }

                // Update the pie chart with the new data
                showPieChart(entries);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

    private void showPieChart(List<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "Travel Log");

        // Set colors by fetching from resources
        dataSet.setColors(
                ContextCompat.getColor(this.getContext(), R.color.red),
                ContextCompat.getColor(this.getContext(), R.color.lime)
        );

        // Additional styling
        dataSet.setSliceSpace(3f); // Optional: adds space between slices
        dataSet.setValueTextColor(ContextCompat.getColor(this.getContext(), R.color.black)); // Text color
        dataSet.setValueTextSize(12f);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.invalidate(); // Refresh the chart
    }



}



