package za.co.foodaways.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import za.co.foodaways.model.*;
import za.co.foodaways.repository.OrderRepository;
import za.co.foodaways.repository.ProductsRepository;
import za.co.foodaways.repository.StoreManager;
import za.co.foodaways.repository.StoreRepository;
import za.co.foodaways.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class StoreManagerService implements StoreManager {

    ProductsRepository productsRepository;
    OrderRepository orderRepository;
    StoreRepository storeRepository;

    public StoreManagerService(ProductsRepository productsRepository,
                               OrderRepository orderRepository, StoreRepository storeRepository){
        this.productsRepository = productsRepository;
        this.orderRepository = orderRepository;
        this.storeRepository = storeRepository;
    }

    @Override
    public Store getManagedStoreById(int storeId) {
        Optional<Store> foundStore = storeRepository.findByIdManagerId(storeId);
        return foundStore.get();
    }

    @Override
    public Product getstoreProductById(int productId) {
        return productsRepository.getReferenceById(productId);
    }

    @Override
    public Optional<Product> addNewProduct(Product newProduct) {
        Product product =  productsRepository.save(newProduct);
        return Optional.of(product);
    }


    public void addProduct(Product newProduct, MultipartFile productImage, Store store, int storeAdminId){
        String imageName = Utils.writeImage(productImage);
        newProduct.setImageOfProduct(imageName);
        newProduct.setProductImagePath("..");
        newProduct.setStore(store);
        this.adminAddNewProduct(newProduct, storeAdminId);
        Product savedProduct = productsRepository.save(newProduct);
    }

    @Transactional
    public void adminAddNewProduct(Product product, int adminId){
        productsRepository.save(product);
    }

    @Override
    public void editOrder(int orderId, Order order) {

    }

    public void updateProduct(Product updateProduct, int productId){
        Product productToUpdate = productsRepository.findById(productId).get();
        productToUpdate.setProductPrice(updateProduct.getProductPrice());
        productToUpdate.setProductName(updateProduct.getProductName());
        productToUpdate.setProductCategory(updateProduct.getProductCategory());
        productToUpdate.setMenuItems(updateProduct.getMenuItems());
        if(updateProduct.imageOfProduct != null & !productToUpdate.getImageOfProduct().equals(updateProduct.imageOfProduct)){
            productToUpdate.setImageOfProduct(updateProduct.getImageOfProduct());
        }
        productsRepository.save(productToUpdate);
    }

    @Override
    public void updateOrder(int orderId, Order updatedOrder) {
        Order orderUpdate = orderRepository.getReferenceById(orderId);
        if (orderUpdate != null){
            orderUpdate.setOrderStatus(updatedOrder.orderStatus);
            orderRepository.save(orderUpdate);
        }
    }

    @Override
    public void deleteOrder(int orderId) {
        Order order = orderRepository.getReferenceById(orderId);
        if(order != null){
            orderRepository.deleteById(orderId);
        }
    }

    @Override
    public void sendOrderForDelivery(int driverId) {

    }

    public List<Product> getStoreProductsByManagerId(int managerId){
        return productsRepository.findStoreProductsByAdminId(managerId);
    }

    public void deleteProductById(int productId) {
        Product product = productsRepository.findById(productId).get();
        if(product != null){
            product.setStore(null);
            Utils.deleteImage(product.imageOfProduct);
            productsRepository.delete(product);
        }
    }

    @Override
    public Page<Order> getAllStoreOrders(int storeId, int pageNum, String sortField) {
        return orderRepository.findOrdersByStoreId(storeId, "ORDER_PLACED",getPageable(pageNum, sortField));
    }

    @Override
    public ArrayList<Order> getAllStoreOrders(int storeId) {
        return orderRepository.findOrdersByStoreId(storeId);
    }

    @Override
    public Page<Order> getAllNewOrders(int storeId, int pageNum, String sortField) {
        return orderRepository.findOrdersByStoreId(storeId,"ORDER_PLACED", getPageable(pageNum, sortField));
    }

    @Override
    public Page<Order> getCompletedOrders(int storeId, int pageNum, String sortField) {
        return orderRepository.findOrdersByStoreId(storeId, "DELIVERED", getPageable(pageNum, sortField));
    }

    @Override
    public Page<Order> getCancelledOrders(int storeId, int pageNum, String sortField) {
        return orderRepository.findOrdersByStoreId(storeId, "DECLINED", getPageable(pageNum, sortField));
    }

    @Override
    public Page<Order> getInProgressOrders(int storeId, int pageNum, String sortField) {
        return orderRepository.findOrdersByStoreId(storeId, "IN_PROGRESS", getPageable(pageNum, sortField));
    }

    @Override
    public Page<Order> getDeliveredOrders(int storeId, int pageNum, String sortField) {
        return orderRepository.findOrdersByStoreId(storeId, "DELIVERED", getPageable(pageNum, sortField));
    }

    @Override
    public Page<Product> getStoreProductsById(int pageNum, int storeId, String sortField) {
        return productsRepository.findStoreProducts(storeId, getPageable(pageNum, sortField));
    }

    @Override
    public Page<Product> getStoreProductsById(int pageNum, int storeId) {
        return productsRepository.findStoreProducts(storeId, getPageable(pageNum, "product_name"));
    }

    private Pageable getPageable(int pageNum, String sortField){
        return PageRequest.of(pageNum - 1, 20, Sort.by(sortField).ascending());
    }
}
