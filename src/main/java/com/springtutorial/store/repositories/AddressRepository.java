package com.springtutorial.store.repositories;

import com.springtutorial.store.entities.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}