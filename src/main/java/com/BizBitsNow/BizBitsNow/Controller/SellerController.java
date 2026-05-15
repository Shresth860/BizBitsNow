package com.BizBitsNow.BizBitsNow.Controller;

import com.BizBitsNow.BizBitsNow.DTO.SellerDTO;
import com.BizBitsNow.BizBitsNow.Entity.Seller;
import com.BizBitsNow.BizBitsNow.Service.SellerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers")
@AllArgsConstructor
public class SellerController {
    private final SellerService sellerService;

    //delete Seller (permanently)
    @PreAuthorize("hasAnyRole('ADMIN','SELLER')")
    @DeleteMapping("/delete/{sellerId}")
    public ResponseEntity<?> deleteSeller(@PathVariable Long id){
        sellerService.deleteSeller(id);
        return ResponseEntity.ok("Seller Deleted Successfully :");
    }

    // update Seller Details
    @PreAuthorize("hasRole('SELLER')")
    @PutMapping("/update/{sellerId}")
    public ResponseEntity<SellerDTO> updateSeller(@PathVariable Long id , @RequestBody SellerDTO sellerDTO){
        return ResponseEntity.ok(sellerService.updateSeller(id,sellerDTO));
    }

    // get Seller Profile
    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("/me")
    public ResponseEntity<SellerDTO> getMyInfo(){
        return ResponseEntity.ok(sellerService.getMyInfo());
    }

    // get All Seller
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<SellerDTO>> getAllSeller(){
        return ResponseEntity.ok(sellerService.getAllSeller());
    }

}
