package za.co.foodaways.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.foodaways.model.Store;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM store s WHERE s.manager_id = :manager_id LIMIT 1")
    Optional<Store> findByIdManagerId(@Param("manager_id") int managerId);
}
