package com.BizBitsNow.BizBitsNow.Repository;

import com.BizBitsNow.BizBitsNow.Entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepo extends JpaRepository<OtpVerification , String> {
    Optional<OtpVerification> findByEmail(String s);
}
