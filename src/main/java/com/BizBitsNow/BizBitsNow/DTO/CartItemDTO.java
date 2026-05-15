package com.BizBitsNow.BizBitsNow.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
        private Long cartItemId;
        private Long productId;
        private String productName;
        private Integer price;
        private Integer quantity;
        private String imageUrl;
}
