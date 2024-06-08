package za.co.foodaways.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.foodaways.model.StoreUser;

@Repository
public interface StoreUserRepository extends JpaRepository<StoreUser, Integer> {
    StoreUser findByPhoneNumber(String mobileNumber);
}
