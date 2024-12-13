package com.example.phonemanagement.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.phonemanagement.Adapters.PaymentAdapter;
import com.example.phonemanagement.Adapters.PhoneItemUserAdapter;
import com.example.phonemanagement.Models.Order;
import com.example.phonemanagement.Models.OrderDetail;
import com.example.phonemanagement.Models.PhoneItem;
import com.example.phonemanagement.Models.Response.OrderResponse;
import com.example.phonemanagement.R;
import com.example.phonemanagement.Services.IOrderApiService;
import com.example.phonemanagement.Services.IVnPayApiService;
import com.example.phonemanagement.Utils.UnsafeOkHttpClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PhoneItemActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private PhoneItemUserAdapter adapter;
    private Button close_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_item);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);

        // Initialize RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PhoneItemUserAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        close_button = findViewById(R.id.close_button);
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Fetch data for a specific user
        loadPhoneItemByUserID();
    }
    private void loadPhoneItemByUserID() {
        // Lấy JWT từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String jwtToken = sharedPreferences.getString("jwtToken", null);

        if (jwtToken != null) {
            // Giải mã JWT để lấy userID
            DecodedJWT decodedJWT = JWT.decode(jwtToken);
            String userIdString = decodedJWT.getClaim("userId").asString(); // Lấy userId dưới dạng chuỗi

            if (userIdString != null) {
                try {
                    int userId = Integer.parseInt(userIdString); // Chuyển đổi chuỗi thành int
                    fetchPhoneItemData(userId); // Gọi hàm loadCartItems với userId đã lấy được
                } catch (NumberFormatException e) {
                    Toast.makeText(PhoneItemActivity.this, "Invalid userId format", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(PhoneItemActivity.this, "Không tìm thấy userId trong token", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(PhoneItemActivity.this, "Token không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
    private void fetchPhoneItemData(int userId) {
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://10.0.2.2:7295/odata/")
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IOrderApiService service = retrofit.create(IOrderApiService.class);
        Call<List<Order>> call = service.getOrdersByUserId(userId);

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<Order> orders = response.body();
                    List<PhoneItem> allPhoneItems = new ArrayList<>();

                    // Extract phone items from orders
                    for (Order order : orders) {
                        if (order.getOrderDetails() != null) {
                            for (OrderDetail detail : order.getOrderDetails()) {
                                allPhoneItems.addAll(detail.getPhoneItems());
                            }
                        }
                    }

                    // Update adapter data
                    adapter.updateData(allPhoneItems);

                } else {
                    Log.e("API Error", "Response Code: " + response.code());
                    Toast.makeText(PhoneItemActivity.this, "Failed to load orders", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("API Error", "Error: " + t.getMessage());
                Toast.makeText(PhoneItemActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
