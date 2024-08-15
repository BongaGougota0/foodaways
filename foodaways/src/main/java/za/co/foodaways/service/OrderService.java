package za.co.foodaways.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import za.co.foodaways.model.Order;
import za.co.foodaways.repository.OrderRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    public String newOrder(Order newOrder){
        Order order = orderRepository.save(newOrder);
        if(order.orderId != null && order.orderId >= 1){
            return "saved";
        }
        return "order_not_created";
    }

    public List<Order> getStoreOrders(String adminEmail){
        ArrayList<Order> storeOrders = new ArrayList<>();
        return  storeOrders;
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
}
