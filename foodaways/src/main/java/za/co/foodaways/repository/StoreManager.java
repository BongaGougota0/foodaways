package za.co.foodaways.repository;

import za.co.foodaways.model.Order;
import za.co.foodaways.model.Product;
import za.co.foodaways.model.Store;
import za.co.foodaways.service.FilterableCrudService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface StoreManager{
    Store getManagedStoreById(int storeId);
    Product getstoreProductById(int productId);
    Optional<Product> addNewProduct(Product newProduct);
    void editOrder(int orderId, Order order);
    void updateOrder(int orderId, Order updatedOrder);
    void deleteOrder(int orderId);
    void sendOrderForDelivery(int driverId);
    ArrayList<Order> getAllNewOrders(int storeId);
    List<Order> getAllStoreOrders(int storeId);
    List<Order> getCompletedOrders(int storeId);
    List<Order> getCancelledOrders(int storeId);
    List<Order> getInProgressOrders(int storeId);
    ArrayList<Order> getDeliveredOrders(int storeId);
    ArrayList<Product> getStoreProductsById(int storeId);
}
