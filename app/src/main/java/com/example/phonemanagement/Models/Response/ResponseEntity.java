package com.example.phonemanagement.Models.Response;

public class ResponseEntity {
    private String message;
    private boolean success;
    private String token; // or whatever the actual name is in the response
    // Other fields...

    public String getToken() {
        return token;
    }
}
