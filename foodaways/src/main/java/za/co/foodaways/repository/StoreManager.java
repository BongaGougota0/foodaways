package za.co.foodaways.repository;

import za.co.foodaways.model.Order;
import za.co.foodaways.model.Product;

import java.util.List;
import java.util.Optional;

public interface StoreManager {
    void addNewProduct(Product newProduct);
    void editOrder(int orderId, Order order);
    void updateOrder(int orderId, Order updatedOrder);
    void deleteOrder(int orderId);
    void sendOrderForDelivery(int driverId);
    List<Order> getAllStoreOrders(int storeId);
    List<Order> getCompletedOrders(int storeId);
    List<Order> getCancelledOrders(int storeId);
    List<Order> getInProgressOrders(int storeId);

}
