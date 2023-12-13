package com.example.blooddonationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize buttons
        ImageButton findDonorsButton = findViewById(R.id.findDonorsButton);
        ImageButton donatesButton = findViewById(R.id.donatesButton);
        ImageButton orderBloodsButton = findViewById(R.id.orderBloodsButton);
        ImageButton reportButton = findViewById(R.id.reportButton);
        ImageButton requestButton = findViewById(R.id.requestButton);

        // Set click listeners for buttons
        findDonorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Find Donors button click
                Toast.makeText(HomeActivity.this, "Find Donors clicked", Toast.LENGTH_SHORT).show();
                // Navigate to Find Donors page
                // Intent intent = new Intent(HomeActivity.this, FindDonorsActivity.class);
                // startActivity(intent);
            }
        });

        donatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Donates button click
                Toast.makeText(HomeActivity.this, "Donates clicked", Toast.LENGTH_SHORT).show();
                // Navigate to Donates page
                // Intent intent = new Intent(MainActivity.this, DonatesActivity.class);
                // startActivity(intent);
            }
        });

        orderBloodsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Order Bloods button click
                Toast.makeText(HomeActivity.this, "Order Bloods clicked", Toast.LENGTH_SHORT).show();
                // Navigate to Order Bloods page
                // Intent intent = new Intent(HomeActivity.this, OrderBloodsActivity.class);
                // startActivity(intent);
            }
        });

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Report button click
                Toast.makeText(HomeActivity.this, "Report clicked", Toast.LENGTH_SHORT).show();
                // Navigate to Report page
                // Intent intent = new Intent(HomeActivity.this, ReportActivity.class);
                // startActivity(intent);
            }
        });

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Request button click
                Toast.makeText(HomeActivity.this, "Request clicked", Toast.LENGTH_SHORT).show();
                // Navigate to Request page
                // Intent intent = new Intent(HomeActivity.this, RequestActivity.class);
                // startActivity(intent);
            }
        });

        // Set click listener for the card view
        findViewById(R.id.donationCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Card click action
                Toast.makeText(HomeActivity.this, "Card clicked", Toast.LENGTH_SHORT).show();
                // Perform actions related to the card click
                // For example, navigate to a detailed view of the donation request
                // Intent intent = new Intent(HomeActivity.this, DonationDetailsActivity.class);
                // startActivity(intent);
            }
        });
    }
}
