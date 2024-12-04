package com.example.phonemanagement.Services;

import com.example.phonemanagement.Models.Request.LoginRequest;
import com.example.phonemanagement.Models.Response.ResponseEntity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface  IAuthApiService {
//    @POST("Authen/Register")
//    Call<RegisterResponse> register(@Body RegisterRequest request);

    @POST("api/Authen/login")
    Call<ResponseEntity> login(@Body LoginRequest request);

//    @GET("/Authen/profile")
//    Call<ProfileResponse> getProfile(@Query("userId") int userId);
}
