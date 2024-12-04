package com.example.phonemanagement.Models;

import java.util.List;

public class Order {
    private int orderId;
    private int userId;
    private String orderDate;
    private double totalAmount;
    private String status;
    private List<OrderDetail> orderDetails;

    public Order(int userId, List<OrderDetail> orderDetails) {
        this.userId = userId;
        this.orderDetails = orderDetails;
    }
    // Getters and Setters

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}

