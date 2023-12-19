package com.example.blooddonationapp;

// ReceiveActivity.java

// ReceiveActivity.java

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReceiveActivity extends AppCompatActivity {

    private RecyclerView bloodBagsRecyclerView;
    private BloodBagAdapter bloodBagAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive);
        // Initialize RecyclerView
        bloodBagsRecyclerView = findViewById(R.id.bloodBagsRecyclerView);
        ImageButton gobackButton=findViewById(R.id.goBackButton);
        gobackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=getIntent();
                Person user=null;
                if(intent!=null)
                    user= (Person) intent.getSerializableExtra("User");
                final Person finalUser=user;
                String source=intent.getStringExtra("Source");
                if(source.equals("Admin"))
                    intent = new Intent(ReceiveActivity.this, AdminPage.class);
                else
                    intent = new Intent(ReceiveActivity.this, UserPage.class);
                intent.putExtra("User", finalUser);
                startActivity(intent);
            }
        });
        // Assuming you have initialized the bloodBagList
        List<BloodBagItem> bloodBagList = getBloodBagList();

        // Initialize the BloodBagAdapter with the list of blood bags
        bloodBagAdapter = new BloodBagAdapter(bloodBagList);

        // Set layout manager and adapter for blood bags RecyclerView
        bloodBagsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bloodBagsRecyclerView.setAdapter(bloodBagAdapter);
    }

    // Method to get a list of blood bags (BloodBagItem)
    private List<BloodBagItem> getBloodBagList() {
        Intent intent = getIntent();
        Person user = null;
        if (intent != null)
            user = (Person) intent.getSerializableExtra("User");

        final Person finalUser = user;
        Log.d("BloodType",finalUser.getBloodType() );
        // Populate your blood bag list here
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        return db.getBloodBagItemsByBloodID(String.valueOf(finalUser.getBloodId(finalUser.getBloodType())));
    }

    // Adapter for BloodBag RecyclerView
    private class BloodBagAdapter extends RecyclerView.Adapter<BloodBagAdapter.BloodBagViewHolder> {

        private List<BloodBagItem> bloodBagList;

        public BloodBagAdapter(List<BloodBagItem> bloodBagList) {
            this.bloodBagList = bloodBagList;
        }

        @NonNull
        @Override
        public BloodBagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_bag_item, parent, false);
            return new BloodBagViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BloodBagViewHolder holder, int position) {
            BloodBagItem bloodBagItem = bloodBagList.get(position);
            holder.bind(bloodBagItem);
        }

        @Override
        public int getItemCount() {
            return bloodBagList.size();
        }

        // ViewHolder for BloodBag
        public class BloodBagViewHolder extends RecyclerView.ViewHolder {

            private TextView donorNameTextView;
            private TextView bloodTypeTextView;
            private Button receiveButton;

            public BloodBagViewHolder(@NonNull View itemView) {
                super(itemView);
                donorNameTextView = itemView.findViewById(R.id.donorNameTextView);
                bloodTypeTextView = itemView.findViewById(R.id.bloodTypeTextView);
                receiveButton = itemView.findViewById(R.id.receiveButton);
            }

            public void bind(BloodBagItem bloodBagItem) {
                // Get donor information and populate the views
                String donorName = bloodBagItem.getName(); // Assuming you have this method in BloodBagItem
                String bagID = String.valueOf(bloodBagItem.getBloodBagID());

                donorNameTextView.setText("Name: "+donorName+"\n");
                bloodTypeTextView.setText("Bag ID: "+bagID);

                // Set click listener for receive button
                receiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = getIntent();
                        Person user = null;
                        if (intent != null)
                            user = (Person) intent.getSerializableExtra("User");

                        final Person finalUser = user;
                        DatabaseHelper db=new DatabaseHelper(getApplicationContext());
                        db.insertRecipient(finalUser);
                        db.removeBag(Integer.parseInt(bagID));
                        Toast.makeText(ReceiveActivity.this, "You Received a blood bag successfully", Toast.LENGTH_SHORT).show();
                        intent = new Intent(ReceiveActivity.this, AdminPage.class);
                        intent.putExtra("User", finalUser);
                        startActivity(intent);

                        // Perform actions on button click (insert into Recipient table and go back to HomePage)
                        // ...
                    }
                });
            }

        }
    }
}
