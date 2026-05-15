package com.BizBitsNow.BizBitsNow.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    private String token;           // JWT Token (Bearer)
    private String role;            // ROLE_SELLER ya ROLE_CUSTOMER
    private String shopName;
    private String fullName;
    private String email;   // Login ID

    // Sirf Seller ke liye: Redirect karne ke liye zaroori hai
    private String subdomain;

    private String message;         // User-friendly feedback (e.g. "Welcome back!")

    @Builder.Default
    private String tokenType = "Bearer";

}
