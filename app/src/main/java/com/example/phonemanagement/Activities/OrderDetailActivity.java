package com.example.phonemanagement.Activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phonemanagement.Adapters.OrderDetailAdapter;
import com.example.phonemanagement.Models.Order;
import com.example.phonemanagement.Models.OrderDetail;
import com.example.phonemanagement.R;
import com.example.phonemanagement.Services.IOrderApiService;
import com.example.phonemanagement.Utils.UnsafeOkHttpClient;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView orderId, orderStatus, orderDate, orderTotal;
    private RecyclerView orderDetailRecyclerView;
    private OrderDetailAdapter orderDetailAdapter;
    private IOrderApiService orderService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> finish());
        // Bind views
        orderId = findViewById(R.id.orderId);
        orderStatus = findViewById(R.id.orderStatus);
        orderDate = findViewById(R.id.orderDate);
        orderTotal = findViewById(R.id.orderTotal);
        orderDetailRecyclerView = findViewById(R.id.orderDetailRecyclerView);

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://10.0.2.2:7295/odata/")  // Replace with your base URL
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        orderService = retrofit.create(IOrderApiService.class);

        // Get order ID from Intent
        int orderId = getIntent().getIntExtra("orderId", -1);
        if (orderId != -1) {
            fetchOrderDetails(orderId);
        } else {
            Toast.makeText(this, "Invalid Order ID!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void fetchOrderDetails(int orderId) {
        Call<Order> call = orderService.getOrderById(orderId);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populateOrderDetails(response.body());
                } else {
                    Toast.makeText(OrderDetailActivity.this, "Failed to fetch order details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(OrderDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateOrderDetails(Order order) {
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        // Populate order summary details
        this.orderId.setText("Order ID: " + order.getOrderId());
        orderStatus.setText("Status: " + order.getStatus());
        orderDate.setText("Date: " + order.getOrderDate());
        orderTotal.setText("Total: " + numberFormat.format(order.getTotalAmount()) + "VND");

        // Set up RecyclerView for order details
        List<OrderDetail> orderDetails = order.getOrderDetails();
        if (orderDetails != null && !orderDetails.isEmpty()) {
            orderDetailAdapter = new OrderDetailAdapter(orderDetails);
            orderDetailRecyclerView.setAdapter(orderDetailAdapter);
            orderDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            Toast.makeText(this, "No order details available", Toast.LENGTH_SHORT).show();
        }
    }
}
