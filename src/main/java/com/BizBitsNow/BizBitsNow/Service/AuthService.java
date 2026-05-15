package com.BizBitsNow.BizBitsNow.Service;

import com.BizBitsNow.BizBitsNow.DTO.*;
import com.BizBitsNow.BizBitsNow.Entity.Customer;
import com.BizBitsNow.BizBitsNow.Entity.Seller;
import com.BizBitsNow.BizBitsNow.Repository.CustomerRepo;
import com.BizBitsNow.BizBitsNow.Repository.SellerRepo;
import com.BizBitsNow.BizBitsNow.config.JwtConfig;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final SellerRepo sellerRepo;
    private final CustomerRepo customerRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;
    private final OtpService otpService;

    // ---------------- SELLER ----------------

    @Transactional
    public RegisterResponseDTO registerSeller(@Valid RegisterRequestDTO dto) {

        if (sellerRepo.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }

        Seller seller = new Seller();
        seller.setEmail(dto.getEmail());
        seller.setShopName(dto.getShopName());
        seller.setLogoUrl(dto.getLogoUrl());
        seller.setBannerUrl(dto.getBannerUrl());
        seller.setBrandColor(dto.getBrandColor());
        seller.setVerified(false);
        seller.setBrandColor(dto.getBrandColor());
        seller.setPin(passwordEncoder.encode(dto.getPin()));

        String baseSubdomain = dto.getShopName().toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");

        String finalSubdomain = baseSubdomain;
        int count = 1;

        while (sellerRepo.existsBySubdomain(finalSubdomain)) {
            finalSubdomain = baseSubdomain + "-" + count;
            count++;
        }

        seller.setSubdomain(finalSubdomain);

        Seller saved = sellerRepo.save(seller);

        otpService.generateAndSendOtp(saved.getEmail());

        return RegisterResponseDTO.builder()
                .sellerId(saved.getId())
                .shopName(saved.getShopName())
                .email(saved.getEmail())
                .subdomain(saved.getSubdomain())
                .message("Seller registered. OTP sent.")
                .build();
    }

    public AuthResponseDTO loginSeller(@Valid LoginRequestDTO dto) {

        Seller seller = sellerRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        if (!seller.isVerified()) {
            throw new RuntimeException("Seller not verified");
        }

        if (!passwordEncoder.matches(dto.getPin(), seller.getPin())) {
            throw new RuntimeException("Invalid PIN");
        }

        String token = jwtConfig.generateToken(seller.getEmail(), seller.getRole());

        return AuthResponseDTO.builder()
                .token(token)
                .email(seller.getEmail())
                .role(String.valueOf(seller.getRole()))
                .shopName(seller.getShopName())
                .subdomain(seller.getSubdomain())
                .message("Login successful")
                .build();
    }

    // ---------------- CUSTOMER ----------------

    @Transactional
    public String registerCustomer(CustomerRegisterRequestDTO dto, String subdomain) {

        if (dto.getSubdomain() == null || dto.getSubdomain().isBlank()) {
            throw new RuntimeException("Subdomain is required");
        }

        Seller seller = sellerRepo.findBySubdomain(dto.getSubdomain())
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        if (customerRepo.existsByMobileNumberAndSeller(dto.getMobileNumber(), seller)) {
            throw new RuntimeException("Customer already exists");
        }

        Customer customer = new Customer();
        customer.setMobileNumber(dto.getMobileNumber());
        customer.setFullName(dto.getFullName());
        customer.setEmail(dto.getEmail());
        customer.setSeller(seller);

        customerRepo.save(customer);

        return "Customer registered successfully for " + seller.getShopName();
    }
    public LoginResponseDTO loginCustomer(CustomerLoginRequestDTO dto, String subdomain) {

        Seller seller = sellerRepo.findBySubdomain(dto.getSubdomain())
                .orElseThrow(() -> new RuntimeException("Invalid shop"));

        Customer customer = customerRepo.findByMobileNumberAndSeller(
                dto.getMobileNumber(),
                seller
        ).orElseThrow(() -> new RuntimeException("Customer not found"));

        String token = jwtConfig.generateToken(
                customer.getMobileNumber(),
                customer.getRole()

        );

        return LoginResponseDTO.builder()
                .token(token)
                .role(customer.getRole().name())
                .email(customer.getEmail())
                .message("Login successful")
                .build();
    }

    // ---------------- OTP ----------------

    @Transactional
    public AuthResponseDTO verifyOtp(String email, String otp) {

        if (!otpService.validateOtp(email, otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        Seller seller = sellerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        seller.setVerified(true);
        sellerRepo.save(seller);

        otpService.deleteOtp(email);

        String token = jwtConfig.generateToken(seller.getEmail(), seller.getRole());

        return AuthResponseDTO.builder()
                .token(token)
                .email(seller.getEmail())
                .role(String.valueOf(seller.getRole()))
                .subdomain(seller.getSubdomain())
                .message("OTP verified")
                .build();
    }
}