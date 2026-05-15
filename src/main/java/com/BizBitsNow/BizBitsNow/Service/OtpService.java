package com.BizBitsNow.BizBitsNow.Service;

import com.BizBitsNow.BizBitsNow.Entity.OtpVerification;
import com.BizBitsNow.BizBitsNow.Repository.OtpRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final OtpRepo otpRepository;
    private final EmailService emailService;

    public void generateAndSendOtp(String email){

        // 6 digit otp generate karega
        String otp = String.format("%06d",new Random().nextInt(1000000));

        //DB mein save ya update karega
        OtpVerification otpEntry = new OtpVerification();
        otpEntry.setEmail(email);
        otpEntry.setOtp(otp);
        otpEntry.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        otpRepository.save(otpEntry);

        // Email bhejna
        emailService.sendOtpEmail(email, otp);
    }
    public boolean validateOtp(String email, String otp) {
        // Repository se data fetch karein
        Optional<OtpVerification> otpData = otpRepository.findByEmail(email);

        if (otpData.isPresent()) {
            OtpVerification data = otpData.get();

            // Manual check karein (taaki 'Iterator' wali confusion na ho)
            boolean isMatch = data.getOtp().equals(otp);
            boolean isNotExpired = data.getExpiryTime().isAfter(LocalDateTime.now());

            return isMatch && isNotExpired;
        }
        return false;
    }

    public void deleteOtp(String email) {
        otpRepository.deleteById(email);
    }

}
