package com.example.phonemanagement.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.phonemanagement.Models.OrderDetail;
import com.example.phonemanagement.R;
import com.example.phonemanagement.Services.IOrderApiService;
import com.example.phonemanagement.Utils.UnsafeOkHttpClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<OrderDetail> orderDetails;

    public CartAdapter(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public void updateOrderDetails(List<OrderDetail> details) {
        this.orderDetails.clear();  // Clear the existing data
        this.orderDetails.addAll(details);  // Add the new data
        notifyDataSetChanged();  // Notify the adapter that the data has changed
    }

    // Add this getter method to return the list of OrderDetails
    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        OrderDetail detail = orderDetails.get(position);
        String orderIdValue = "" + detail.getOrderId(); // Replace with actual logic to fetch OrderId
        holder.bind(detail, orderIdValue);

        holder.deleteButton.setOnClickListener(v -> {
            deleteOrderDetail(detail.getOrderId(), detail.getOrderDetailId(), position);
        });
    }

    private void deleteOrderDetail(int orderId, int orderDetailId, int position) {
        // Use Retrofit to call the DELETE API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://10.0.2.2:7295/odata/")
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IOrderApiService orderApiService = retrofit.create(IOrderApiService.class);

        Call<Void> call = orderApiService.deleteOrderDetail(orderId, orderDetailId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Remove the item from the list and update the adapter
                    orderDetails.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, orderDetails.size());
                } else {
                    Log.e("CartAdapter", "Delete failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("CartAdapter", "Delete failed", t);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {

        private TextView orderId, orderDetailId, phoneName, quantity, price;
        private ImageView phoneImage, deleteButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDetailId = itemView.findViewById(R.id.orderDetailId);
            orderId = itemView.findViewById(R.id.orderId);
            phoneName = itemView.findViewById(R.id.phoneName);
            quantity = itemView.findViewById(R.id.quantity);
            price = itemView.findViewById(R.id.price);
            phoneImage = itemView.findViewById(R.id.phoneImage);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public void bind(OrderDetail detail, String orderIdValue) {
            // Bind the OrderId
            orderId.setText("Order ID: " + orderIdValue);
            orderDetailId.setText("OrderDetail ID: " + detail.getOrderDetailId());
            // Bind the rest of the details
            phoneName.setText("Phone ID: " + detail.getPhoneId()); // Replace with phone name if available
            quantity.setText("Quantity: " + detail.getQuantity());
            price.setText("Price: $" + detail.getUnitPrice());

            // Load the image using Glide or Picasso (optional)
            // Glide.with(itemView.getContext())
            //     .load("https://your_base_image_url/" + detail.getImage())
            //     .into(phoneImage);
            Glide.with(itemView.getContext())
                    .load(detail.getImage())
                    .placeholder(R.drawable.replace) // Optional placeholder
                    .error(R.drawable.replace) // Optional error image
                    .into(phoneImage);
        }
    }
}
