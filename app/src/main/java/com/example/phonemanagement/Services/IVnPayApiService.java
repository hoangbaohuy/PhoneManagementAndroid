package com.example.phonemanagement.Services;


import com.example.phonemanagement.Models.Response.OrderResponse;
import com.example.phonemanagement.Models.Response.VnPayResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IVnPayApiService {
    @POST("VnPay/proceed-vnpay-payment")
    @Headers("Content-Type: application/json")
    Call<VnPayResponse> proceedVnPayPayment(@Body String orderId);
    @POST("VnPay/{orderId}")
    Call<Void> doneOrder(@Path("orderId") int orderId);
    @GET ("VnPay/get-order-payment-by-id-user-/{id}")
    Call<List<OrderResponse>> getOrdersByUserId(@Path("id") int userId);
}
