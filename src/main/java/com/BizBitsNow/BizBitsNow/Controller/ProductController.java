package com.BizBitsNow.BizBitsNow.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BizBitsNow.BizBitsNow.DTO.ProductDTO;
import com.BizBitsNow.BizBitsNow.Service.ProductService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    //add product
    @PreAuthorize("hasAnyRole('SELLER')")
    @PostMapping("/addProduct")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO){
        return ResponseEntity.ok(productService.addProduct(productDTO));
    }

    // update Product
    @PreAuthorize("hasAnyRole('SELLER')")
    @PutMapping("/updateProduct/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO , @PathVariable Long id){
        return ResponseEntity.ok(productService.updateProduct(productDTO,id));
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
