package com.example.phonemanagement.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.phonemanagement.Adapters.OrderAdapter;
import com.example.phonemanagement.Models.Order;
import com.example.phonemanagement.R;
import com.example.phonemanagement.Services.IOrderApiService;
import com.example.phonemanagement.Utils.UnsafeOkHttpClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerViewOrders;
    private OrderAdapter orderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerViewOrders = view.findViewById(R.id.recyclerViewOrders);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        orderAdapter = new OrderAdapter(requireContext(), new ArrayList<>());

        recyclerViewOrders.setAdapter(orderAdapter);

        loadOrderByUserID();

        return view;
    }
    private void loadOrderByUserID() {
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
                    fetchOrderData(userId); // Gọi hàm loadCartItems với userId đã lấy được
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
    private void fetchOrderData(int userId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://10.0.2.2:7295/odata/")
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IOrderApiService orderApiService = retrofit.create(IOrderApiService.class);

        Call<List<Order>> call = orderApiService.getOrdersByUserIdAndStatus(
                userId,
                "status eq 'Completed'"
        );
        Log.d("HistoryFragment", "Request URL: " + call.request().url());
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Order> orders = response.body();
                    orderAdapter.updateOrders(orders);
                } else {
                    Log.e("HistoryFragment", "Response not successful");
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.e("HistoryFragment", "Failed to fetch orders", t);
            }
        });
    }
}
