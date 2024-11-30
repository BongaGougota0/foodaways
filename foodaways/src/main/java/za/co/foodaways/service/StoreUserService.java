package za.co.foodaways.service;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.foodaways.dto.OrderDto;
import za.co.foodaways.model.*;
import za.co.foodaways.repository.RoleRepository;
import za.co.foodaways.repository.StoreRepository;
import za.co.foodaways.repository.StoreUserRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class StoreUserService {

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
        Optional<StoreUser> user = storeUserRepository.findById(userId);
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

    public void placeCustomerOrder(OrderDto orderProducts, StoreUser user) {
        String val = orderService.customerNewOrder(orderProducts, user);
    }
}
