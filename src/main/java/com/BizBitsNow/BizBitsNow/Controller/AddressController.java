package com.BizBitsNow.BizBitsNow.Controller;

import com.BizBitsNow.BizBitsNow.DTO.AddressDTO;
import com.BizBitsNow.BizBitsNow.Service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
@AllArgsConstructor
public class AddressController {
   private final AddressService addressService;

    @PreAuthorize("hasAnyRole('CUSTOMER')")
   @PostMapping("/addAddress")
    public ResponseEntity<AddressDTO> addAddress(@RequestBody AddressDTO addressDTO){
       return ResponseEntity.ok(addressService.addAddress(addressDTO));
   }

    @PreAuthorize("hasAnyRole('CUSTOMER')")
   @PutMapping("/updateAddress/{AddressId}")
    public ResponseEntity<AddressDTO> updateAddress(@RequestBody AddressDTO addressDTO , @PathVariable Long addressId){
       return ResponseEntity.ok(addressService.updateAddress(addressDTO,addressId));
   }

    @PreAuthorize("hasAnyRole('CUSTOMER')")
   @DeleteMapping("/deleteAddress/{AddressId}")
    public  ResponseEntity<?> deleteAddress(@PathVariable Long addressId){
       addressService.deleteAddress(addressId);
       return ResponseEntity.ok("Address Deleted Successfully");
   }

    @PreAuthorize("hasAnyRole('CUSTOMER')")
   @GetMapping("/allAddress")
    public ResponseEntity<List<AddressDTO>> getAllAddress(){
       return ResponseEntity.ok(addressService.getAllAddress());
   }

    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @GetMapping("/default")
    public ResponseEntity<AddressDTO> getDefaultAddress() {
        return ResponseEntity.ok(addressService.getDefaultAddress());
    }


    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @PatchMapping("/set-default/{addressId}")
    public ResponseEntity<String> setDefaultAddress(@PathVariable Long addressId) {
        addressService.setDefaultAddress(addressId);
        return ResponseEntity.ok("Address set as default");
    }
}
