package za.co.foodaways.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import za.co.foodaways.model.Order;
import za.co.foodaways.model.OrderStatus;
import za.co.foodaways.model.Product;
import za.co.foodaways.model.Store;
import za.co.foodaways.repository.ProductsRepository;
import za.co.foodaways.repository.StoreManager;
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

    private final EntityManager entityManager;
    ProductsRepository productsRepository;
    @Autowired
    public StoreManagerService(EntityManager entityManager, ProductsRepository productsRepository){
        this.entityManager = entityManager;
        this.productsRepository = productsRepository;
    }
    @Override
    public void addNewProduct(Product newProduct) {
        entityManager.persist(newProduct);
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
        Order updateOrder = new Order();
        TypedQuery<Order> typedQuery = entityManager
                        .createQuery("FROM Order order WHERE order.id =:id", Order.class);
        typedQuery.setParameter("id", orderId);
       List<Order> result = typedQuery.getResultList();
       Optional<Order> orderFirst = result.stream().findFirst();
       if(orderFirst.isPresent()){
           // Update applicable details here.
//           updateOrder = orderFirst.get().orderStatus;
       }
    }

    public void updateProduct(Product updateProduct, int productId){
        Product productToUpdate = productsRepository.findById(productId).get();
        productToUpdate.setProductPrice(updateProduct.getProductPrice());
        productToUpdate.setProductName(updateProduct.getProductName());
        productToUpdate.setProductCategory(updateProduct.getProductCategory());
        productToUpdate.setMenuItems(updateProduct.getMenuItems());

        //image update : check first
        // delete old
        if(updateProduct.imageOfProduct != null & !productToUpdate.getImageOfProduct().equals(updateProduct.imageOfProduct)){
            productToUpdate.setImageOfProduct(updateProduct.getImageOfProduct());
        }
        productsRepository.save(productToUpdate);
    }

    @Override
    public void updateOrder(int orderId, Order updatedOrder) {

    }

    @Override
    public void deleteOrder(int orderId) {
        Order removeOrder = entityManager.find(Order.class, orderId);
        if(!(removeOrder == null)){
            entityManager.remove(removeOrder);
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
            productsRepository.delete(product);
        }
    }

    @Override
    public List<Order> getAllStoreOrders(int storeId) {
        return null;
    }

    @Override
    public List<Order> getCompletedOrders(int storeId) {
        List<Order> completedOrders = new ArrayList<>();
        TypedQuery<Order> typedQuery = entityManager
                .createQuery("FROM Order order WHERE order.storeId  =:id", Order.class);
        typedQuery.setParameter("id",storeId);
        if(!typedQuery.getResultList().isEmpty()){
            completedOrders = typedQuery.getResultList()
                            .stream()
                            .peek(System.out::println) // Preview check function working. # Comment out
                            .filter( order -> order.orderStatus.equals(OrderStatus.Status.DELIVERED))
                            .collect(Collectors.toList());
        }
        return completedOrders;
    }

    @Override
    public List<Order> getCancelledOrders(int storeId) {
        List<Order> cancelledOrders = new ArrayList<>();
        TypedQuery<Order> typedQuery = entityManager
                .createQuery("FROM Order order WHERE order.id =:id", Order.class);
        typedQuery.setParameter("id", storeId);
        if(!typedQuery.getResultList().isEmpty()){
            cancelledOrders = typedQuery.getResultList()
                    .stream()
                    .filter( order -> order.orderStatus.equals(OrderStatus.Status.ORDER_ACCEPTED))
                    .collect(Collectors.toList());
        }
        return cancelledOrders;
    }

    @Override
    public List<Order> getInProgressOrders(int storeId) {
        List<Order> inProgressOrders = new ArrayList<>();
        TypedQuery<Order> typedQuery = entityManager
                        .createQuery("FROM Order order WHERE order.id =:id", Order.class);
        typedQuery.setParameter("id", storeId);
        if(!typedQuery.getResultList().isEmpty()){
            inProgressOrders = typedQuery.getResultList()
                    .stream()
                    .filter( order -> order.orderStatus.equals(OrderStatus.Status.PREPARING_ORDER))
                    .collect(Collectors.toList());
        }
        return inProgressOrders;
    }
}
