package com.BizBitsNow.BizBitsNow.DTO;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponseDTO {
    private String message;      // e.g., "Registration Successful!"
    private Long sellerId;       // Future API calls ke liye
    private String email;
    private String shopName;
    private String subdomain;    // e.g., "ramesh-tiffin" (Very important for the seller to know)
    private String token;
}
