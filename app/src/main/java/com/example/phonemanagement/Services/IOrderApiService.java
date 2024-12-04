package com.example.phonemanagement.Services;

import com.example.phonemanagement.Models.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IOrderApiService {
    @GET("Order/get-order-by-id-user/{id}")
    Call<List<Order>> getOrdersByUserId(@Path("id") int id);
    @GET("Order/get-order-by-id-user/{userId}")
    Call<List<Order>> getOrdersByUserIdAndStatus(
            @Path("userId") int userId,
            @Query("$filter") String filter
    );
    @DELETE("Order/orders/{orderId}/orderdetails/{orderdetailId}")
    Call<Void> deleteOrderDetail(@Path("orderId") int orderId, @Path("orderdetailId") int orderDetailId);
    @DELETE("Order/{orderId}")
    Call<Void> deleteOrder(@Path("orderId") int orderId);

    @PUT("Order/done/{orderId}")
    Call<Void> doneOrder(@Path("orderId") int orderId);

    @POST("Order")
    Call<Void> placeOrder(@Body Order order);
}
