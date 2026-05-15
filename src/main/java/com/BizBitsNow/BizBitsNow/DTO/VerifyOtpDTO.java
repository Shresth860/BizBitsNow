package com.BizBitsNow.BizBitsNow.DTO;

import lombok.Data;

@Data
public class VerifyOtpDTO {
    private String email;
    private String otp;
}
