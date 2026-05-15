package com.BizBitsNow.BizBitsNow.Controller;

import com.BizBitsNow.BizBitsNow.DTO.CustomerDTO;
import com.BizBitsNow.BizBitsNow.Entity.Customer;
import com.BizBitsNow.BizBitsNow.Service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    //delete customer
    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id){
        customerService.deleteCustomer(id);
        return ResponseEntity.ok("Customer deleted Sucessfully:");
    }

    // NOTE : Add Customer api banane ki jarurat nhi hai jab user register karega pheli baar to automatically wo add ho jayega database mein


    // Update Customer
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@RequestBody CustomerDTO customerDTO , @PathVariable Long id){
        return ResponseEntity.ok(customerService.updateCustomer(customerDTO,id));
    }

    // Get Customer
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/me")
    public ResponseEntity<CustomerDTO> getMyProfile(){
        return ResponseEntity.ok(customerService.getMyProfile());
    }

    // Get All Customer ( Admin / Seller side)
    @PreAuthorize("hasAnyRole('ADMIN','SELLER')")
    @GetMapping()
    public ResponseEntity<List<CustomerDTO>> getAllCustomer(){
        return ResponseEntity.ok(customerService.getAllCustomer());
    }

    // Get Customer By Seller id
    @PreAuthorize("hasAnyRole('ADMIN','SELLER')")
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<CustomerDTO>> getAllCustomerBySellerId(@PathVariable Long sellerId){
        return ResponseEntity.ok(customerService.getAllCustomerBySellerId(sellerId));
    }
}
