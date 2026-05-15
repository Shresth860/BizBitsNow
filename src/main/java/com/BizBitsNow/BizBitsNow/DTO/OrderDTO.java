package com.BizBitsNow.BizBitsNow.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class OrderDTO {
    private Long orderId;
    private Integer totalAmount;

    private String deliveryStatus;
    private String paymentStatus; // paid , pending

    private LocalDateTime placedAt;
    private LocalDateTime deliveredAt;


    private AddressDTO address;

    private List<OrderItemResponseDTO> items;

}
