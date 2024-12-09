package com.example.phonemanagement.Models;

import java.io.Serializable;
import java.util.List;

public class OrderDetail {
    private int orderDetailId;
    private int phoneId;
    private String image;
    private int quantity;
    private double unitPrice;
    private int orderId;
    private int warrantyPeriod;
    private List<PhoneItem> phoneItems;
    public OrderDetail(int phoneId, int quantity) {
        this.phoneId = phoneId;
        this.quantity = quantity;
    }
    // Getters and Setters

    public List<PhoneItem> getPhoneItems() {
        return phoneItems;
    }

    public void setPhoneItems(List<PhoneItem> phoneItems) {
        this.phoneItems = phoneItems;
    }

    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(int warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(int phoneId) {
        this.phoneId = phoneId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
