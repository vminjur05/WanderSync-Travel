package com.example.sprintproject.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sprintproject.R;
import com.example.sprintproject.model.FirebaseDatabaseHelper;
import com.example.sprintproject.model.LogisticsFragmentModel;
import com.example.sprintproject.viewmodels.LogisticsViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.content.ContextCompat;
import com.github.mikephil.charting.utils.ColorTemplate;


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
    private static LogisticsFragmentModel instance;
    private LogisticsViewModel viewModel;
    private DatabaseReference destinationsReference;
    private DatabaseReference travelLogReference;
    private FirebaseAuth firebaseAuth;

    private String userInputTextAddUser = "";
    private String userInputTextAddNotes = "";

    private BarChart barChart;
    private DatabaseReference databaseReference;

    // Private constructor to prevent instantiation
    public LogisticsFragment() { }

    // Thread-safe method to get the single instance of DestinationFragment
    public static synchronized LogisticsFragmentModel getInstance() {
        if (instance == null) {
            instance = new LogisticsFragmentModel();
        }
        return instance;
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
        viewModel = LogisticsViewModel.getInstance(requireActivity().getApplication());
        // Initialize Firebase references using singleton FirebaseDatabaseHelper
        destinationsReference = FirebaseDatabaseHelper.getInstance().getDestinationsReference();
        travelLogReference = FirebaseDatabaseHelper.getInstance().getTravelLogReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_logistics, container, false);
        // Use view.findViewById to access the PieChart
        barChart = view.findViewById(R.id.logistics_graph);
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
                setupBarChart();
                loadDataFromFirebase();
            }
        });
        FloatingActionButton userInviteButton = view.findViewById(R.id.floating_invite_button);

        userInviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialogAddUser();
            }
        });

        FloatingActionButton userAddNoteButton = view.findViewById(R.id.floating_notes_button);

        userAddNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialogAddNotes();
            }
        });
    }

    private void showInputDialogAddUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter Your Text");

        final EditText input = new EditText(getContext());
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            userInputTextAddUser = input.getText().toString();
            Toast.makeText(getContext(), "Input stored: " + userInputTextAddUser,
                    Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showInputDialogAddNotes() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter Your Text");

        final EditText input = new EditText(getContext());
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            userInputTextAddNotes = input.getText().toString();
            Toast.makeText(getContext(), "Input stored: " + userInputTextAddNotes,
                    Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void setupBarChart() {
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setPinchZoom(true);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);

        // Set up the legend
        Legend legend = barChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
    }

    private void loadDataFromFirebase() {
        String userEmail = firebaseAuth.getCurrentUser().getEmail();
        String sanitizedEmail = userEmail.replace(".", ",");

        databaseReference = FirebaseDatabase.getInstance().getReference("travelLog")
                .child(sanitizedEmail);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<BarEntry> durationEntries = new ArrayList<>();
                List<BarEntry> allottedEntries = new ArrayList<>();
                List<String> labels = new ArrayList<>();
                int index = 0;

                for (DataSnapshot tripSnapshot : snapshot.getChildren()) {
                    String durationStr = tripSnapshot.child("duration").getValue(String.class);
                    String endDateStr = tripSnapshot.child("endDate").getValue(String.class);
                    String startDate = tripSnapshot.child("startDate").getValue(String.class);
                    //System.out.println(durationStr + endDateStr + startDate);
                    if (durationStr != null && !durationStr.isEmpty()) {
                        try {
                            // Parse duration as float for BarEntry
                            float duration = Float.parseFloat(durationStr);
                            durationEntries.add(new BarEntry(index, duration));

                            LogisticsViewModel.DurationResultLogistics allottedBringer
                                    = viewModel.calculateDurationLogistics(startDate, endDateStr);
                            float allotted = allottedBringer.getDuration();
                            if (allotted == -1234) {
                                allottedEntries.add(new BarEntry(index, duration));
                            } else {
                                allottedEntries.add(new BarEntry(index, allotted));
                            }

                            // Use startDate as label
                            labels.add(startDate + " through " + endDateStr);
                            index++;
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
                BarEntry lastDuration = durationEntries.get(durationEntries.size() - 1);
                BarEntry lastAlloted = allottedEntries.get(allottedEntries.size() - 1);
                String lastLabel = labels.get(labels.size() - 1);
                allottedEntries.clear();
                allottedEntries.add(lastAlloted);
                durationEntries.clear();
                durationEntries.add(lastDuration);
                labels.clear();
                labels.add(lastLabel);

                displayDataInChart(durationEntries, allottedEntries, labels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error loading data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadRecentTrips(LinearLayout recentTripsLayout) {
        String userEmail = firebaseAuth.getCurrentUser().getEmail();
        String sanitizedEmail = userEmail.replace(".", ",");

        destinationsReference.child(sanitizedEmail)
                .orderByKey()
                .limitToLast(5)
                .get()
                .addOnSuccessListener(dataSnapshot -> {
                    recentTripsLayout.removeAllViews();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot tripSnapshot : dataSnapshot.getChildren()) {
                            String location = tripSnapshot.child("location").getValue(String.class);
                            String start = tripSnapshot.child("estimatedStart")
                                    .getValue(String.class);
                            String end = tripSnapshot.child("estimatedEnd").getValue(String.class);

                            TextView tripView = new TextView(getContext());
                            tripView.setText("Location: " + location + "\nStart: "
                                    + start + "\nEnd: " + end);
                            tripView.setPadding(0, 10, 0, 10);
                            recentTripsLayout.addView(tripView);
                        }
                    } else {
                        TextView noTripsView = new TextView(getContext());
                        noTripsView.setText("No recent trips found.");
                        recentTripsLayout.addView(noTripsView);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load recent trips.",
                            Toast.LENGTH_SHORT).show();
                });
    }


    private void displayDataInChart(List<BarEntry> durationEntries, List<BarEntry> endDateEntries,
                                    List<String> labels) {
        BarDataSet durationDataSet = new BarDataSet(durationEntries, "Planned Days");
        durationDataSet.setColor(ColorTemplate.MATERIAL_COLORS[0]);

        BarDataSet endDateDataSet = new BarDataSet(endDateEntries, "Allotted Days");
        endDateDataSet.setColor(ColorTemplate.MATERIAL_COLORS[1]);

        BarData data = new BarData(durationDataSet, endDateDataSet);
        data.setValueTextSize(10f);
        data.setBarWidth(0.4f); // Space between bars

        barChart.setData(data);

        // Format x-axis labels
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);
        barChart.getXAxis().setCenterAxisLabels(true);

        // Group bars with a spacing of 0.1f between groups
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(labels.size());
        barChart.groupBars(0f, 0.2f, 0.05f); // Adjusts space between groups

        barChart.invalidate(); // Refresh chart
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
        dataSet.setValueTextColor(ContextCompat.getColor(this.getContext(),
                R.color.black)); // Text color
        dataSet.setValueTextSize(16f);

        PieData pieData = new PieData(dataSet);
        //pieChart.setData(pieData);
        //pieChart.invalidate(); // Refresh the chart
    }

    //ADD INVITE FUNCTIONALITY



}




