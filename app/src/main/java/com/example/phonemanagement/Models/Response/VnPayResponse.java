package com.example.phonemanagement.Models.Response;

import com.google.gson.annotations.SerializedName;

public class VnPayResponse {
    @SerializedName("paymentUrl")
    private String paymentUrl;

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }
}
