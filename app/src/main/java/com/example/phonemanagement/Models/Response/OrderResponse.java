package com.example.phonemanagement.Models.Response;

import com.google.gson.annotations.SerializedName;

public class OrderResponse {
    @SerializedName("orderId")
    private int orderId;

    @SerializedName("userId")
    private int userId;

    @SerializedName("payment")
    private Payment payment;

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

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
    // Getters and setters

    public static class Payment {
        @SerializedName("paymentId")
        private int paymentId;

        @SerializedName("orderId")
        private int orderId;

        @SerializedName("paymentDate")
        private String paymentDate;

        @SerializedName("paymentMethod")
        private String paymentMethod;

        @SerializedName("amountPaid")
        private double amountPaid;

        // Getters and setters

        public int getPaymentId() {
            return paymentId;
        }

        public void setPaymentId(int paymentId) {
            this.paymentId = paymentId;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public String getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(String paymentDate) {
            this.paymentDate = paymentDate;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public double getAmountPaid() {
            return amountPaid;
        }

        public void setAmountPaid(double amountPaid) {
            this.amountPaid = amountPaid;
        }
    }
}