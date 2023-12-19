package com.example.blooddonationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {

    private EditText editTextId;
    private Button btnSearch;
    private LinearLayout layoutPersonDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        editTextId = findViewById(R.id.editTextId);
        btnSearch = findViewById(R.id.btnRemove);
        ImageButton btnGoBack=findViewById(R.id.goBackButton);
        layoutPersonDetails = findViewById(R.id.layoutPersonDetails);
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=getIntent();
                Person user=null;
                if (intent != null) {
                    user = (Person) intent.getSerializableExtra("User");
                }
                // Set click listeners for buttons
                Person finalUser = user;
                intent = new Intent(SearchActivity.this, HomeActivity.class);
                intent.putExtra("User",finalUser);
                startActivity(intent);

            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db=new DatabaseHelper(getApplicationContext());
                EditText editTextId = findViewById(R.id.editTextId);
                String personIdStr = editTextId.getText().toString();

                try {
                    int personId = Integer.parseInt(personIdStr);
                    Person person = db.getPersonById(personId);
                    if (person != null) {
                        // Handle the person object
                        layoutPersonDetails.setVisibility(View.VISIBLE);
                        // Now populate the details in the layout based on the retrieved person object
                        populatePersonDetails(person); // You'll need to implement this method

                    } else {
                        Toast.makeText(SearchActivity.this, "User not found!", Toast.LENGTH_SHORT).show();
                    }

                } catch (NumberFormatException e) {
                    // Handle the case where the entered value is not a valid integer
                    Toast.makeText(SearchActivity.this, "Please enter a valid ID", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void populatePersonDetails(Person person) {
        if (person != null) {
            // Assuming you have TextView widgets in your layout with the following IDs
            TextView textViewID = findViewById(R.id.textViewId);
            TextView textViewName = findViewById(R.id.textViewName);
            TextView textViewDob = findViewById(R.id.textViewDob);
            TextView textViewContactNum = findViewById(R.id.textViewContactNum);
            TextView textViewEmail = findViewById(R.id.textViewEmail);
            TextView textViewAddress = findViewById(R.id.textViewAddress);
            TextView textViewHealthStatus = findViewById(R.id.textViewHealthStatus);
            TextView textViewWeight = findViewById(R.id.textViewWeight);

            // Populate the TextViews with person details
            textViewID.setText("ID: "+person.getId());
            textViewName.setText("Name: "+person.getFname()+" "+person.getLname());
            textViewDob.setText("DOB: "+person.getDob());
            textViewContactNum.setText("Contact Number: "+person.getNum());
            textViewEmail.setText("Email: "+person.getEmail());
            textViewAddress.setText("Address: "+person.getAddress());
            textViewHealthStatus.setText("Health Status: "+person.getHealthStatus());
            textViewWeight.setText(String.valueOf("Weight: "+person.getWeight())); // Convert weight to string before setting
        } else {
            Toast.makeText(this, "No details available for the given person ID", Toast.LENGTH_SHORT).show();
        }
    }

}
