package za.co.foodaways.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.foodaways.model.Address;

public interface AddressService extends JpaRepository<Address, Integer> {
}
