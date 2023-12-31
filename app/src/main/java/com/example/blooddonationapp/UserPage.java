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

public class UserPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_page);
        // Initialize buttons
        ImageButton findDonorsButton = findViewById(R.id.btnReceive);
        ImageButton donatesButton = findViewById(R.id.btnDonate);
        ImageButton requestButton = findViewById(R.id.btnRequest);
        ImageButton ProfileBtn = findViewById(R.id.Profile);
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
                Intent intent = new Intent(UserPage.this, ReceiveActivity.class);
                intent.putExtra("User", finalUser);
                intent.putExtra("Source", "User");
                startActivity(intent);
            }

        });
        ProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Find Donors page
                Intent intent = new Intent(UserPage.this, ProfileActivity.class);
                intent.putExtra("User", finalUser);
                startActivity(intent);
            }

        });


        donatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLastDonationWithinThreeMonths(finalUser)) {
                    // Handle Donates button click
                    Intent intent = new Intent(UserPage.this, DonationActivity.class);
                    intent.putExtra("User", finalUser);
                    intent.putExtra("Source", "User");
                    startActivity(intent);
                }
                else{
                    Toast.makeText(UserPage.this, "You have to wait at least 3 months between each donation", Toast.LENGTH_SHORT).show();

                }
            }
        });

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserPage.this, RequestActivity.class);
                intent.putExtra("User", finalUser);
                intent.putExtra("Source", "User");
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
