package com.BizBitsNow.BizBitsNow.Repository;

import com.BizBitsNow.BizBitsNow.Entity.Seller;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SellerRepo extends JpaRepository<Seller , Long> {
    Optional<Seller> findByEmail(String email);
    Optional<Seller> findBySubdomain(String subdomain);
    boolean existsByEmail(String email);

    boolean existsBySubdomain(String finalSubdomain);

//    boolean existsByEmail(@Email(message = "Please provide a valid email address") @NotBlank(message = "Email cannot be empty") String email);
}
