package za.co.foodaways.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.foodaways.model.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {
}
