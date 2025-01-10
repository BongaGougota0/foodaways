package za.co.foodaways.repository;

import org.springframework.data.domain.Page;
import za.co.foodaways.model.Order;
import za.co.foodaways.model.Product;
import za.co.foodaways.model.Store;

public interface StoreManager{
    Store getManagedStoreById(int storeId);
    Product getstoreProductById(int productId);
    Page<Order> getAllStoreOrders(int storeId, int pageNum, String sortField);
    Page<Order> getCompletedOrders(int storeId, int pageNum, String sortField);
    Page<Order> getInProgressOrders(int storeId, int pageNum, String sortField);
    Page<Product> getStoreProductsById(int pageNum, int storeId, String sortField);
}
