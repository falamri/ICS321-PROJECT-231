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
        ImageButton findDonorsButton = findViewById(R.id.btnReceive);
        ImageButton donatesButton = findViewById(R.id.btnDonate);
        ImageButton reportButton = findViewById(R.id.btnReports);
        ImageButton requestButton = findViewById(R.id.btnRequest);

        // Set click listeners for buttons
        findDonorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Find Donors page
                Intent intent = new Intent(HomeActivity.this, FindDonorsActivity.class);
                 startActivity(intent);
            }
        });

        donatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Donates button click
                 Intent intent = new Intent(HomeActivity.this, DonateActivity.class);
                 startActivity(intent);
            }
        });

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(HomeActivity.this, ReportActivity.class);
                 startActivity(intent);
            }
        });

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(HomeActivity.this, RequestActivity.class);
                 startActivity(intent);
            }
        });


    }
}
