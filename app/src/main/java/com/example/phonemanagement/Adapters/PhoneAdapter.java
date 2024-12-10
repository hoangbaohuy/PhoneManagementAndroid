package com.example.phonemanagement.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.phonemanagement.Models.Phone;
import com.example.phonemanagement.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.PhoneViewHolder> {

    private List<Phone> phoneList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Phone phone);
    }

    public PhoneAdapter(List<Phone> phoneList, OnItemClickListener listener) {
        this.phoneList = phoneList;
        this.listener = listener;
    }

    public void updatePhones(List<Phone> phones) {
        this.phoneList = phones;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_phone, parent, false);
        return new PhoneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneViewHolder holder, int position) {
        Phone phone = phoneList.get(position);
        holder.bind(phone, listener);
    }

    @Override
    public int getItemCount() {
        return phoneList.size();
    }

    static class PhoneViewHolder extends RecyclerView.ViewHolder {

        private final ImageView phoneImage;
        private final TextView phoneId, phoneName, phonePrice, phoneStock;

        public PhoneViewHolder(@NonNull View itemView) {
            super(itemView);
            phoneId = itemView.findViewById(R.id.phoneId);
            phoneImage = itemView.findViewById(R.id.phoneImage);
            phoneName = itemView.findViewById(R.id.phoneName);
            phonePrice = itemView.findViewById(R.id.phonePrice);
            phoneStock = itemView.findViewById(R.id.phoneStock);
        }

        public void bind(Phone phone, OnItemClickListener listener) {
            NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            phoneId.setText(""+ phone.getPhoneId());
            phoneName.setText(phone.getModelName());
            phonePrice.setText("Price: " + numberFormat.format(phone.getPrice()) + "VND");
            phoneStock.setText("Stock: " + phone.getStockQuantity());

            // Load image using Glide
            Glide.with(itemView.getContext())
                    .load(phone.getImage()) // Use a full image URL if necessary
                    .into(phoneImage);

            itemView.setOnClickListener(v -> listener.onItemClick(phone));
        }
    }
}