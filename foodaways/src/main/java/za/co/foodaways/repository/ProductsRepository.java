package za.co.foodaways.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.foodaways.model.Product;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Product,Integer> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM products p WHERE p.store_id = " +
                    "(SELECT s.store_id from store s LEFT JOIN store_user u ON s.manager_id = :manager_id)")
    List<Product> findStoreProductsByAdminId(@Param("manager_id") int managerId);
}
