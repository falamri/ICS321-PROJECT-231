package com.example.blooddonationapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        ImageButton searchButton = findViewById(R.id.btnSearchUsers);
        ImageButton addRemoveButton = findViewById(R.id.btnAddRemoveUsers);
        Intent intent = getIntent();
        Person user=null;
        if (intent != null) {
             user = (Person) intent.getSerializableExtra("User");
        }
            // Set click listeners for buttons
        Person finalUser = user;
        findDonorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Find Donors page
                    Intent intent = new Intent(HomeActivity.this, ReceiveActivity.class);
                    intent.putExtra("User", finalUser);
                    startActivity(intent);
                }

        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Find Donors page
                    Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                    intent.putExtra("User", finalUser);
                    startActivity(intent);
                }

        });
        addRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Find Donors page
                    Intent intent = new Intent(HomeActivity.this, AddRemoveActivity.class);
                    intent.putExtra("User", finalUser);
                    startActivity(intent);
                }

        });

        donatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLastDonationWithinThreeMonths(finalUser)) {
                    // Handle Donates button click
                    Intent intent = new Intent(HomeActivity.this, DonationActivity.class);
                    intent.putExtra("User", finalUser);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(HomeActivity.this, "You have to wait at least 3 months between each donation", Toast.LENGTH_SHORT).show();

                }
            }
        });


        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=getIntent();
                Person user=null;
                if(intent!=null)
                    user= (Person) intent.getSerializableExtra("User");

                 intent = new Intent(HomeActivity.this, ReportActivity.class);
                 intent.putExtra("User",finalUser);
                 startActivity(intent);
            }
        });

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(HomeActivity.this, RequestActivity.class);
                intent.putExtra("User", finalUser);
                startActivity(intent);
            }
        });


    }
    @SuppressLint("NewApi")
    public boolean isLastDonationWithinThreeMonths(Person person) {
        // Get the Person ID
        int personID = person.getId();
        DatabaseHelper db=new DatabaseHelper(getApplicationContext());

        // Get the Last Donation Date from the database
        String lastDonationDateStr = db.getLastDonationDate(personID);

        if (lastDonationDateStr != null) {
            // Parse Last Donation Date string into LocalDate object
            @SuppressLint({"NewApi", "LocalSuppress"}) DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
            @SuppressLint({"NewApi", "LocalSuppress"}) LocalDate lastDonationDate = LocalDate.parse(lastDonationDateStr, formatter);
            @SuppressLint({"NewApi", "LocalSuppress"}) LocalDate currentDate = LocalDate.now();

            // Check if last donation date is within three months
            @SuppressLint({"NewApi", "LocalSuppress"}) LocalDate threeMonthsAhead = lastDonationDate.plusMonths(3);
            Log.d("Check date", formatter+"\n"+lastDonationDate+"\n"+threeMonthsAhead);
            return currentDate.isAfter(threeMonthsAhead);
        } else {
            return false; // Handle case where LastDonationDate is null
        }
    }

}
