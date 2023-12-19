package com.example.blooddonationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class AddRemoveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_remove);
        Intent intent=getIntent();
        Person user=null;
        if (intent != null) {
            user = (Person) intent.getSerializableExtra("User");
        }
        // Set click listeners for buttons
        Person finalUser = user;

        // Find the Add User Button by its ID and set its click listener
        ImageButton btnAddUser = findViewById(R.id.btnAddUser);
        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AddActivity
                Intent intent = new Intent(AddRemoveActivity.this, AddActivity.class);
                intent.putExtra("User",finalUser);
                startActivity(intent);
            }
        });

        // Find the Remove User Button by its ID and set its click listener
        ImageButton btnRemoveUser = findViewById(R.id.btnRemoveUser);
        btnRemoveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to RemoveActivity
                Intent intent = new Intent(AddRemoveActivity.this, RemoveActivity.class);
                intent.putExtra("User",finalUser);
                startActivity(intent);
            }
        });
        ImageButton goBack=findViewById(R.id.goBackButton);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to RemoveActivity
                Intent intent = new Intent(AddRemoveActivity.this, HomeActivity.class);
                intent.putExtra("User",finalUser);
                startActivity(intent);
            }
        });
    }
}

