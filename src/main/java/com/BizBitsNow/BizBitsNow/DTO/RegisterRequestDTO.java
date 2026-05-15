package com.BizBitsNow.BizBitsNow.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterRequestDTO {

    @Column(unique = true, nullable = false)
    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email cannot be empty")
    private String email;


    @NotBlank(message = "PIN is required")
    @Size(min = 4, max = 4, message = "PIN must be exactly 4 digits")
    @Pattern(regexp = "^[0-9]+$", message = "PIN must contain only numbers")
    private String pin; // Encrypted permanent 4-digit PIN

    @NotBlank(message = "ShopName is required")
    private String shopName;

    private String logoUrl;

    private String fullName;

    private String brandColor;

    private boolean  isVerified;

    private String bannerUrl;

//    @NotBlank(message = "subdomain is required")
    private String subdomain; // Example: "ramesh-tiffin"

}
