package com.BizBitsNow.BizBitsNow.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SoftDelete(columnName = "is_deleted")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_id;

    @ManyToOne
    @JoinColumn(name="seller_id",nullable = false)
    private Seller seller;

    private Integer mrp;
    private Integer sellingPrice;

    private String unit;
    private String quantityInfo;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> metadata;

    private Long category_id;

    private String categoryName;

    private String name;

    private Integer price;

    private String image_url;

    private String description;


    private boolean is_available=true;

    private LocalDateTime created_at;
}
