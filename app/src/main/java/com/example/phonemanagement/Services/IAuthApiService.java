package com.example.phonemanagement.Services;

import com.example.phonemanagement.Models.Request.LoginRequest;
import com.example.phonemanagement.Models.Request.RegisterRequest;
import com.example.phonemanagement.Models.Response.RegisterResponse;
import com.example.phonemanagement.Models.Response.ResponseEntity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface  IAuthApiService {
    @POST("api/Authen/register-mobile")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @POST("api/Authen/login")
    Call<ResponseEntity> login(@Body LoginRequest request);

}
