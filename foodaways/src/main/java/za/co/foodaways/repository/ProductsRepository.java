package za.co.foodaways.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.foodaways.model.Product;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Product,Integer> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM products p WHERE p.store_id IN " +
                    "(SELECT s.store_id FROM store s LEFT JOIN store_user u ON s.manager_id = u.user_id WHERE u.user_id = :manager_id)")
    List<Product> findStoreProductsByAdminId(@Param("manager_id") int managerId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM product p WHERE p.product_id = :productId")
    void deleteProductById(@Param("productId") int productId);

}
