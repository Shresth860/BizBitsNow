package com.BizBitsNow.BizBitsNow.Service;

import com.BizBitsNow.BizBitsNow.DTO.SellerDTO;
import com.BizBitsNow.BizBitsNow.Entity.Seller;
import com.BizBitsNow.BizBitsNow.Repository.SellerRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile; // Imported

import java.util.List;

@Service
@AllArgsConstructor
public class SellerService {
    private final SellerRepo sellerRepo;
    private final CloudinaryService cloudinaryService; // Inject Cloudinary Service

    public void deleteSeller(Long id) {
        sellerRepo.deleteById(id);
    }

    // Method signature mein logoFile aur bannerFile add kiya gaya hai
    public SellerDTO updateSeller(Long id, SellerDTO sellerDTO, MultipartFile logoFile, MultipartFile bannerFile) {
        // 1. Phele check karo Seller exist karta hai ya nhi
        Seller seller = sellerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Seller not Found"));

        if (sellerDTO.getShopName() != null) {
            seller.setShopName(sellerDTO.getShopName());
        }
        if (sellerDTO.getEmail() != null) {
            seller.setEmail(sellerDTO.getEmail());
        }

        // --- CLOUDINARY LOGIC FOR BANNER ---
        if (bannerFile != null && !bannerFile.isEmpty()) {
            String bannerUrl = cloudinaryService.uploadImage(bannerFile);
            seller.setBannerUrl(bannerUrl);
        } else if (sellerDTO.getBannerUrl() != null) {
            seller.setBannerUrl(sellerDTO.getBannerUrl());
        }

        if (sellerDTO.getSubdomain() != null) {
            seller.setSubdomain(sellerDTO.getSubdomain());
        }

        // --- CLOUDINARY LOGIC FOR LOGO ---
        if (logoFile != null && !logoFile.isEmpty()) {
            String logoUrl = cloudinaryService.uploadImage(logoFile);
            seller.setLogoUrl(logoUrl);
        } else if (sellerDTO.getLogoUrl() != null) {
            seller.setLogoUrl(sellerDTO.getLogoUrl());
        }

        if (sellerDTO.getBrandColor() != null) {
            seller.setBrandColor(sellerDTO.getBrandColor());
        }

        return mapToDTO(sellerRepo.save(seller));
    }

    private SellerDTO mapToDTO(Seller seller) {
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        System.out.println("JWT Email: " + email);

        Seller seller = sellerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return mapToDTO(seller);
    }

    public List<SellerDTO> getAllSeller() {
        return sellerRepo.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }
}