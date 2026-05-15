package com.BizBitsNow.BizBitsNow.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SellerDTO {
    private Long id;
    private String email;
    private String shopName;
    private String logoUrl;
    boolean isVerified=false;
    private String brandColor;
    private String bannerUrl;
    private String subdomain;
}
