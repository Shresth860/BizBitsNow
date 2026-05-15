package com.BizBitsNow.BizBitsNow.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerLoginRequestDTO {
    private String email;
    private String mobileNumber;
    private String subdomain;
}
