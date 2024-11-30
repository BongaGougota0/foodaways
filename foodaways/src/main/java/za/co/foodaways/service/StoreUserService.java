package za.co.foodaways.service;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import za.co.foodaways.dto.OrderDto;
import za.co.foodaways.model.*;
import za.co.foodaways.repository.RoleRepository;
import za.co.foodaways.repository.StoreRepository;
import za.co.foodaways.repository.StoreUserRepository;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class StoreUserService implements CrudService<StoreUser>{

    public final StoreUserRepository storeUserRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    RoleRepository roleRepository;

    public final OrderService orderService;

    public StoreUserService(OrderService orderService, StoreUserRepository storeUserRepository){
        this.orderService = orderService;
        this.storeUserRepository = storeUserRepository;
    }

    public StoreUser findUserByEmail(String userEmail){
        return storeUserRepository.findByEmail(userEmail);
    }

    public int createUser(StoreUser user){
        user.setRole(roleRepository.findRoleByRoleName("CUSTOMER"));
        StoreUser savedUser = storeUserRepository.save(user);
        if(savedUser.getUserId() > 0){
            return 1;
        }
        return 0;
    }

    public StoreUser getUserByEmail(String userEmail){
        return storeUserRepository.findByEmail(userEmail);
    }

    public StoreUser getUserById(int userId){
        Optional<StoreUser> user = getRepository().findById(userId);
        return user.get();
    }

    public Store getManagedStoreByAdminId(int managerId){
        Optional<Store> store = storeRepository.findByIdManagerId(managerId);
        if(store.get() != null){
            return store.get();
        }
        return new Store();
    }

    public void submitReview(Review review) {
    }

    public Order placeCustomerOrder(OrderDto orderProducts, StoreUser user) {
        Order savedOrder =  orderService.customerNewOrder(orderProducts, user);
        if(savedOrder.getOrderStatus().equalsIgnoreCase(OrderStatus.Status.ORDER_PLACED.name())){
            return savedOrder;
        }
        throw new RuntimeException("Error placing order.");
    }

    @Override
    public JpaRepository<StoreUser, Integer> getRepository() {
        return this.storeUserRepository;
    }

    @Override
    public StoreUser findById(int id) {
        return null;
    }
}
