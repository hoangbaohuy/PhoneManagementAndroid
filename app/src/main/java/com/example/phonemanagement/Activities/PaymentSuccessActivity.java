package com.example.phonemanagement.Activities;


import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phonemanagement.Adapters.CartAdapter;
import com.example.phonemanagement.R;
import com.example.phonemanagement.Services.IOrderApiService;
import com.example.phonemanagement.Services.IVnPayApiService;
import com.example.phonemanagement.Utils.UnsafeOkHttpClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaymentSuccessActivity extends AppCompatActivity {
    private Button close_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_success);
        close_button = findViewById(R.id.close_button);
        int intOrderId = getIntent().getIntExtra("orderId", -1);
        if (intOrderId != -1) {
            checkoutCart(intOrderId);
            doneOrder(intOrderId);
        }
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

                    Toast.makeText(PaymentSuccessActivity.this, "Checkout completed successfully", Toast.LENGTH_SHORT).show();
                    // Refresh cart items
                } else {
                    Log.e("Payment Success", "Failed with error code: " + response.code());
                    Toast.makeText(PaymentSuccessActivity.this, "Checkout failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(PaymentSuccessActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doneOrder(int orderId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://10.0.2.2:7295/api/")
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IVnPayApiService vnPayApiService = retrofit.create(IVnPayApiService.class);
        Call<Void> call = vnPayApiService.doneOrder(orderId);

        Log.d("PaymentSuccessActivity", "Request URL: " + call.request().url());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PaymentSuccessActivity.this, "Done order completed successfully", Toast.LENGTH_SHORT).show();

                } else {
                    Log.e("PaymentSuccessActivity", "Done order failed with status code: " + response.code());
                    Toast.makeText(PaymentSuccessActivity.this, "Done order failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("PaymentSuccessActivity", "API call failed: " + t.getMessage(), t);
                Toast.makeText(PaymentSuccessActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
