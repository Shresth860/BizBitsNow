package com.BizBitsNow.BizBitsNow.Service;

import com.BizBitsNow.BizBitsNow.DTO.CustomerDTO;
import com.BizBitsNow.BizBitsNow.Entity.Customer;
import com.BizBitsNow.BizBitsNow.Repository.CustomerRepo;
import lombok.AllArgsConstructor;
//import org.jspecify.annotations.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepo customerRepo;

    @Transactional
    public void deleteCustomer(Long id) {
         customerRepo.deleteById(id);
    }

    @Transactional
    public  CustomerDTO updateCustomer(CustomerDTO customerDTO, Long id) {
        // 1. Pehle check karein ki customer exist karta hai ya nahi
        Customer existingCustomer = customerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        // 2. Fields update karein (Sirf wahi fields jo change karni hain)
        existingCustomer.setFullName(customerDTO.getFullName());
        existingCustomer.setEmail(customerDTO.getEmail());
        existingCustomer.setMobileNumber(customerDTO.getMobileNumber());


        // 3. Save karein
        Customer updatedCustomer = customerRepo.save(existingCustomer);

        // 4. Entity ko wapas DTO mein convert karke return karein
        return mapToDTO(updatedCustomer);
    }

    // Helper method to convert Entity to DTO
    private CustomerDTO mapToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setFullName(customer.getFullName());
        dto.setEmail(customer.getEmail());
        dto.setRole(customer.getRole().name());
        dto.setMobileNumber(customer.getMobileNumber());

        // Agar seller hai toh uski ID set karein
        if(customer.getSeller() != null) {
            dto.setSellerId(customer.getSeller().getId());
        }
        return dto;
    }

    public CustomerDTO getMyProfile() {

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        Customer customer = customerRepo
                .findByMobileNumber(username)
                .orElseThrow(() ->
                        new RuntimeException("Profile not found"));

        return mapToDTO(customer);
    }
    public  List<CustomerDTO> getAllCustomer() {
        return customerRepo.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public  List<CustomerDTO> getAllCustomerBySellerId(Long sellerId) {
        // 1. Database se list fetch karein
        List<Customer> customers = customerRepo.findBySellerId(sellerId);

        // 2. Stream use karke DTO mein convert karein
        return customers.stream()
                .map(this::mapToDTO)
                .toList();
    }
}
