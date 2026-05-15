package com.BizBitsNow.BizBitsNow.Entity;

import com.BizBitsNow.BizBitsNow.Enum.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @Column(nullable = false)
    private String pin; // Encrypted permanent 4-digit PIN

    @Column(nullable = false)
    private String shopName;

    private String logoUrl;

    @Column(name = "is_verified", nullable = false)
    private boolean verified = false;

    private String brandColor;

    private String bannerUrl;




    @Column(unique = true, nullable = false)
    private String subdomain; // Example: "ramesh-tiffin"

    @Enumerated(EnumType.STRING)
    private Role role = Role.SELLER; // Constant role

    private LocalDateTime createdAt = LocalDateTime.now();
}
