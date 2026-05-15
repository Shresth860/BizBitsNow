package com.BizBitsNow.BizBitsNow.Service;

import com.BizBitsNow.BizBitsNow.DTO.AddressDTO;
import com.BizBitsNow.BizBitsNow.Entity.Address;
import com.BizBitsNow.BizBitsNow.Entity.Customer;
import com.BizBitsNow.BizBitsNow.Repository.AddressRepo;
import com.BizBitsNow.BizBitsNow.Repository.CustomerRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
//import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AddressService {
    private final AddressRepo addressRepo;
    private final CustomerRepo customerRepo;

    @Transactional
    public  AddressDTO addAddress(AddressDTO dto) {
        // 1. Logged-in Customer nikalna
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepo.findByMobileNumber(userName) // Ya findByMobileNumber jo bhi aapka identifier ho
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // 2. Agar ye address 'Default' hai, toh purane default addresses ko false karo
        if (dto.isDefault()) {
            resetDefaultAddress(customer.getId());
        }

        // 3. DTO to Entity Mapping
        Address address = new Address();
        address.setFullName(dto.getFullName());
        address.setMobileNumber(dto.getMobileNumber());
        address.setFlatHouseNumber(dto.getFlatHouseNumber());
        address.setAreaLocality(dto.getAreaLocality());
        address.setLandmark(dto.getLandmark());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPincode(dto.getPincode());
        address.setAddressType(dto.getAddressType());
        address.setDefault(dto.isDefault());
        address.setCustomer(customer); // Link address to customer

        Address savedAddress = addressRepo.save(address);
        return mapToDTO(savedAddress);
    }

    // Helper method: Ek customer ke saare addresses se default flag hatane ke liye
    private void resetDefaultAddress(Long customerId) {
        List<Address> addresses = addressRepo.findByCustomerIdAndIsDefaultTrue(customerId);
        addresses.forEach(address -> address.setDefault(false));
        addressRepo.saveAll(addresses);
    }

    private AddressDTO mapToDTO(Address address) {
        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setFullName(address.getFullName());
        dto.setMobileNumber(address.getMobileNumber());
        dto.setFlatHouseNumber(address.getFlatHouseNumber());
        dto.setAreaLocality(address.getAreaLocality());
        dto.setLandmark(address.getLandmark());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setPincode(address.getPincode());
        dto.setAddressType(address.getAddressType());
        dto.setDefault(address.isDefault());
        return dto;

    }

    @Transactional
    public AddressDTO updateAddress(AddressDTO dto, Long addressId) {

        Address existingAddress = addressRepo.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + addressId));


        String loggedInUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!existingAddress.getCustomer().getEmail().equals(loggedInUserEmail)) {
            throw new RuntimeException("Unauthorized: You cannot update someone else's address");
        }

        if (dto.isDefault() && !existingAddress.isDefault()) {
            resetDefaultAddress(existingAddress.getCustomer().getId());
        }


        if (dto.getFullName() != null) existingAddress.setFullName(dto.getFullName());
        if (dto.getMobileNumber() != null) existingAddress.setMobileNumber(dto.getMobileNumber());
        if (dto.getFlatHouseNumber() != null) existingAddress.setFlatHouseNumber(dto.getFlatHouseNumber());
        if (dto.getAreaLocality() != null) existingAddress.setAreaLocality(dto.getAreaLocality());
        if (dto.getLandmark() != null) existingAddress.setLandmark(dto.getLandmark());
        if (dto.getCity() != null) existingAddress.setCity(dto.getCity());
        if (dto.getState() != null) existingAddress.setState(dto.getState());
        if (dto.getPincode() != null) existingAddress.setPincode(dto.getPincode());
        if (dto.getAddressType() != null) existingAddress.setAddressType(dto.getAddressType());


        existingAddress.setDefault(dto.isDefault());

        Address updatedAddress = addressRepo.save(existingAddress);
        return mapToDTO(updatedAddress);
    }
   @Transactional
    public void deleteAddress( Long addressId) {
        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        // 2. Security Check: Kya ye address logged-in customer ka hi hai?
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!address.getCustomer().getEmail().equals(loggedInEmail)) {
            throw new RuntimeException("Unauthorized: You cannot delete this address");
        }

        boolean wasDefault = address.isDefault();
        Long customerId = address.getCustomer().getId();

        // 3. Address delete karein
        addressRepo.delete(address);

        // 4. Logic: Agar default address delete hua hai, toh kisi aur ko default banayein
        if (wasDefault) {
            List<Address> remainingAddresses = addressRepo.findByCustomerId(customerId);
            if (!remainingAddresses.isEmpty()) {
                Address newDefault = remainingAddresses.get(0);
                newDefault.setDefault(true);
                addressRepo.save(newDefault);
            }
        }
    }

    public  List<AddressDTO> getAllAddress() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();


        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));


        List<Address> addresses = addressRepo.findByCustomerId(customer.getId());


        return addresses.stream()
                .map(this::mapToDTO)
                .toList();
    }


    public AddressDTO getDefaultAddress() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));


        Address defaultAddress = addressRepo.findByCustomerIdAndIsDefaultTrue(customer.getId())
                .stream()
                .findFirst() // Pehla default address uthayein
                .orElseThrow(() -> new RuntimeException("No default address found. Please set one."));

        return mapToDTO(defaultAddress);
    }


    @Transactional
    public void setDefaultAddress(Long addressId) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));


        Address targetAddress = addressRepo.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!targetAddress.getCustomer().getId().equals(customer.getId())) {
            throw new RuntimeException("Unauthorized: This address does not belong to you.");
        }


        List<Address> allAddresses = addressRepo.findByCustomerId(customer.getId());
        allAddresses.forEach(address -> address.setDefault(false));
        addressRepo.saveAll(allAddresses);


        targetAddress.setDefault(true);
        addressRepo.save(targetAddress);
    }
}
