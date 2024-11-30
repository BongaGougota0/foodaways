package za.co.foodaways.service;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import za.co.foodaways.dto.CartDto;
import za.co.foodaways.dto.OrderDto;
import za.co.foodaways.dto.ProductDto;
import za.co.foodaways.exceptions.MultiStoreOrderException;
import za.co.foodaways.mapper.DtoMapper;
import za.co.foodaways.model.*;
import za.co.foodaways.repository.OrderRepository;
import za.co.foodaways.repository.StoreRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService implements CrudService<Order>{
    OrderRepository orderRepository;
    StoreRepository storeRepository;
    DtoMapper dtoMapper;

    public OrderService(OrderRepository orderRepository,
                        StoreRepository storeRepository, DtoMapper dtoMapper){
        this.orderRepository = orderRepository;
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

    // Not in use. Keep for ref.
    @Transactional
    public Order customerNewOrder(OrderDto newOrder, StoreUser user){
        ArrayList<Product> products = newOrder.orderItems.stream()
                .map(dtoMapper::toEntity).collect(Collectors.toCollection(ArrayList::new));
        Optional<Store> store = storeRepository.findById(newOrder.storeId);
        Order order = new Order();
        order.setOrderStatus(OrderStatus.Status.ORDER_PLACED.name());
        order.setUser(user);
        order.setStore(store.get());
        order.setOrderItems(products);
        order.setOrderItemsString();
        order.setOrderTotal(order.getOrderTotal());
        return getRepository().save(order);
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
        return orderRepository.findUserOrdersById(userId);
    }


    @Override
    public JpaRepository<Order, Integer> getRepository() {
        return this.orderRepository;
    }

    @Override
    public Order findById(int id) {
        Optional<Order> order = getRepository().findById(id);
        return order.get();
    }
}
