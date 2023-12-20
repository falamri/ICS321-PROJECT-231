package com.example.blooddonationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditUserActivity extends AppCompatActivity {

    private EditText idInput;
    private Button btnSearch;
    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText,
            phoneNumberEditText, dateOfBirthEditText, addressEditText, weightEditText;
    private Spinner bloodTypeSpinner;
    private Button editBtn;

    private RelativeLayout layoutPersonDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user);
        firstNameEditText = findViewById(R.id.fname);
        lastNameEditText = findViewById(R.id.lname);
        emailEditText = findViewById(R.id.email1);
        passwordEditText = findViewById(R.id.password1);
        phoneNumberEditText = findViewById(R.id.phoneNumber);
        dateOfBirthEditText = findViewById(R.id.dateOfBirth);
        addressEditText = findViewById(R.id.address);
        weightEditText = findViewById(R.id.weight);
        bloodTypeSpinner = findViewById(R.id.bloodTypeSpinner);
        editBtn = findViewById(R.id.loginButton);


        idInput = findViewById(R.id.idInput);
        btnSearch = findViewById(R.id.findUserButton);
        ImageButton btnGoBack=findViewById(R.id.goBackButton);
        layoutPersonDetails = findViewById(R.id.layout2);
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
                intent = new Intent(EditUserActivity.this, AddRemoveActivity.class);
                intent.putExtra("User",finalUser);
                startActivity(intent);

            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db=new DatabaseHelper(getApplicationContext());
                String personIdStr = idInput.getText().toString();

                try {
                    int personId = Integer.parseInt(personIdStr);
                    Person person = db.getPersonById(personId);
                    if (person != null) {
                        // Handle the person object
                        layoutPersonDetails.setVisibility(View.VISIBLE);
                        // Now populate the details in the layout based on the retrieved person object
                        populatePersonDetails(person); // You'll need to implement this method

                    } else {
                        Toast.makeText(EditUserActivity.this, "User not found!", Toast.LENGTH_SHORT).show();
                    }

                } catch (NumberFormatException e) {
                    // Handle the case where the entered value is not a valid integer
                    Toast.makeText(EditUserActivity.this, "Please enter a valid ID", Toast.LENGTH_SHORT).show();
                }


            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db=new DatabaseHelper(getApplicationContext());
                int id= Integer.parseInt(idInput.getText().toString());
                int weight= Integer.parseInt(weightEditText.getText().toString());
                Person newUser=new Person(id,passwordEditText.getText().toString(),emailEditText.getText().toString(),
                        firstNameEditText.getText().toString(),lastNameEditText.getText().toString(),
                        dateOfBirthEditText.getText().toString(),phoneNumberEditText.getText().toString(),
                        addressEditText.getText().toString(),weight,
                        bloodTypeSpinner.getSelectedItem().toString());
                boolean result=db.updateUser(newUser);
                Toast.makeText(EditUserActivity.this, "User Updated successfully", Toast.LENGTH_SHORT).show();
                Intent intent=getIntent();
                Person user=null;
                if (intent != null) {
                    user = (Person) intent.getSerializableExtra("User");
                }
                // Set click listeners for buttons
                Person finalUser = user;
                intent = new Intent(EditUserActivity.this, AddRemoveActivity.class);
                intent.putExtra("User",finalUser);
                startActivity(intent);

            }
        });
    }

    private void populatePersonDetails(Person person) {
        if (person != null) {
            // Set IDs for EditText widgets since they are editable
            EditText editTextFname = findViewById(R.id.fname);
            EditText editTextLname = findViewById(R.id.lname);
            EditText editTextDob = findViewById(R.id.dateOfBirth);
            EditText editTextContactNum = findViewById(R.id.phoneNumber);
            EditText editTextEmail = findViewById(R.id.email1);
            EditText editTextAddress = findViewById(R.id.address);
            EditText editTextWeight = findViewById(R.id.weight);
            EditText editTextPassword = findViewById(R.id.password1);

            // Set values from the Person object to the respective EditText widgets
            editTextFname.setText(person.getFname());
            editTextLname.setText(person.getLname());
            editTextDob.setText(person.getDob());
            editTextContactNum.setText(person.getNum());
            editTextEmail.setText(person.getEmail());
            editTextAddress.setText(person.getAddress());
            editTextWeight.setText(String.valueOf(person.getWeight())); // Assuming getWeight returns a float/double
            editTextPassword.setText(person.getPassword());


            // Make the EditText fields editable
            editTextFname.setEnabled(true);
            editTextLname.setEnabled(true);
            editTextDob.setEnabled(true);
            editTextContactNum.setEnabled(true);
            editTextEmail.setEnabled(true);
            editTextAddress.setEnabled(true);
            editTextWeight.setEnabled(true);
            editTextPassword.setEnabled(true);

        } else {
            Toast.makeText(this, "No details available for the given person ID", Toast.LENGTH_SHORT).show();
        }
    }

}
