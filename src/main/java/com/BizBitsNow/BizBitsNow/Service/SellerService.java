package com.BizBitsNow.BizBitsNow.Service;

import com.BizBitsNow.BizBitsNow.DTO.SellerDTO;
import com.BizBitsNow.BizBitsNow.Entity.Customer;
import com.BizBitsNow.BizBitsNow.Entity.Seller;
import com.BizBitsNow.BizBitsNow.Repository.SellerRepo;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SellerService {
    private final SellerRepo sellerRepo;

    public void deleteSeller(Long id) {
        sellerRepo.deleteById(id);
    }

    public  SellerDTO updateSeller(Long id, SellerDTO sellerDTO) {
        // 1. Phele check karo Seller exist karta hai ya nhi
        Seller seller = sellerRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Seller not Found"));

        if(sellerDTO.getShopName()!=null){
            seller.setShopName(sellerDTO.getShopName());
        }
        if(sellerDTO.getEmail()!=null){
            seller.setEmail(sellerDTO.getEmail());
        }
        if(sellerDTO.getBannerUrl()!=null){
            seller.setBannerUrl(sellerDTO.getBannerUrl());
        }
        if(sellerDTO.getSubdomain()!=null){
            seller.setSubdomain(sellerDTO.getSubdomain());
        }
        if(sellerDTO.getLogoUrl()!=null){
            seller.setLogoUrl(sellerDTO.getLogoUrl());
        }
        if(sellerDTO.getBrandColor()!=null){
            seller.setBrandColor(sellerDTO.getBrandColor());
        }
        return mapToDTO(sellerRepo.save(seller));
    }
    private SellerDTO mapToDTO(Seller seller){
        return new SellerDTO(
                seller.getId(),
                seller.getEmail(),
                seller.getShopName(),
                seller.getLogoUrl(),
                seller.isVerified(),
                seller.getBrandColor(),
                seller.getBannerUrl(),
                seller.getSubdomain()
        );
    }

    public SellerDTO getMyInfo() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        System.out.println("JWT Email: " + email);

        Seller seller = sellerRepo.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Profile not found"));

        return mapToDTO(seller);
    }

    public  List<SellerDTO> getAllSeller() {
        return sellerRepo.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }
}
