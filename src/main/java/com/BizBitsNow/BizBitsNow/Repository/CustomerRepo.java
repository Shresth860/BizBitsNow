package com.BizBitsNow.BizBitsNow.Repository;

import com.BizBitsNow.BizBitsNow.Entity.Customer;
import com.BizBitsNow.BizBitsNow.Entity.Seller;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepo  extends JpaRepository<Customer, Long> {
    Optional<Customer> findByMobileNumberAndSeller(String mobileNumber, Seller seller);

    Optional<Customer> findByEmail(String username);

    List<Customer> findBySellerId(Long sellerId);

    boolean existsByMobileNumberAndSeller(@NotBlank(message = "Mobile number is required") @Pattern(regexp = "^[0-9]{10}$", message = "Invalid mobile number") String mobileNumber, Seller seller);

    Optional<Customer> findByMobileNumber(String username);

//    String deleteByCustomerId(Long id);
}
