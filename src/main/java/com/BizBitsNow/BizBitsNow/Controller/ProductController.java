package com.BizBitsNow.BizBitsNow.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.BizBitsNow.BizBitsNow.DTO.ProductDTO;
import com.BizBitsNow.BizBitsNow.Service.ProductService;

import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    //add product
    @PreAuthorize("hasAnyRole('SELLER')")
    @PostMapping(value = "/add", consumes = {"multipart/form-data"})
    public ResponseEntity<ProductDTO> addProduct(
            @RequestPart("product") ProductDTO productDTO,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        return ResponseEntity.ok(productService.addProduct(productDTO, imageFile));
    }

    // update Product
    @PreAuthorize("hasAnyRole('SELLER')")
    @PutMapping(value = "/updateProduct/{productId}", consumes = {"multipart/form-data"})
    public ResponseEntity<ProductDTO> updateProduct(
            @RequestPart("product") ProductDTO productDTO,
            @RequestPart(value = "image", required = false) MultipartFile imageFile,
            @PathVariable("productId") Long productId) {

        return ResponseEntity.ok(productService.updateProduct(productDTO, productId, imageFile));
    }

    // delete Product
    @PreAuthorize("hasAnyRole('SELLER')")
    @DeleteMapping("/deleteProduct/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
       productService.deleteProduct(id);
       return ResponseEntity.ok("Product Deleted Successfully");
    }
    // get all product
    @PreAuthorize("hasAnyRole('CUSTOMER','SELLER')")
    @GetMapping("/allProducts")
    public ResponseEntity<List<ProductDTO>> getAllProduct(){
        return ResponseEntity.ok(productService.getAllProduct());
    }

    // Get Product By Name
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @GetMapping("/product/{productName}")
    public ResponseEntity<List<ProductDTO>> getProductByName(
            @PathVariable String productName
    ) {

        return ResponseEntity.ok(
                productService.getProductByName(productName)
        );
    }

    // search by Category
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<ProductDTO>> getProductByCategory(@PathVariable String categoryName){
        return ResponseEntity.ok(productService.getProductByCategory(categoryName));
    }
}
