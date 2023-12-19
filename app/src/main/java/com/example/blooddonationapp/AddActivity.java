package com.example.blooddonationapp;

import androidx.appcompat.app.AppCompatActivity;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class AddActivity extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText,
            phoneNumberEditText, dateOfBirthEditText, addressEditText, weightEditText;
    private Spinner bloodTypeSpinner;
    private Button loginButton, goBackButton;
    private TextView haveAccountText, signInText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

        // Initialize  UI elements
        firstNameEditText = findViewById(R.id.fname);
        lastNameEditText = findViewById(R.id.lname);
        emailEditText = findViewById(R.id.email1);
        passwordEditText = findViewById(R.id.password1);
        phoneNumberEditText = findViewById(R.id.phoneNumber);
        dateOfBirthEditText = findViewById(R.id.dateOfBirth);
        addressEditText = findViewById(R.id.address);
        weightEditText = findViewById(R.id.weight);
        bloodTypeSpinner = findViewById(R.id.bloodTypeSpinner);
        loginButton = findViewById(R.id.loginButton);
        ImageButton goBackButton = (ImageButton) findViewById(R.id.goBackButton);
        haveAccountText = findViewById(R.id.haveAccountText);

        // Set click listeners
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpAndNavigateToMainPage();
            }
        });


    }

    private void goBack() {
        Intent intent=getIntent();
        Person user=null;
        if (intent != null) {
            user = (Person) intent.getSerializableExtra("User");
        }
        // Set click listeners for buttons
        Person finalUser = user;
         intent = new Intent(this, AddRemoveActivity.class);
         intent.putExtra("User",finalUser);
        startActivity(intent);
        finish(); // Optional: Close the current activity
    }

    private void signUpAndNavigateToMainPage() {
        // Check if all fields are filled
        if (areAllFieldsFilled()) {
            // Use the DatabaseHelper to get the highest existing ID
            DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
            int highestId = dbHelper.getHighestUserId();

            // Increment the highest ID to generate a new unique ID
            int newId = highestId + 1;
            Log.d("DatabaseHelper", " ID: " + newId);

            // Create a Person object with the details
            Person person = new Person(
                    newId,
                    passwordEditText.getText().toString(),
                    emailEditText.getText().toString(),
                    firstNameEditText.getText().toString(),
                    lastNameEditText.getText().toString(),
                    dateOfBirthEditText.getText().toString(),
                    phoneNumberEditText.getText().toString(),
                    addressEditText.getText().toString(),// Replace with actual health status if applicable
                    Integer.parseInt(weightEditText.getText().toString()),bloodTypeSpinner.getSelectedItem().toString()
            );

            // Use the DatabaseHelper to add the user
            boolean result=dbHelper.addUser(person);
            if(result)
                Toast.makeText(this, "User has been Created", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish(); // Optional: Close the current activity
        }
        else
            Toast.makeText(this, "Fill All the Information", Toast.LENGTH_SHORT).show();

    }

    private boolean areAllFieldsFilled() {
        // Check if each field has some value
        return !(
                firstNameEditText.getText().toString().isEmpty() ||
                        lastNameEditText.getText().toString().isEmpty() ||
                        emailEditText.getText().toString().isEmpty() ||
                        passwordEditText.getText().toString().isEmpty() ||
                        dateOfBirthEditText.getText().toString().isEmpty() ||
                        phoneNumberEditText.getText().toString().isEmpty() ||
                        addressEditText.getText().toString().isEmpty() ||
                        weightEditText.getText().toString().isEmpty()
        );
    }


}


