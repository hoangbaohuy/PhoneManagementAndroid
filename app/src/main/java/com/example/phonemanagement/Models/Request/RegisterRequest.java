package com.example.phonemanagement.Models.Request;

public class RegisterRequest {
    private String userName;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String email;
    private String passwordHash;

    public RegisterRequest(String userName, String fullName, String address, String phoneNumber, String email, String passwordHash) {
        this.userName = userName;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.passwordHash = passwordHash;
    }
}
