package za.co.foodaways.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.foodaways.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
