package com.BizBitsNow.BizBitsNow.Controller;

import com.BizBitsNow.BizBitsNow.DTO.CartDTO;
import com.BizBitsNow.BizBitsNow.Service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@AllArgsConstructor
public class CartController {
    private final CartService cartService;
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @PostMapping("/add")
    public ResponseEntity<CartDTO> addToCart(
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(cartService.addItemToCart(productId, quantity));
    }

    // 2. Get Logged-in User's Cart
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @GetMapping
    public ResponseEntity<CartDTO> getCart() {
        return ResponseEntity.ok(cartService.getMyCart());
    }

    // 3. Update Item Quantity (Directly from Cart Page)
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @PutMapping("/update-item/{cartItemId}")
    public ResponseEntity<CartDTO> updateItemQuantity(
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(cartService.updateItemQuantity(cartItemId, quantity));
    }

    // 4. Remove a specific item from
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<CartDTO> removeItem(@PathVariable Long cartItemId) {
        return ResponseEntity.ok(cartService.removeItemFromCart(cartItemId));
    }

    // 5. Clear entire Cart
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart() {
        cartService.clearCart();
        return ResponseEntity.ok("Cart cleared successfully");
    }
}
