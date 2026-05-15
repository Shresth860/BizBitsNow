package com.BizBitsNow.BizBitsNow.Repository;

import com.BizBitsNow.BizBitsNow.Entity.Cart;
import com.BizBitsNow.BizBitsNow.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart,Long> {
    Optional<Cart> findByCustomer(Customer customer);
}
