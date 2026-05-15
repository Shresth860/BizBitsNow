package com.BizBitsNow.BizBitsNow.Service;

import com.BizBitsNow.BizBitsNow.DTO.CartDTO;
import com.BizBitsNow.BizBitsNow.DTO.CartItemDTO;
import com.BizBitsNow.BizBitsNow.Entity.Cart;
import com.BizBitsNow.BizBitsNow.Entity.CartItem;
import com.BizBitsNow.BizBitsNow.Entity.Customer;
import com.BizBitsNow.BizBitsNow.Entity.Product;
import com.BizBitsNow.BizBitsNow.Repository.CartItemRepo;
import com.BizBitsNow.BizBitsNow.Repository.CartRepo;
import com.BizBitsNow.BizBitsNow.Repository.CustomerRepo;
import com.BizBitsNow.BizBitsNow.Repository.ProductRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartService {
    private final CartRepo cartRepo;
    private final CustomerRepo customerRepo;
    private final CartItemRepo cartItemRepo;
    private final ProductRepo productRepo;

    @Transactional
    public  CartDTO addItemToCart(Long productId, Integer quantity) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepo.findByMobileNumber(userName)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // 2. Customer ka Cart dhundna (Agar nahi hai toh naya banana)
        Cart cart = cartRepo.findByCustomer(customer)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCustomer(customer);
                    return cartRepo.save(newCart);
                });

        // 3. Product fetch karna
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 4. Check karna ki product pehle se cart mein hai ya nahi
        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getProduct_id().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            // Agar hai, toh purani quantity mein nayi quantity add kar do
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepo.save(item);
        } else {
            // Agar nahi hai, toh naya CartItem banao
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cartItemRepo.save(newItem);
            cart.getCartItems().add(newItem); // List update for DTO mapping
        }

        return mapToDTO(cart);
    }

    public  CartDTO getMyCart() {
        // Logged-in user ka cart fetch karna
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepo.findByMobileNumber(userName)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Cart cart = cartRepo.findByCustomer(customer)
                .orElseThrow(() -> new RuntimeException("Cart is empty or not initialized"));

        return mapToDTO(cart);
    }

    @Transactional
    public  CartDTO updateItemQuantity(Long cartItemId, Integer quantity) {
        CartItem item = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart Item not found"));

        // Security Check: Kya ye item usi logged-in user ka hai?
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!item.getCart().getCustomer().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized access to this cart item");
        }

        if (quantity <= 0) {
            cartItemRepo.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepo.save(item);
        }

        return getMyCart();
    }
    @Transactional
    public  CartDTO removeItemFromCart(Long cartItemId) {
        CartItem item = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart Item not found"));

        // Security Check
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!item.getCart().getCustomer().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized access");
        }

        cartItemRepo.delete(item);

        // Repo delete ke baad, current cart fetch karke return karein
        return getMyCart();
    }
    @Transactional
    public void clearCart() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Cart cart = cartRepo.findByCustomer(customer)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Saare items delete kar dein par Cart entity rehne dein
        cartItemRepo.deleteAllByCartId(cart.getId());
        cart.getCartItems().clear();
        cartRepo.save(cart);
    }
    private CartDTO mapToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setCartId(cart.getId());

        int totalAmount = 0;
        List<CartItemDTO> itemDTOList = new ArrayList<>();

        for (CartItem item : cart.getCartItems()) {
            CartItemDTO iDto = new CartItemDTO();
            iDto.setCartItemId(item.getId());
            iDto.setProductId(item.getProduct().getProduct_id());
            iDto.setProductName(item.getProduct().getName());
            iDto.setPrice(item.getProduct().getPrice());
            iDto.setQuantity(item.getQuantity());
            iDto.setImageUrl(item.getProduct().getImage_url());

            totalAmount += (item.getProduct().getPrice() * item.getQuantity());
            itemDTOList.add(iDto);
        }

        dto.setItems(itemDTOList);
        dto.setTotalAmount(totalAmount);
        return dto;
    }
}
