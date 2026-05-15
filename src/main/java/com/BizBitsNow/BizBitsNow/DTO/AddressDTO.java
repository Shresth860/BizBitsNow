package com.BizBitsNow.BizBitsNow.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddressDTO {
    private Long id;
    private String fullName;
    private String mobileNumber;
    private String flatHouseNumber;
    private String areaLocality;
    private String landmark;
    private String city;
    private String state;
    private String pincode;
    private String addressType;
    private boolean isDefault;
}
