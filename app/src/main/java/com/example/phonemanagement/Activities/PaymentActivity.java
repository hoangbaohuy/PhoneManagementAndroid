package com.example.phonemanagement.Activities;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phonemanagement.Models.Response.VnPayResponse;
import com.example.phonemanagement.R;
import com.example.phonemanagement.Services.IVnPayApiService;
import com.example.phonemanagement.Utils.UnsafeOkHttpClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaymentActivity extends AppCompatActivity {
    private Button close_button;
    private WebView paymentWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        close_button = findViewById(R.id.close_button);
        paymentWebView = findViewById(R.id.paymentWebView);
        paymentWebView.getSettings().setJavaScriptEnabled(true); // Enable JavaScript if required
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Replace "5" with your actual order ID
        int intOrderId = getIntent().getIntExtra("orderId", -1);
        if (intOrderId != -1) {
            String orderId = String.valueOf(intOrderId);
            proceedPayment(orderId);
        } else {
            Toast.makeText(this, "Invalid Order ID!", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void proceedPayment(String orderId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://10.0.2.2:7295/api/")
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IVnPayApiService vnPayApi = retrofit.create(IVnPayApiService.class);
        Call<VnPayResponse> call = vnPayApi.proceedVnPayPayment(orderId);

        call.enqueue(new Callback<VnPayResponse>() {
            @Override
            public void onResponse(Call<VnPayResponse> call, Response<VnPayResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String paymentUrl = response.body().getPaymentUrl();
                    Log.d("PaymentActivity", "Opening payment URL in WebView: " + paymentUrl);

                    // Load the URL in the WebView instead of opening a browser
                    paymentWebView.getSettings().setJavaScriptEnabled(true);
                    paymentWebView.getSettings().setDomStorageEnabled(true);  // Enable DOM storage if required
                    paymentWebView.getSettings().setAllowContentAccess(true); // Allow content access
                    paymentWebView.getSettings().setAllowFileAccess(true);    // Allow file access
                    paymentWebView.setWebViewClient(new WebViewClient());  // Ensure links open in WebView
                    paymentWebView.setVisibility(View.VISIBLE);  // Show WebView
                    paymentWebView.loadUrl(paymentUrl);
                    paymentWebView.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            if (url.contains("/payment-callback")) {
                                // Extract query parameters from the URL
                                Uri callbackUri = Uri.parse(url);

                                String vnpResponseCode = callbackUri.getQueryParameter("vnp_ResponseCode");
                                String orderId = callbackUri.getQueryParameter("orderId");

                                if ("00".equals(vnpResponseCode)) {
                                    // Payment is successful
                                    Intent intent = new Intent(PaymentActivity.this, PaymentSuccessActivity.class);
                                    intent.putExtra("orderId", orderId);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Payment failed or was canceled
                                    Toast.makeText(PaymentActivity.this, "Payment Failed. Code: " + vnpResponseCode, Toast.LENGTH_SHORT).show();
                                    // Optionally, redirect to a failure activity or simply stay on the current screen.
                                }
                                return true; // Prevent the WebView from loading the callback URL
                            }
                            return false; // Allow the WebView to load other URLs
                        }
                    });
                    // Check payment result after the payment is completed
//                    checkPaymentResult(orderId);
                } else {
                    Toast.makeText(PaymentActivity.this, "Failed to retrieve URL", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VnPayResponse> call, Throwable t) {
                Toast.makeText(PaymentActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
