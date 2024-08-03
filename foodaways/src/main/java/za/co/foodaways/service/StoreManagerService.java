package za.co.foodaways.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import za.co.foodaways.model.Order;
import za.co.foodaways.model.Product;
import za.co.foodaways.repository.StoreManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class StoreManagerService implements StoreManager {

    private final EntityManager entityManager;
    @Autowired
    public StoreManagerService(EntityManager entityManager){
        this.entityManager = entityManager;
    }
    @Override
    public void addNewProduct(Product newProduct) {
        entityManager.persist(newProduct);
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
                            .filter( order -> order.orderStatus == 1)
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
                    .filter( order -> order.orderStatus == 3)
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
                    .filter( order -> order.orderStatus == 2)
                    .collect(Collectors.toList());
        }
        return inProgressOrders;
    }
}
