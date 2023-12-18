package com.example.blooddonationapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RequestActivity extends AppCompatActivity {

    private EditText locationEditText;
    private EditText hospitalEditText;
    private EditText mobileEditText;
    private EditText notesEditText;
    private Button submitRequestButton;
private ImageButton goBackButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request);

        dbHelper = new DatabaseHelper(this);

        // Initialize UI components
        locationEditText = findViewById(R.id.locationEditText);
        hospitalEditText = findViewById(R.id.hospitalEditText);
        mobileEditText = findViewById(R.id.mobileEditText);
        notesEditText = findViewById(R.id.notesEditText);
        submitRequestButton = findViewById(R.id.submitRequestButton);
        goBackButton=findViewById(R.id.goBackButton);
        Intent intent=getIntent();
        Person user=null;
        if(intent!=null)
             user= (Person) intent.getSerializableExtra("User");

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToHomePage();
            }
        });
        // Set click listener for the submit button
        Person finalUser = user;
        submitRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a Request object with input data
                Request request = new Request(1, finalUser.getId(), finalUser.getFname(),"Request",finalUser.getType());
                // Assuming you have appropriate methods to get Id, PersonID, and Status

                // Call the method to create a request
                boolean isRequestCreated = dbHelper.createRequest(request);

                if (isRequestCreated) {
                    Toast.makeText(RequestActivity.this, "Request created successfully", Toast.LENGTH_SHORT).show();
                    // You can finish the activity or navigate to another screen if needed
                    finish();
                } else {
                    Toast.makeText(RequestActivity.this, "Failed to create request", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void goBackToHomePage() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish(); // Optional: Close the current activity

    }

}

