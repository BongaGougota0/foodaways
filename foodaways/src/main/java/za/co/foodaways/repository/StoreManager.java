package za.co.foodaways.repository;

import org.springframework.data.domain.Page;
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
    Page<Order> getAllNewOrders(int storeId, int pageNum, String sortField);
    Page<Order> getAllStoreOrders(int storeId, int pageNum, String sortField);
    List<Order> getAllStoreOrders(int storeId); //, int pageNum, String sortField);
    Page<Order> getCompletedOrders(int storeId, int pageNum, String sortField);
    Page<Order> getCancelledOrders(int storeId, int pageNum, String sortField);
    Page<Order> getInProgressOrders(int storeId, int pageNum, String sortField);
    Page<Order> getDeliveredOrders(int storeId, int pageNum, String sortField);
    Page<Product> getStoreProductsById(int pageNum, int storeId, String sortField);
    Page<Product> getStoreProductsById(int pageNum, int storeId);
}
