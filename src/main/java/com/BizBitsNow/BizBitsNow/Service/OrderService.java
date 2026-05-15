package com.BizBitsNow.BizBitsNow.Service;

import com.BizBitsNow.BizBitsNow.DTO.AddressDTO;
import com.BizBitsNow.BizBitsNow.DTO.OrderDTO;
import com.BizBitsNow.BizBitsNow.DTO.OrderItemResponseDTO;
import com.BizBitsNow.BizBitsNow.DTO.OrderRequestDTO;
import com.BizBitsNow.BizBitsNow.Entity.*;
import com.BizBitsNow.BizBitsNow.Enum.DeliveryStatus;
import com.BizBitsNow.BizBitsNow.Enum.PaymentStatus;
import com.BizBitsNow.BizBitsNow.Repository.AddressRepo;
import com.BizBitsNow.BizBitsNow.Repository.CustomerRepo;
import com.BizBitsNow.BizBitsNow.Repository.OrderRepo;
import com.BizBitsNow.BizBitsNow.Repository.ProductRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepo orderRepo;
    private final CustomerRepo customerRepo;
    private final AddressRepo addressRepo;
    private final ProductRepo productRepo;

    @Transactional // <--- Isse Rollback support milega agar koi error aayi
    public OrderDTO placeOrder(OrderRequestDTO dto) {
        // Get logged-in Customer
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepo.findByMobileNumber(userName)
                .orElseThrow(() -> new RuntimeException("Customer Not found"));

        Address address = addressRepo.findById(dto.getAddressId())
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getCustomer().getId().equals(customer.getId())) {
            throw new RuntimeException("This address does not belong to the logged-in user");
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setAddress(address);
        order.setDeliveryStatus(DeliveryStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.COD);
        order.setPlacedAt(LocalDateTime.now());

        int totalAmount = 0;
        List<OrderItem> orderItems = new ArrayList<>();
        Seller seller = null;

        for (var itemReq : dto.getItems()) {
            Product product = productRepo.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found ID: " + itemReq.getProductId()));

            // Total calculate karte waqt safety check
            totalAmount += product.getPrice() * itemReq.getQuantity();

            // Sabhi items ek hi seller ke hain, ye assume kar rahe hain
            if (seller == null) seller = product.getSeller();

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);

            // FIXED: itemReq se quantity uthayein, orderItem se nahi
            orderItem.setQuantity(itemReq.getQuantity());

            orderItem.setPriceAtOrder(product.getPrice());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }

        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);
        order.setSeller(seller);

        // CascadeType.ALL ki wajah se orderItems bhi save ho jayenge
        Order savedOrder = orderRepo.save(order);

        return mapToDTO(savedOrder);
    }

    // --- MAPPING LOGIC ---
    private OrderDTO mapToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setDeliveryStatus(order.getDeliveryStatus().name());
        dto.setPaymentStatus(order.getPaymentStatus().name());
        dto.setPlacedAt(order.getPlacedAt());
        dto.setAddress(mapAddressToDTO(order.getAddress()));

        dto.setDeliveredAt(order.getDeliveredAt());


        // Address mapping (Optional: Agar AddressDTO banaya hai toh use karein)
        // dto.setAddress(mapAddressToDTO(order.getAddress()));

        List<OrderItemResponseDTO> itemDTOs = order.getOrderItems().stream().map(item -> {
            OrderItemResponseDTO iDto = new OrderItemResponseDTO();
            iDto.setProductId(item.getProduct().getProduct_id());
            iDto.setProductName(item.getProduct().getName());
            iDto.setImageUrl(item.getProduct().getImage_url());
            iDto.setQuantity(item.getQuantity());
            iDto.setPriceAtOrder(item.getPriceAtOrder());
            return iDto;
        }).collect(Collectors.toList());

        dto.setItems(itemDTOs);
        return dto;
    }

    private AddressDTO mapAddressToDTO(Address address) {
        if (address == null) return null;

        AddressDTO dto = new AddressDTO();

        dto.setId(address.getId());
        dto.setFullName(address.getFullName());
        dto.setMobileNumber(address.getMobileNumber());
        dto.setFlatHouseNumber(address.getFlatHouseNumber());
        dto.setAreaLocality(address.getAreaLocality());
        dto.setLandmark(address.getLandmark());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setPincode(address.getPincode());
        dto.setAddressType(address.getAddressType());
        dto.setDefault(address.isDefault());

        return dto;
    }

    public  OrderDTO getOrderDetails(Long id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return mapToDTO(order);
    }

    public  List<OrderDTO> getCustomerOrders() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return orderRepo.findByCustomerEmailOrderByPlacedAtDesc(email)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public  OrderDTO updateOrderStatus(Long id, String status) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        //Security: Check if the logged-in user is the owner (Seller) of this order
        String sellerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!order.getSeller().getEmail().equals(sellerEmail)) {
            throw new RuntimeException("Unauthorized: Only the seller can update this order status");
        }

        // Sirf DeliveryStatus update hoga
        order.setDeliveryStatus(DeliveryStatus.valueOf(status));

        // Agar status DELIVERED hai, toh delivery time set karein
        if (status.equals("DELIVERED")) {
            order.setDeliveredAt(LocalDateTime.now());
        }

        Order updatedOrder = orderRepo.save(order);
        return mapToDTO(updatedOrder);
    }

    @Transactional
    public void cancelOrder(Long id) {
            Order order = orderRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            // Security: Kya ye order usi customer ka hai?
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            if (!order.getCustomer().getEmail().equals(email)) {
                throw new RuntimeException("Unauthorized!");
            }

            // Business Logic: Kya order cancel ho sakta hai?
            // Agar order SHIPPED ya DELIVERED ho chuka hai, toh cancel nahi hona chahiye
            if (order.getDeliveryStatus() != DeliveryStatus.PENDING) {
                throw new RuntimeException("Order cannot be cancelled now. It's already " + order.getDeliveryStatus());
            }

            order.setDeliveryStatus(DeliveryStatus.CANCELLED);
            orderRepo.save(order);
    }
}

