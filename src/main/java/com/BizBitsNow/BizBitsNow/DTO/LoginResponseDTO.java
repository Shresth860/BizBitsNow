package com.BizBitsNow.BizBitsNow.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponseDTO {
    private String token;           // JWT Token (Bearer)
    private String role;
    private String email;
    private String message;
    @Builder.Default
    private String tokenType = "Bearer";
}
