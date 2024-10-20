package za.co.foodaways.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import za.co.foodaways.dto.OrderDto;
import za.co.foodaways.exceptions.MultiStoreOrderException;
import za.co.foodaways.mapper.DtoMapper;
import za.co.foodaways.model.*;
import za.co.foodaways.repository.OrderRepository;
import za.co.foodaways.repository.StoreRepository;
import za.co.foodaways.repository.StoreUserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class OrderService {
    OrderRepository orderRepository;
    StoreUserRepository storeUserRepository;
    StoreRepository storeRepository;
    DtoMapper dtoMapper;

    public OrderService(OrderRepository orderRepository, StoreUserRepository storeUserRepository, StoreRepository storeRepository, DtoMapper dtoMapper){
        this.orderRepository = orderRepository;
        this.storeUserRepository = storeUserRepository;
        this.storeRepository = storeRepository;
        this.dtoMapper = dtoMapper;
    }

    public void declineOrderUpdate(int orderId, String declineReason){
        Order order = orderRepository.findById(orderId).get();
        order.setOrderStatus(OrderStatus.Status.ORDER_DECLINED.name());
    }

    public void acceptOrderUpdate(int orderId){
        Order order = orderRepository.findById(orderId).get();
        order.setOrderStatus(OrderStatus.Status.ORDER_ACCEPTED.name());
        orderRepository.save(order);
    }

    public String customerNewOrder(OrderDto newOrder, StoreUser user){
        ArrayList<Product> products = newOrder.orderItems.stream()
                .map(dtoMapper::toEntity).collect(Collectors.toCollection(ArrayList::new));
        Product randomProduct = products.stream().findFirst().get();
       boolean predicate = products.stream().allMatch(product -> product.getStoreId() == randomProduct.getStoreId());
        if(predicate){
            // go on and place order
            Store store = storeRepository.findStoreByProductId(randomProduct.getStoreId());
            Order order = new Order();
            order.setOrderStatus(OrderStatus.Status.ORDER_PLACED.name());
            order.setUser(user);
            order.setStoreOrder(store);
            order.setOrderItems(products);
            // order total ?!
            Order placedOrder = orderRepository.save(order);
            return placedOrder.getOrderStatus();
        }else {
            throw new MultiStoreOrderException("You cannot place a single order to multiple store.\n " +
                    "Please make your order has products to the same store.");
        }
    }

    public List<Order> getStoreOrders(int storeId){
        ArrayList<Order> storeOrders = orderRepository.findOrdersByStoreId(storeId);
        return storeOrders;
    }
    public List<Order> getStoreOrdersByStatus(int storeId){
        ArrayList<Order> storeOrders = new ArrayList<>();
        List<Order> storeOrdersById = orderRepository.findStoreCompletedOrders(storeId);
        if(storeOrdersById.isEmpty()){
            return storeOrders;
        }
        storeOrders.addAll(storeOrdersById);
        return storeOrders;
    }

    public void updateOrder(int orderId, String cancel) {

    }

    public ArrayList<Order> getCustomerOrdersById(Integer userId) {
//      ArrayList<Order> customerOrders = new ArrayList<>();
        return orderRepository.findUserOrdersById(userId);
    }
}
