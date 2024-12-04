package com.example.phonemanagement.Services;

import com.example.phonemanagement.Models.Phone;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IPhoneApiService {
    @GET("Phone/details")
    Call<List<Phone>> getPhoneDetails();
}
