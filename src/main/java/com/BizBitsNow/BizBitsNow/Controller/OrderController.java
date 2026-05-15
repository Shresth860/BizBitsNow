package com.BizBitsNow.BizBitsNow.Controller;

import com.BizBitsNow.BizBitsNow.DTO.OrderDTO;
import com.BizBitsNow.BizBitsNow.DTO.OrderRequestDTO;
import com.BizBitsNow.BizBitsNow.Service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // customer naya order place karega
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @PostMapping("/placeOrder")
    public ResponseEntity<OrderDTO> placeOrder(@RequestBody OrderRequestDTO dto){
        return ResponseEntity.ok(orderService.placeOrder(dto));
    }

    // Customer apne saare purane orders dekhega
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderDTO>> getMyOrders() {
        return ResponseEntity.ok(orderService.getCustomerOrders());
    }

    // Ek specific order ki detail dekhna ID se
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderDetails(id));
    }

    // Seller ke liye: Status badalna (SHIPPED, DELIVERED etc.)
    @PreAuthorize("hasAnyRole('SELLER')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    // Customer ke liye: Order cancel karna
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @PostMapping("/{id}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok("Order Cancelled Successfully");
    }

}
