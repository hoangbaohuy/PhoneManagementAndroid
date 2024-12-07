package com.example.phonemanagement.Services;

import com.example.phonemanagement.Models.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IUserApiService {
    @GET("Users/{id}")
    Call<User> getUserById(@Path("id") int id);
}
