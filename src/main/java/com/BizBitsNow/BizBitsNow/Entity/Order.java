package com.BizBitsNow.BizBitsNow.Entity;

import com.BizBitsNow.BizBitsNow.Enum.DeliveryStatus;
import com.BizBitsNow.BizBitsNow.Enum.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    // OrderItems ki list (Jo products order hue hain)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    private Integer totalAmount;

     @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

     @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDateTime placedAt = LocalDateTime.now();
    private LocalDateTime deliveredAt;
}