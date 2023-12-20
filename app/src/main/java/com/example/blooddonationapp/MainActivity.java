package com.example.blooddonationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseHelper db= new DatabaseHelper(getApplicationContext());
        db.printPersonData(db.getReadableDatabase());
    }

    // Handle login button click
    public void onLoginButtonClick(View view) {
        // You can add your login logic here or navigate to the login activity
        // For example:

        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    // Handle signup button click
    public void onSignupButtonClick(View view) {
        // You can add your signup logic here or navigate to the signup activity
        // For example:
        Intent signupIntent = new Intent(this, SignupActivity.class);
        startActivity(signupIntent);
    }
}
