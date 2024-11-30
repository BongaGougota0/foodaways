package za.co.foodaways.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.foodaways.model.Order;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM Orders o WHERE o.store_id =:storeId AND o.order_status = 'ORDER_COMPLETED'")
    List<Order> findStoreCompletedOrders(@Param("storeId") int storeId);

    @Query(nativeQuery = true, value = "SELECT * FROM Orders o WHERE o.store_id = :storeId")
    ArrayList<Order> findOrdersByStoreId(@Param("storeId") int storeId);

    @Query(nativeQuery = true, value = "SELECT * FROM Orders o WHERE o.user_id = :userId")
    ArrayList<Order> findUserOrdersById(@Param("userId") int userId);

    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO orders(order_items, order_status, user_id, store_reference_id)" +
            " VALUES(:items, :status, :storeId, :userId)")
    void placeOrder(String items, String status, int storeId, int userId);
}
