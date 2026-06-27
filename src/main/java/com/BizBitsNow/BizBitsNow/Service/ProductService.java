package com.BizBitsNow.BizBitsNow.Service;

import com.BizBitsNow.BizBitsNow.DTO.ProductDTO;
import com.BizBitsNow.BizBitsNow.Entity.Product;
import com.BizBitsNow.BizBitsNow.Entity.Seller;
import com.BizBitsNow.BizBitsNow.Repository.ProductRepo;
import com.BizBitsNow.BizBitsNow.Repository.SellerRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final SellerRepo sellerRepo;
    private final CloudinaryService cloudinaryService; // Inject Cloudinary Service

    // Controller se MultipartFile pass karein image ke liye
    public ProductDTO addProduct(ProductDTO productDTO, MultipartFile imageFile) {
        // 1. Logged-in Seller ki details nikalna
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Seller not found. Only registered sellers can add products."));

        long productCount = productRepo.countBySeller(seller);
        if (productCount > 15) {
            throw new RuntimeException("Limit Exceeded: Aap 15 se zyada products add nahi kar sakte.");
        }

        // Cloudinary par image upload karna agar file present hai
        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = cloudinaryService.uploadImage(imageFile);
        }

        // 2. DTO to Entity Mapping
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());

        // Agar Cloudinary se URL aaya hai toh wo set hoga, nahi toh DTO ka fallback
        product.setImage_url(imageUrl != null ? imageUrl : productDTO.getImageUrl());

        product.setCategory_id(productDTO.getCategoryId());
        product.setMrp(productDTO.getMrp());
        product.setUnit(productDTO.getUnit());
        product.setSellingPrice(productDTO.getSellingPrice());
        product.setMetadata(productDTO.getExtraDetails());
        product.setCategoryName(productDTO.getCategoryName());
        product.set_available(true);
        product.setCreated_at(LocalDateTime.now());

        // 3. Link Seller to Product
        product.setSeller(seller);

        // 4. Save to Database
        Product savedProduct = productRepo.save(product);

        // 5. Entity back to DTO for response
        return mapToDTO(savedProduct);
    }

    private ProductDTO mapToDTO(Product savedProduct) {
        ProductDTO dto = new ProductDTO();
        dto.setId(savedProduct.getProduct_id());
        dto.setName(savedProduct.getName());
        dto.setPrice(savedProduct.getPrice());
        dto.setMrp(savedProduct.getMrp());
        dto.setUnit(savedProduct.getUnit());
        dto.setSellingPrice(savedProduct.getSellingPrice());
        dto.setExtraDetails(savedProduct.getMetadata());
        dto.setCategoryName(savedProduct.getCategoryName());
        dto.setDescription(savedProduct.getDescription());
        dto.setImageUrl(savedProduct.getImage_url());
        dto.setAvailable(savedProduct.is_available());
        dto.setCategoryId(savedProduct.getCategory_id());

        if (savedProduct.getSeller() != null) {
            dto.setSellerId(savedProduct.getSeller().getId());
        }
        return dto;
    }

    public ProductDTO updateProduct(ProductDTO productDTO, Long id, MultipartFile imageFile) {
        Product existingProduct = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        String loggedInUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!existingProduct.getSeller().getEmail().equals(loggedInUserEmail)) {
            throw new RuntimeException("You are not authorized to update this product!");
        }

        if (productDTO.getName() != null) existingProduct.setName(productDTO.getName());
        if (productDTO.getPrice() != null) existingProduct.setPrice(productDTO.getPrice());
        if (productDTO.getDescription() != null) existingProduct.setDescription(productDTO.getDescription());

        // Update ke dauran agar nayi image upload ki gayi hai
        if (imageFile != null && !imageFile.isEmpty()) {
            String newImageUrl = cloudinaryService.uploadImage(imageFile);
            existingProduct.setImage_url(newImageUrl);
        } else if (productDTO.getImageUrl() != null) {
            existingProduct.setImage_url(productDTO.getImageUrl());
        }

        if (productDTO.getCategoryId() != null) existingProduct.setCategory_id(productDTO.getCategoryId());

        existingProduct.set_available(productDTO.isAvailable());

        Product updatedProduct = productRepo.save(existingProduct);
        return mapToDTO(updatedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!product.getSeller().getEmail().equals(loggedInEmail)) {
            throw new RuntimeException("Unauthorized delete attempt!");
        }
        productRepo.delete(product);
    }

    public List<ProductDTO> getAllProduct() {
        List<Product> products = productRepo.findAll();
        return products.stream().map(this::mapToDTO).toList();
    }

    public List<ProductDTO> getProductByName(String productName) {
        List<Product> products = productRepo.findByNameContainingIgnoreCase(productName);
        return products.stream().map(this::mapToDTO).toList();
    }

    public List<ProductDTO> getProductByCategory(String categoryName) {
        List<Product> category = productRepo.findByCategoryNameContainingIgnoreCase(categoryName);
        return category.stream().map(this::mapToDTO).toList();
    }
}