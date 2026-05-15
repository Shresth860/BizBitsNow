package com.BizBitsNow.BizBitsNow.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ProductDTO {
    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;
    private String description;
    private boolean isAvailable;
    private boolean isDeleted;
    private Integer mrp;
    private Integer sellingPrice;
    private String unit;
    private Map<String, Object> extraDetails;
    private Long sellerId;
    private Long categoryId;
    private String categoryName;
}
