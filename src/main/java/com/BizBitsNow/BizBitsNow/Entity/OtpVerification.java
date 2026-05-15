package com.BizBitsNow.BizBitsNow.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
public class OtpVerification {

    @Id
    private String email;

    private String otp;

    private LocalDateTime expiryTime;

    public OtpVerification() {

    }
}
