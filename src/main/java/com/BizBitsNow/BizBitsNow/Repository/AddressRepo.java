package com.BizBitsNow.BizBitsNow.Repository;

import com.BizBitsNow.BizBitsNow.Entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepo extends JpaRepository<Address , Long> {
    List<Address> findByCustomerIdAndIsDefaultTrue(Long customerId);

    List<Address> findByCustomerId(Long customerId);
}
