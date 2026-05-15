package com.BizBitsNow.BizBitsNow.Service;

import com.BizBitsNow.BizBitsNow.DTO.ProductDTO;
import com.BizBitsNow.BizBitsNow.Entity.Product;
import com.BizBitsNow.BizBitsNow.Entity.Seller;
import com.BizBitsNow.BizBitsNow.Repository.ProductRepo;
import com.BizBitsNow.BizBitsNow.Repository.SellerRepo;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final SellerRepo sellerRepo;

    public  ProductDTO addProduct(ProductDTO productDTO) {
        // 1. Logged-in Seller ki details nikalna
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Seller not found. Only registered sellers can add products."));

        long productCount = productRepo.countBySeller(seller);
        if (productCount >15) {
            throw new RuntimeException("Limit Exceeded: Aap 15 se zyada products add nahi kar sakte.");
        }
        // 2. DTO to Entity Mapping
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setImage_url(productDTO.getImageUrl());
        product.setCategory_id(productDTO.getCategoryId());
        product.setMrp(productDTO.getMrp());
        product.setUnit(productDTO.getUnit());
        product.setSellingPrice(productDTO.getSellingPrice());
        product.setMetadata(productDTO.getExtraDetails());
        product.setCategoryName(productDTO.getCategoryName());
        // product.setDeleted(false);
        product.set_available(true); // By default product available rahega
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
            // dto.setDeleted(savedProduct.isDeleted());
            dto.setDescription(savedProduct.getDescription());
            dto.setImageUrl(savedProduct.getImage_url());
            dto.setAvailable(savedProduct.is_available());

            // Entity se ID nikal kar DTO ki ID mein set karein
            dto.setCategoryId(savedProduct.getCategory_id());

            // Agar aapke paas CategoryRepo hai, toh name nikalne ka logic:
            // String name = categoryRepo.findById(product.getCategoryId()).getName();
            // dto.setCategoryName(name);

            if (savedProduct.getSeller() != null) {
                dto.setSellerId(savedProduct.getSeller().getId());
            }
            return dto;
    }

    public  ProductDTO updateProduct(ProductDTO productDTO, Long id) {

        Product existingProduct = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        String loggedInUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!existingProduct.getSeller().getEmail().equals(loggedInUserEmail)) {
            throw new RuntimeException("You are not authorized to update this product!");
        }

        if (productDTO.getName() != null) existingProduct.setName(productDTO.getName());
        if (productDTO.getPrice() != null) existingProduct.setPrice(productDTO.getPrice());
        if (productDTO.getDescription() != null) existingProduct.setDescription(productDTO.getDescription());
        if (productDTO.getImageUrl() != null) existingProduct.setImage_url(productDTO.getImageUrl());
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
        // product.set_available(false);
        // product.setDeleted(true);
        productRepo.delete(product);
    }

    public  List<ProductDTO> getAllProduct() {
        List<Product> products = productRepo.findAll();
       return products.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public  List<ProductDTO> getProductByName(String productName) {
        List<Product> products =
                productRepo.findByNameContainingIgnoreCase(productName);

        return products.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public  List<ProductDTO> getProductByCategory(String categoryName) {
        List<Product> category =
                productRepo.findByCategoryNameContainingIgnoreCase(categoryName);
        return category.stream()
                .map(this::mapToDTO)
                .toList();
    }
}
