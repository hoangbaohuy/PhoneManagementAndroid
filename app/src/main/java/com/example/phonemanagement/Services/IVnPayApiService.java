package com.example.phonemanagement.Services;


import com.example.phonemanagement.Models.Response.VnPayResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IVnPayApiService {
    @POST("VnPay/proceed-vnpay-payment")
    @Headers("Content-Type: application/json")
    Call<VnPayResponse> proceedVnPayPayment(@Body String orderId);

}
