package za.co.foodaways.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.foodaways.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}