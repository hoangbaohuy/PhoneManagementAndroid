package com.example.phonemanagement.Services;

import com.example.phonemanagement.Models.Request.UserUpdateRequest;
import com.example.phonemanagement.Models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IUserApiService {
    @GET("Users/{id}")
    Call<User> getUserById(@Path("id") int id);
    @PUT("Users/edit-profile/{userId}")
    Call<Void> updateUserProfile(@Path("userId") int userId, @Body UserUpdateRequest updateRequest);
}
