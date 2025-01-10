package za.co.foodaways.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import za.co.foodaways.model.StoreUser;

@Repository
public interface StoreUserRepository extends JpaRepository<StoreUser, Integer> {
    StoreUser findStoreUserByPhoneNumber(String mobileNumber);
    StoreUser findStoreUserByEmail(String email);

//    global admin functions
    @Query(nativeQuery = true, value = "SELECT * FROM store_user")
    Page<StoreUser> findUsersByFilter(Pageable pageable);
}
