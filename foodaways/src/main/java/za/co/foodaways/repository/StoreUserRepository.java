package za.co.foodaways.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.foodaways.model.Store;
import za.co.foodaways.model.StoreUser;

import java.util.Optional;

@Repository
public interface StoreUserRepository extends JpaRepository<StoreUser, Integer> {
    StoreUser findByPhoneNumber(String mobileNumber);
    StoreUser findByEmail(String email);

}
