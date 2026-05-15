package com.BizBitsNow.BizBitsNow.Repository;

import com.BizBitsNow.BizBitsNow.Entity.Product;
import com.BizBitsNow.BizBitsNow.Entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {
    List<Product> findByNameContainingIgnoreCase(String productName);

    List<Product> findByCategoryNameContainingIgnoreCase(String categoryName);

    long countBySeller(Seller seller);
}
