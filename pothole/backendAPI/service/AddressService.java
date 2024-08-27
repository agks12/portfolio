package com.h2o.poppy.service;

import com.h2o.poppy.model.address.Address;
import com.h2o.poppy.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public int saveAddress(String address) {
        if (!StringUtils.hasText(address)) {
            System.out.println("Invalid address input.");
            return 0;
        }

        if (addressRepository.existsById(address)) {
            System.out.println("Address already exists in the database.");
            return 2;
        }

        Address newAddress = new Address(address);
        addressRepository.save(newAddress);
        System.out.println("Address saved to database successfully.");
        return 1;
    }
}
