package com.example.blooddonationapp;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.ViewHolder> {

    private List<DonorItem> donorList;

    public DonorAdapter(List<DonorItem> donorList) {
        this.donorList = donorList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DonorItem donor = donorList.get(position);
        holder.bind(donor);
    }

    @Override
    public int getItemCount() {
        return donorList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView donorNameTextView;
        private TextView donationDateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            donorNameTextView = itemView.findViewById(R.id.donorNameTextView);
            donationDateTextView = itemView.findViewById(R.id.donationDateTextView);
        }

        public void bind(DonorItem donor) {
            donorNameTextView.setText("Donor: " + donor.getFname() + " " + donor.getLname());
            donationDateTextView.setText("Donation Date: " + donor.getDate());
        }
    }
}
