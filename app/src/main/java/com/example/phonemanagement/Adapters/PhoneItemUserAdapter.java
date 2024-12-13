package com.example.phonemanagement.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phonemanagement.Models.PhoneItem;
import com.example.phonemanagement.R;

import java.util.ArrayList;
import java.util.List;

public class PhoneItemUserAdapter extends RecyclerView.Adapter<PhoneItemUserAdapter.ViewHolder> {
    private List<PhoneItem> phoneItems = new ArrayList<>(); // Initialize to avoid null issues

    public PhoneItemUserAdapter(List<PhoneItem> phoneItems) {
        if (phoneItems != null) {
            this.phoneItems = phoneItems;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.phone_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PhoneItem phoneItem = phoneItems.get(position);

        // Safely bind data
        holder.serialNumber.setText(phoneItem.getSerialNumber() != null ? "Serial Number: " + phoneItem.getSerialNumber() : "N/A");
        holder.status.setText(phoneItem.getStatus() != null ? "Status: " + phoneItem.getStatus() : "N/A");
        holder.dateImported.setText(phoneItem.getDateImported() != null ? "Date Imported: " + phoneItem.getDateImported() : "N/A");
        holder.datePurchased.setText(phoneItem.getDatePurchased() != null ? "Date Purchased: " +  phoneItem.getDatePurchased() : "N/A");
        holder.expiryDate.setText(phoneItem.getExpiryDate() != null ? "Expiry Date: " + phoneItem.getExpiryDate() : "N/A");
    }

    @Override
    public int getItemCount() {
        return phoneItems != null ? phoneItems.size() : 0;
    }

    public void updateData(List<PhoneItem> newPhoneItems) {
        if (newPhoneItems != null) {
            this.phoneItems.clear();
            this.phoneItems.addAll(newPhoneItems);
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView serialNumber, status, expiryDate, datePurchased, dateImported;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serialNumber = itemView.findViewById(R.id.tvSerialNumber);
            status = itemView.findViewById(R.id.tvStatus);
            expiryDate = itemView.findViewById(R.id.tvExpiryDate);
            datePurchased = itemView.findViewById(R.id.tvDatePurchased);
            dateImported = itemView.findViewById(R.id.tvDateImported);
        }
    }
}