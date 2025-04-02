package com.uni.bankproject.dto;

import lombok.Data;

@Data
// This data is temporary until more fields are added
public class RegisterRequest {
    private String userID;

    private String userName;

    private String email;

    private String phoneNumber;

    private String address;

    private String userKey;
}
