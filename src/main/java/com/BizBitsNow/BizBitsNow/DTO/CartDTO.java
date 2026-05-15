package com.BizBitsNow.BizBitsNow.DTO;


import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class CartDTO {
    private Long cartId;
    private List<CartItemDTO> items;
    private Integer totalAmount;

}
