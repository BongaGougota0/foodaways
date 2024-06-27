package za.co.foodaways.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import za.co.foodaways.model.Product;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Product,Integer> {
    List<Product> findBySales();

    List<Product> findByRating();
}
