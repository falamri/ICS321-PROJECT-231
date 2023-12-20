package com.example.blooddonationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private Person user;

    private EditText etFname;
    private EditText etLname;
    private EditText etDob;
    private EditText etNum;
    private EditText etAddress;
    private EditText etWeight;
    private EditText etBloodType;
    private TextView etDonationNum;
    private TextView etReceiveNum;
    private EditText etPassword;
    private EditText etEmail;
    private Button editBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        DatabaseHelper db=new DatabaseHelper(getApplicationContext());
        // Retrieve the ProfileItem from the intent
        user = (Person) getIntent().getSerializableExtra("User");
        ProfileItem userProfile=new ProfileItem(user,db.getDonationNum(user.getId()),db.getReceiveNum(user.getId()));

        if (userProfile == null || userProfile.getPerson() == null) {
            // Handle error case where userProfile or user inside it is null
            finish();
            return;
        }
        ImageButton goBackButton=findViewById(R.id.goBackButton);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=getIntent();
                Person user=null;
                if (intent != null) {
                    user = (Person) intent.getSerializableExtra("User");
                }
                // Set click listeners for buttons
                Person finalUser = user;
                intent = new Intent(ProfileActivity.this, UserPage.class);
                intent.putExtra("User",finalUser);
                startActivity(intent);

            }
        });
        editBtn=findViewById(R.id.editButton);

        // Initialize EditText fields
        etFname = findViewById(R.id.textViewFname);
        etLname = findViewById(R.id.textViewLname);
        etDob = findViewById(R.id.textViewDob);
        etNum = findViewById(R.id.textViewNum);
        etAddress = findViewById(R.id.textViewAddress);
        etWeight = findViewById(R.id.textViewWeight);
        etBloodType = findViewById(R.id.textViewBloodType);
        etDonationNum = findViewById(R.id.textViewDonationNum);
        etReceiveNum = findViewById(R.id.textViewReceiveNum);
        etPassword = findViewById(R.id.textViewPassword);
        etEmail = findViewById(R.id.textViewEmail);

        // Populate EditText fields with userProfile data
        Person user = userProfile.getPerson();
        etFname.setText(user.getFname());
        etLname.setText(user.getLname());
        etDob.setText(user.getDob());
        etNum.setText(user.getNum());
        etAddress.setText(user.getAddress());
        etWeight.setText(String.valueOf(user.getWeight()));
        etBloodType.setText(user.getBloodType());
        etDonationNum.setText(String.valueOf("Number of Donations: "+userProfile.getDonationNum()));
        etReceiveNum.setText(String.valueOf("Number of blood bags Received: "+ userProfile.getReceiveNum()));
        etPassword.setText(user.getPassword());
        etEmail.setText(user.getEmail());

        // Allow the fields to be editable
        etFname.setEnabled(true);
        etLname.setEnabled(true);
        etDob.setEnabled(true);
        etNum.setEnabled(true);
        etAddress.setEnabled(true);
        etWeight.setEnabled(true);
        etBloodType.setEnabled(true);
        etDonationNum.setEnabled(false);
        etReceiveNum.setEnabled(false);
        etPassword.setEnabled(true);
        etEmail.setEnabled(true);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db=new DatabaseHelper(getApplicationContext());
                int id= user.getId();
                int weight= Integer.parseInt(etWeight.getText().toString());
                Person newUser=new Person(id,etPassword.getText().toString(),etEmail.getText().toString(),
                        etFname.getText().toString(),etLname.getText().toString(),
                        etDob.getText().toString(),etNum.getText().toString(),
                        etAddress.getText().toString(),weight,
                        etBloodType.getText().toString());
                boolean result=db.updateUser(newUser);
                Toast.makeText(ProfileActivity.this, "User Updated successfully", Toast.LENGTH_SHORT).show();

            }
        });
    }

    }




