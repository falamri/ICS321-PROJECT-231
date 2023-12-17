package com.example.blooddonationapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CollectionDriveAdapter extends RecyclerView.Adapter<CollectionDriveAdapter.ViewHolder> {

    private List<Drive> collectionDriveList;

    public CollectionDriveAdapter(List<Drive> collectionDriveList) {
        this.collectionDriveList = collectionDriveList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collection_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Drive collectionDrive = collectionDriveList.get(position);
        holder.bind(collectionDrive);
    }

    @Override
    public int getItemCount() {
        return collectionDriveList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView driveLocationTextView;
        private TextView driveDateTextView;
        private TextView driveIdTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            driveLocationTextView = itemView.findViewById(R.id.driveLocationTextView);
            driveDateTextView = itemView.findViewById(R.id.driveDateTextView);
            driveIdTextView = itemView.findViewById(R.id.driveIdTextView);
        }

        public void bind(Drive collectionDrive) {
            driveLocationTextView.setText("Location: " + collectionDrive.getLocation());
            driveDateTextView.setText("Drive Date: " + collectionDrive.getStart_data());
            driveIdTextView.setText("Drive ID: " + collectionDrive.getId());
        }
    }
}

