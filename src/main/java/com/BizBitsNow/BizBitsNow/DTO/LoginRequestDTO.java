package com.BizBitsNow.BizBitsNow.DTO;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;           // Seller ke liye
    private String pin;            // Seller ke liye
    private String mobileNumber;  // Customer ke liye


}
