package za.co.foodaways.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.foodaways.model.Product;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Product,Integer> {
//    List<Product> findByOrderBySalesDesc();

//    List<Product> findByOrderByRatingDesc();

    @Query(nativeQuery = true, value = "SELECT * FROM Products p WHERE p.store_id = :storeId")
    List<Product> findStoreProductsById(@Param("storeId") int storeId);
}
