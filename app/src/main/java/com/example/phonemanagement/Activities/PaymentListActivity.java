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
import com.example.phonemanagement.Models.Response.OrderResponse;
import com.example.phonemanagement.R;
import com.example.phonemanagement.Services.IVnPayApiService;
import com.example.phonemanagement.Utils.UnsafeOkHttpClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaymentListActivity extends AppCompatActivity {
    private Button close_button;
    private RecyclerView recyclerViewOrders;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_list);
        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);
        progressBar = findViewById(R.id.progressBar);
        close_button = findViewById(R.id.close_button);
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));
        loadPaymentByUserID();
    }
    private void loadPaymentByUserID() {
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
                    fetchPaymentData(userId); // Gọi hàm loadCartItems với userId đã lấy được
                } catch (NumberFormatException e) {
                    Toast.makeText(PaymentListActivity.this, "Invalid userId format", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(PaymentListActivity.this, "Không tìm thấy userId trong token", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(PaymentListActivity.this, "Token không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
    private void fetchPaymentData(int userId){
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://10.0.2.2:7295/api/")
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Initialize Retrofit
        IVnPayApiService service = retrofit.create(IVnPayApiService.class);

        // Call the API
        Call<List<OrderResponse>> call = service.getOrdersByUserId(userId);

        call.enqueue(new Callback<List<OrderResponse>>() {
            @Override
            public void onResponse(Call<List<OrderResponse>> call, Response<List<OrderResponse>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<OrderResponse> orders = response.body();
                    PaymentAdapter adapter = new PaymentAdapter(orders);
                    recyclerViewOrders.setAdapter(adapter);
                } else {
                    Log.e("API Error", "Response Code: " + response.code());
                    Toast.makeText(PaymentListActivity.this, "Failed to load orders", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("API Error", "Error: " + t.getMessage());
                Toast.makeText(PaymentListActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
