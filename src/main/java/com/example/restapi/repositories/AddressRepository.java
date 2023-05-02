package com.example.restapi.repositories;

import com.example.restapi.domain.users.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    Address findByAddress(String address);
}
