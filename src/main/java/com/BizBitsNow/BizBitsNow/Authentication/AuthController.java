package com.BizBitsNow.BizBitsNow.Authentication;

import com.BizBitsNow.BizBitsNow.DTO.*;
import com.BizBitsNow.BizBitsNow.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ---------------- SELLER ----------------

    @PostMapping("/registerSeller")
    public ResponseEntity<RegisterResponseDTO> registerSeller(
            @Valid @RequestBody RegisterRequestDTO dto
    ) {
        return ResponseEntity.ok(authService.registerSeller(dto));
    }

    @PostMapping("/loginSeller")
    public ResponseEntity<AuthResponseDTO> loginSeller(
            @Valid @RequestBody LoginRequestDTO dto
    ) {
        return ResponseEntity.ok(authService.loginSeller(dto));
    }

    // ---------------- CUSTOMER ----------------

    @PostMapping("/registerCustomer")
    public ResponseEntity<String> registerCustomer(
            @Valid @RequestBody CustomerRegisterRequestDTO dto
    ) {
        return ResponseEntity.ok(
                authService.registerCustomer(dto, dto.getSubdomain())
        );
    }

    @PostMapping("/loginCustomer")
    public ResponseEntity<LoginResponseDTO> loginCustomer(
            @Valid @RequestBody CustomerLoginRequestDTO dto
    ) {
        return ResponseEntity.ok(
                authService.loginCustomer(dto, dto.getSubdomain())
        );
    }

    // ---------------- OTP ----------------

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @Valid @RequestBody VerifyOtpDTO dto
    ) {
        try {
            return ResponseEntity.ok(
                    authService.verifyOtp(dto.getEmail(), dto.getOtp())
            );
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }
}