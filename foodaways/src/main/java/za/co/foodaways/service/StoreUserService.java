package za.co.foodaways.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import za.co.foodaways.dto.OrderDto;
import za.co.foodaways.mapper.SocketDtoMapper;
import za.co.foodaways.model.*;
import za.co.foodaways.repository.RoleRepository;
import za.co.foodaways.repository.StoreRepository;
import za.co.foodaways.repository.StoreUserRepository;
import java.util.Optional;

@Service
public class StoreUserService implements CrudService<StoreUser>{

    public final StoreUserRepository storeUserRepository;
    public final StoreRepository storeRepository;
    public RoleRepository roleRepository;
    public final OrderService orderService;

    public StoreUserService(OrderService orderService, StoreUserRepository storeUserRepository,
                            StoreRepository storeRepository, RoleRepository roleRepository){
        this.orderService = orderService;
        this.storeUserRepository = storeUserRepository;
        this.storeRepository = storeRepository;
        this.roleRepository = roleRepository;
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
        return store.get();
    }

    public void submitReview(Review review) {
    }

    public SocketOrderDto placeCustomerOrder(OrderDto orderProducts, StoreUser user) {
        Order savedOrder =  orderService.customerNewOrder(orderProducts, user);
        if(savedOrder.getOrderStatus().equalsIgnoreCase(OrderStatus.Status.ORDER_PLACED.name())){
            SocketDtoMapper orderDto = () -> {
                SocketOrderDto o = new SocketOrderDto();
                o.setOrderId(savedOrder.getOrderId());
                o.setOrderStatus(savedOrder.getOrderStatus());
                o.setOrderRecipient(savedOrder.getUser().getFullName());
                o.setOrderItems(savedOrder.getOrder_items());
                o.setDeliveryAddress("FOR_NOW_EMPTY");
                return o;
            };
            return orderDto.getOrderDto();
        }else {
            throw new RuntimeException("Error placing order.");
        }
    }

    @Override
    public JpaRepository<StoreUser, Integer> getRepository() {
        return this.storeUserRepository;
    }

    @Override
    public StoreUser findById(int id) {
        return getUserById(id);
    }
}
