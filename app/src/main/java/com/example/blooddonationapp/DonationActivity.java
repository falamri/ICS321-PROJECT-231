package com.example.blooddonationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class DonationActivity extends AppCompatActivity {

    private EditText hospitalField;
    private EditText notesField;
    private ImageButton goBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donation_layout);

        // Assuming you have initialized your EditText fields
        hospitalField = findViewById(R.id.hospitalField);
        notesField = findViewById(R.id.notesField);

        // Donate Button
        Button donateButton = findViewById(R.id.donateButton);
        ImageButton gobackButton=findViewById(R.id.goBackButton);
        gobackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=getIntent();
                Person user=null;
                if(intent!=null)
                    user= (Person) intent.getSerializableExtra("User");
                final Person finalUser=user;
                intent = new Intent(DonationActivity.this, HomeActivity.class);
                intent.putExtra("User", finalUser);
                startActivity(intent);
            }
        });
        Intent intent = getIntent();
        Person user = null;
        if (intent != null) {
            user = (Person) intent.getSerializableExtra("User");
        }
        final Person finalUser = user;
        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values from EditText fields
                String hospital = hospitalField.getText().toString();
                String notes = notesField.getText().toString();

                // Validate if fields are not empty
                if (!hospital.isEmpty() && !notes.isEmpty()) {
                    DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());


                    // Call insertDonor method to insert donation details
                    dbHelper.insertDonor(finalUser);

                    // Finish the current activity and go back to the HomeActivity
                    Intent intent = new Intent(DonationActivity.this, HomeActivity.class);
                    intent.putExtra("User", finalUser);
                    startActivity(intent);
                }
            }
        });
    }
}
