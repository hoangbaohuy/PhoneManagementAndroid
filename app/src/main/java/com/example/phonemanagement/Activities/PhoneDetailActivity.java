package com.example.phonemanagement.Activities;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bumptech.glide.Glide;
import com.example.phonemanagement.Models.Order;
import com.example.phonemanagement.Models.OrderDetail;
import com.example.phonemanagement.Models.Phone;
import com.example.phonemanagement.R;
import com.example.phonemanagement.Services.IOrderApiService;
import com.example.phonemanagement.Utils.UnsafeOkHttpClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PhoneDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_detail);

        // Get references to the UI elements
        ImageView phoneImageView = findViewById(R.id.phoneImageView);
        TextView phoneIdTextView = findViewById(R.id.phoneIdTextView);
        TextView modelNameTextView = findViewById(R.id.modelNameTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        TextView priceTextView = findViewById(R.id.priceTextView);
        TextView stockTextView = findViewById(R.id.stockTextView);
        TextView chipsetTextView = findViewById(R.id.chipsetTextView);
        TextView gpuTextView = findViewById(R.id.gpuTextView);
        TextView colorTextView = findViewById(R.id.colorTextView);
        TextView warrantyTextView = findViewById(R.id.warrantyTextView);
        TextView releaseDateTextView = findViewById(R.id.releaseDateTextView);
        TextView operatingSystemTextView = findViewById(R.id.operatingSystemTextView);
        TextView ramTextView = findViewById(R.id.ramTextView);
        TextView storageTextView = findViewById(R.id.storageTextView);
        TextView brandNameTextView = findViewById(R.id.brandNameTextView);

        // Get data passed from the previous activity (HomeFragment)
        Intent intent = getIntent();
        phoneIdTextView.setText("" + intent.getIntExtra("phoneId",0));
        modelNameTextView.setText(intent.getStringExtra("phoneModel"));
        descriptionTextView.setText(intent.getStringExtra("phoneDescription"));
        priceTextView.setText("$" + intent.getDoubleExtra("phonePrice", 0.0));
        stockTextView.setText("Stock: " + intent.getIntExtra("phoneStock", 0));
        chipsetTextView.setText("Chipset: " + intent.getStringExtra("phoneChipset"));
        gpuTextView.setText("GPU: " + intent.getStringExtra("phoneGpu"));
        colorTextView.setText("Color: " + intent.getStringExtra("phoneColor"));
        warrantyTextView.setText("Warranty Period: " + intent.getIntExtra("phoneWarrantyPeriod", 0) + " months");
        releaseDateTextView.setText("Release Date: " + intent.getStringExtra("phoneReleaseDate"));
        operatingSystemTextView.setText("OS: " + intent.getStringExtra("phoneOperatingSystem"));
        ramTextView.setText("RAM: " + intent.getIntExtra("phoneRam", 0) + "GB");
        storageTextView.setText("Storage: " + intent.getIntExtra("phoneStorage", 0) + "GB");
        brandNameTextView.setText("Brand: " + intent.getStringExtra("phoneBrandName"));

//        // Load image (Optional)
//        String imageUrl = intent.getStringExtra("phoneImage");
//        Glide.with(this)
//                .load(imageUrl)
//                .placeholder(R.drawable.placeholder) // Optional placeholder
//                .error(R.drawable.error) // Optional error image
//                .into(phoneImageView);
        setupAddToCartButton();
    }

    private void setupAddToCartButton() {
        findViewById(R.id.addToCartButton).setOnClickListener(v -> {

            // Get JWT token from SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            String jwtToken = sharedPreferences.getString("jwtToken", null);

            if (jwtToken != null) {
                try {
                    // Decode the JWT token to extract userId
                    DecodedJWT decodedJWT = JWT.decode(jwtToken);
                    String userIdString = decodedJWT.getClaim("userId").asString();

                    if (userIdString != null) {
                        int userId = Integer.parseInt(userIdString); // Convert userId to integer

                        // Retrieve phoneId and quantity
                        int phoneId = getIntent().getIntExtra("phoneId", 0); // Retrieve phoneId from intent
                        int quantity = 1; // You can modify this based on user input

                        // Create the request body
                        OrderDetail orderDetail = new OrderDetail(phoneId, quantity);
                        List<OrderDetail> orderDetails = new ArrayList<>();
                        orderDetails.add(orderDetail);
                        Order order = new Order(userId, orderDetails);

                        // Set up Retrofit
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("https://10.0.2.2:7295/odata/")
                                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        IOrderApiService orderService = retrofit.create(IOrderApiService.class);

                        // Make the API call
                        orderService.placeOrder(order).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(PhoneDetailActivity.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(PhoneDetailActivity.this, "Failed to place order. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(PhoneDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(PhoneDetailActivity.this, "Invalid userId in JWT token", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(PhoneDetailActivity.this, "Error decoding JWT: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(PhoneDetailActivity.this, "JWT token not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

}