package com.example.phonemanagement.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phonemanagement.Models.OrderDetail;
import com.example.phonemanagement.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {

    private List<OrderDetail> orderDetails;

    public OrderDetailAdapter(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_detail, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        OrderDetail detail = orderDetails.get(position);
        holder.bind(detail);
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    static class OrderDetailViewHolder extends RecyclerView.ViewHolder {

        private TextView detailId, quantity, unitPrice;
        private RecyclerView phoneItemsRecyclerView;

        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            detailId = itemView.findViewById(R.id.detailId);
            quantity = itemView.findViewById(R.id.quantity);
            unitPrice = itemView.findViewById(R.id.unitPrice);
            phoneItemsRecyclerView = itemView.findViewById(R.id.phoneItemsRecyclerView);
        }

        public void bind(OrderDetail detail) {
            NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            detailId.setText("Detail ID: " + detail.getOrderDetailId());
            quantity.setText("Quantity: " + detail.getQuantity());
            unitPrice.setText("Unit Price: " + numberFormat.format(detail.getUnitPrice()) + "VND");

            // Bind phone items using PhoneItemAdapter
            PhoneItemAdapter adapter = new PhoneItemAdapter(detail.getPhoneItems());
            phoneItemsRecyclerView.setAdapter(adapter);
            phoneItemsRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }

}
