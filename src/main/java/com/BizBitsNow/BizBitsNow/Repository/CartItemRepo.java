package com.BizBitsNow.BizBitsNow.Repository;

import com.BizBitsNow.BizBitsNow.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem,Long> {
    void deleteAllByCartId(Long id);
}
