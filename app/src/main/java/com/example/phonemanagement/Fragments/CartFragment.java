package com.example.phonemanagement.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.phonemanagement.Activities.OrderDetailActivity;
import com.example.phonemanagement.Activities.PaymentActivity;
import com.example.phonemanagement.Adapters.CartAdapter;
import com.example.phonemanagement.Models.Order;
import com.example.phonemanagement.Models.OrderDetail;
import com.example.phonemanagement.R;
import com.example.phonemanagement.Services.IOrderApiService;
import com.example.phonemanagement.Utils.UnsafeOkHttpClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CartFragment extends Fragment {

    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private Button btnClearCart, btnCheckoutCart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        btnClearCart = view.findViewById(R.id.btnClearCart);
        btnCheckoutCart = view.findViewById(R.id.btnCheckoutCart);

        recyclerViewCart = view.findViewById(R.id.recyclerViewCart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext()));
        cartAdapter = new CartAdapter(new ArrayList<>());
        recyclerViewCart.setAdapter(cartAdapter);

        btnClearCart.setOnClickListener(v -> {
            int orderId = getSelectedOrderId();
            if (orderId != -1) { // Only proceed if a valid orderId is returned
                clearCart(orderId);
            }
        });

        btnCheckoutCart.setOnClickListener(v -> {
            int orderId = getSelectedOrderId();
            if (orderId != -1) {
                Intent intent = new Intent(requireContext(), PaymentActivity.class);
                intent.putExtra("orderId", orderId); // Pass the selected order
                startActivity(intent);
            }
//            if (orderId != -1) { // Only proceed if a valid orderId is returned
//                checkoutCart(orderId);
//            }
        });
        loadCartByUserID();
        return view;
    }
    private int getSelectedOrderId() {
        if (cartAdapter != null && cartAdapter.getItemCount() > 0) {
            List<OrderDetail> orderDetails = cartAdapter.getOrderDetails();
            if (orderDetails != null && !orderDetails.isEmpty()) {
                return orderDetails.get(0).getOrderId();
            }
        }
        Toast.makeText(getContext(), "No orders available to clear/checkout", Toast.LENGTH_SHORT).show();
        return -1;
    }

    
    private void loadCartByUserID() {
        // Lấy JWT từ SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String jwtToken = sharedPreferences.getString("jwtToken", null);

        if (jwtToken != null) {
            // Giải mã JWT để lấy userID
            DecodedJWT decodedJWT = JWT.decode(jwtToken);
            String userIdString = decodedJWT.getClaim("userId").asString(); // Lấy userId dưới dạng chuỗi

            if (userIdString != null) {
                try {
                    int userId = Integer.parseInt(userIdString); // Chuyển đổi chuỗi thành int
                    fetchCartData(userId); // Gọi hàm loadCartItems với userId đã lấy được
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Invalid userId format", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Không tìm thấy userId trong token", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Token không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchCartData(int userId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://10.0.2.2:7295/odata/")
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IOrderApiService orderApiService = retrofit.create(IOrderApiService.class);

        Call<List<Order>> call = orderApiService.getOrdersByUserIdAndStatus(
                userId,
                "status eq 'Pending'"
        );
        Log.d("CartFragment", "Request URL: " + call.request().url());

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Order> orders = response.body();
                    if (!orders.isEmpty()) {
                        List<OrderDetail> details = new ArrayList<>();
                        for (Order order : orders) {
                            for (OrderDetail detail : order.getOrderDetails()) {
                                detail.setOrderId(order.getOrderId()); // Add OrderId to each detail
                                details.add(detail);
                            }
                        }
                        cartAdapter.updateOrderDetails(details);
                    }
                } else {
                    try {
                        Log.e("CartFragment", "Response not successful: " + response.code());
                        if (response.errorBody() != null) {
                            String errorMessage = response.errorBody().string();
                            Log.e("CartFragment", "Error body: " + errorMessage);
                        }
                    } catch (Exception e) {
                        Log.e("CartFragment", "Failed to parse error body", e);
                    }
                }
            }


            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.e("CartFragment", "Failed to fetch cart details", t);
            }
        });
    }
    private void clearCart(int orderId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://10.0.2.2:7295/odata/")
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IOrderApiService orderApiService = retrofit.create(IOrderApiService.class);

        Call<Void> call = orderApiService.deleteOrder(orderId);
        Log.d("CartFragment", "Request URL: " + call.request().url());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Cart cleared successfully", Toast.LENGTH_SHORT).show();
                    loadCartByUserID(); // Refresh cart items
                } else {
                    Log.e("CartFragment", "Failed with error code: " + response.code());
                    Toast.makeText(getContext(), "Failed to clear cart", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkoutCart(int orderId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://10.0.2.2:7295/odata/")
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IOrderApiService orderApiService = retrofit.create(IOrderApiService.class);

        Call<Void> call = orderApiService.doneOrder(orderId);
        Log.d("CartFragment", "Request URL: " + call.request().url());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(getContext(), "Checkout completed successfully", Toast.LENGTH_SHORT).show();
                    cartAdapter.updateOrderDetails(new ArrayList<>());  // Clear existing data first
                    loadCartByUserID();  // Reload cart after clearing
                     // Refresh cart items
                } else {
                    Log.e("CartFragment", "Failed with error code: " + response.code());
                    Toast.makeText(getContext(), "Checkout failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
