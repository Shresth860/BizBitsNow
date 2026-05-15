package com.BizBitsNow.BizBitsNow.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CustomerDTO {
    private Long id;
    private String mobileNumber;
    private String email;
    private String fullName;
    private String role;

    // Relationship handle karne ke liye sirf ID ka use karein
    private Long sellerId;

    public CustomerDTO() {

    }
}
