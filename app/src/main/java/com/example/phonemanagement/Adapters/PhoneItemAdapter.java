package com.example.phonemanagement.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phonemanagement.Models.PhoneItem;
import com.example.phonemanagement.R;

import java.util.List;

public class PhoneItemAdapter extends RecyclerView.Adapter<PhoneItemAdapter.PhoneItemViewHolder> {

    private List<PhoneItem> phoneItems;

    public PhoneItemAdapter(List<PhoneItem> phoneItems) {
        this.phoneItems = phoneItems;
    }

    @NonNull
    @Override
    public PhoneItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_phone_item, parent, false);
        return new PhoneItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneItemViewHolder holder, int position) {
        PhoneItem phoneItem = phoneItems.get(position);
        holder.bind(phoneItem);
    }

    @Override
    public int getItemCount() {
        return phoneItems.size();
    }

    static class PhoneItemViewHolder extends RecyclerView.ViewHolder {

        private TextView serialNumber, status, datePurchased, expiryDate;

        public PhoneItemViewHolder(@NonNull View itemView) {
            super(itemView);
            serialNumber = itemView.findViewById(R.id.serialNumber);
            status = itemView.findViewById(R.id.status);
            datePurchased = itemView.findViewById(R.id.datePurchased);
            expiryDate = itemView.findViewById(R.id.expiryDate);
        }

        public void bind(PhoneItem phoneItem) {
            serialNumber.setText("Serial: " + phoneItem.getSerialNumber());
            status.setText("Status: " + phoneItem.getStatus());
            datePurchased.setText("Purchased: " + phoneItem.getDatePurchased());
            expiryDate.setText("Expiry: " + phoneItem.getExpiryDate());
        }
    }
}