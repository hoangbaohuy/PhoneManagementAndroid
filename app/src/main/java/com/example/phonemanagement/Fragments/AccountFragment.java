package com.example.phonemanagement.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bumptech.glide.Glide;
import com.example.phonemanagement.Models.Request.UserUpdateRequest;
import com.example.phonemanagement.Models.User;
import com.example.phonemanagement.R;
import com.example.phonemanagement.Services.IUserApiService;
import com.example.phonemanagement.Utils.UnsafeOkHttpClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountFragment extends Fragment {

    private ImageView userImage;
    private TextView userFullName, userEmail, userAddress, userPhoneNumber;
    private Button btnUpdateProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        userImage = view.findViewById(R.id.userImage);
        userFullName = view.findViewById(R.id.userFullName);
        userEmail = view.findViewById(R.id.userEmail);
        userAddress = view.findViewById(R.id.userAddress);
        userPhoneNumber = view.findViewById(R.id.userPhoneNumber);
        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);
        btnUpdateProfile.setOnClickListener(v -> {
            showUpdateDialog();
        });
        loadAccountByUserID();

        return view;
    }
    private void loadAccountByUserID() {
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
                    fetchUserDetails(userId); // Gọi hàm loadCartItems với userId đã lấy được
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
    private void fetchUserDetails(int userId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://10.0.2.2:7295/odata/")
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IUserApiService userService = retrofit.create(IUserApiService.class);

        userService.getUserById(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response.body());
                } else {
                    Toast.makeText(getContext(), "Failed to fetch user details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(User user) {
        userFullName.setText(user.getFullName());
        userEmail.setText(user.getEmail());
        userAddress.setText(user.getAddress());
        userPhoneNumber.setText(user.getPhoneNumber());

        // Load image using Glide
        if (user.getImage() != null) {
            Glide.with(this)
                    .load(user.getImage())
                    .placeholder(R.drawable.replace) // Optional placeholder
                    .error(R.drawable.replace) // Optional error image
                    .into(userImage);
        } else {
            userImage.setImageResource(R.drawable.replace); // Default image
        }
    }
    private void updateUserProfile(int userId, UserUpdateRequest updateRequest) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://10.0.2.2:7295/odata/") // Replace with your backend's base URL
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IUserApiService userApiService = retrofit.create(IUserApiService.class);

        Call<Void> call = userApiService.updateUserProfile(userId, updateRequest);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    loadAccountByUserID();
                    Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Update Profile", "Failed with code: " + response.code());
                    Toast.makeText(requireContext(), "Update failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Update Profile", "Error: " + t.getMessage(), t);
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUpdateDialog() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String jwtToken = sharedPreferences.getString("jwtToken", null);

        if (jwtToken != null) {
            // Giải mã JWT để lấy userID
            DecodedJWT decodedJWT = JWT.decode(jwtToken);
            String userIdString = decodedJWT.getClaim("userId").asString(); // Lấy userId dưới dạng chuỗi

            if (userIdString != null) {
                try {
                    int userId = Integer.parseInt(userIdString); // Chuyển đổi chuỗi thành int
                    View dialogView = getLayoutInflater().inflate(R.layout.dialog_update_profile, null);

                    EditText inputFullName = dialogView.findViewById(R.id.inputFullName);
                    EditText inputImage = dialogView.findViewById(R.id.inputImage);
                    EditText inputAddress = dialogView.findViewById(R.id.inputAddress);
                    EditText inputPhoneNumber = dialogView.findViewById(R.id.inputPhoneNumber);

                    new AlertDialog.Builder(requireContext())
                            .setTitle("Update Profile")
                            .setView(dialogView)
                            .setPositiveButton("Update", (dialog, which) -> {
                                String fullName = inputFullName.getText().toString();
                                String image = inputImage.getText().toString();
                                String address = inputAddress.getText().toString();
                                String phoneNumber = inputPhoneNumber.getText().toString();

                                UserUpdateRequest updateRequest = new UserUpdateRequest();
                                updateRequest.setFullName(fullName);
                                updateRequest.setImage(image);
                                updateRequest.setAddress(address);
                                updateRequest.setPhoneNumber(phoneNumber);

                                updateUserProfile(userId, updateRequest); // Replace 123 with the actual user ID
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
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
}