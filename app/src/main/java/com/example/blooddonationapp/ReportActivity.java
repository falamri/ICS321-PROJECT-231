package com.example.blooddonationapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private RecyclerView bloodDonationsRecyclerView;
    private RecyclerView collectionDrivesRecyclerView;
    private DonorAdapter donorAdapter;
    private CollectionDriveAdapter collectionDriveAdapter;
    private PieChart pieChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);
        ImageButton goBackButton = (ImageButton) findViewById(R.id.goBackButton);

        // Set click listeners
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToHomePage();
            }
        });


        // Assuming you have initialized the donorList and collectionDriveList
        List<DonorItem> donorList = getDonorList();
        List<Drive> collectionDriveList = getCollectionDriveList();

        // Initialize RecyclerViews
        bloodDonationsRecyclerView = findViewById(R.id.bloodDonationsRecyclerView);
        collectionDrivesRecyclerView = findViewById(R.id.collectionDrivesRecyclerView);
        pieChart = findViewById(R.id.pieChart);

        // Get the blood type counts from the database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        ArrayList<BloodTypeItem> bloodTypeList = dbHelper.getBloodTypeCounts();
        Log.d("Blood items", String.valueOf(bloodTypeList));
        // Populate the pie chart
        populatePieChart(bloodTypeList);

        // Initialize the DonorAdapter with the list of donors
        donorAdapter = new DonorAdapter(donorList);

        // Set layout manager and adapter for blood donations RecyclerView
        bloodDonationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bloodDonationsRecyclerView.setAdapter(donorAdapter);

        // Initialize the CollectionDriveAdapter with the list of collection drives
        collectionDriveAdapter = new CollectionDriveAdapter(collectionDriveList);

        // Set layout manager and adapter for collection drives RecyclerView
        collectionDrivesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        collectionDrivesRecyclerView.setAdapter(collectionDriveAdapter);
    }

    private void goBackToHomePage() {
        Intent intent=getIntent();
        Person user=null;
        if(intent!=null)
            user= (Person) intent.getSerializableExtra("User");
        final Person finalUser=user;

        intent = new Intent(this, HomeActivity.class);
        intent.putExtra("User",finalUser);
        startActivity(intent);
        finish(); // Optional: Close the current activity

    }

    // Method to get a list of sample donors
    private List<DonorItem> getDonorList() {
        // Populate your donor list here
        DatabaseHelper db= new DatabaseHelper(getApplicationContext());

        return db.getAllDonors();
    }

    // Method to get a list of sample collection drives
    private List<Drive> getCollectionDriveList() {
        // Populate your collection drive list here
        DatabaseHelper db= new DatabaseHelper(getApplicationContext());
        return db.getAllDrives();
    }


    private void populatePieChart(ArrayList<BloodTypeItem> bloodTypeList) {
        for (BloodTypeItem bloodTypeItem : bloodTypeList) {
            int colorResourceId = getColorResourceIdForBloodType(bloodTypeItem.getId());
            PieModel pieModel = new PieModel(bloodTypeItem.getId(), bloodTypeItem.getCount(), colorResourceId);
            pieChart.addPieSlice(pieModel);
        }
        // Create a custom legend outside the PieChart
        LinearLayout legendLayout = findViewById(R.id.legendLayout); // Add this LinearLayout to your XML layout
        legendLayout.removeAllViews(); // Clear existing legend entries

        for (BloodTypeItem bloodTypeItem : bloodTypeList) {
            int colorResourceId = getColorResourceIdForBloodType(bloodTypeItem.getId());

            // Create a legend entry (TextView with background color)
            TextView legendEntry = new TextView(this);
            legendEntry.setText(bloodTypeItem.getId()+" = "+ bloodTypeItem.getCount()+"\n");
            legendEntry.setBackgroundColor(colorResourceId);
            legendEntry.setPadding(8, 8, 8, 8);

            // Add the legend entry to the legend layout
            legendLayout.addView(legendEntry);
        }

        // Customize pie chart settings if needed

        // Call this method to draw the chart
        pieChart.startAnimation();
    }

    private int getColorResourceIdForBloodType(String bloodType) {
        switch (bloodType) {
            case "A+":
                return 0xFFFFA07A; // Light Salmon
            case "A-":
                return 0xFFFF6347; // Tomato
            case "B+":
                return 0xFF00CED1; // Dark Turquoise
            case "B-":
                return 0xFF1E90FF; // Dodger Blue
            case "O+":
                return 0xFFFF4500; // Orange Red
            case "O-":
                return 0xFFFF0000; // Red
            case "AB+":
                return 0xFF8A2BE2; // Blue Violet
            case "AB-":
                return 0xFF4B0082; // Indigo
            default:
                // Default color or handle the case accordingly
                return 0xFF000000; // Default to black
        }
    }





}
