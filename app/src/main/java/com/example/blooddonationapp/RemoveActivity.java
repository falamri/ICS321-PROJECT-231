package com.example.blooddonationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RemoveActivity extends AppCompatActivity {

    private EditText editTextId;
    private Button btnRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remove);

        editTextId = findViewById(R.id.editTextId);
        btnRemove = findViewById(R.id.btnRemove);
        ImageButton btnGoBack = findViewById(R.id.goBackButton);
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                Person user = null;
                if (intent != null)
                    user = (Person) intent.getSerializableExtra("User");

                // Set click listeners for buttons
                Person finalUser = user;
                intent = new Intent(RemoveActivity.this, AddRemoveActivity.class);
                intent.putExtra("User", finalUser);
                startActivity(intent);
            }
        });



        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db=new DatabaseHelper(getApplicationContext());
                EditText editTextId = findViewById(R.id.editTextId);
                String personIdStr = editTextId.getText().toString();


                int personId = Integer.parseInt(personIdStr);
                if(db.removeUser(personId)){
                    Toast.makeText(RemoveActivity.this, "User Has been deleted!", Toast.LENGTH_SHORT).show();
                    Intent intent=getIntent();
                    Person user=null;
                    if (intent != null) {
                        user = (Person) intent.getSerializableExtra("User");
                    }
                    // Set click listeners for buttons
                    Person finalUser = user;
                    intent = new Intent(RemoveActivity.this, AddRemoveActivity.class);
                    intent.putExtra("User",finalUser);
                    startActivity(intent);

                }

                 else
                     Toast.makeText(RemoveActivity.this, "User not found!", Toast.LENGTH_SHORT).show();





            }
        });
    }


}
