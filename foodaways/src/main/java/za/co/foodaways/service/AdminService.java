package za.co.foodaways.service;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import za.co.foodaways.model.Order;
import za.co.foodaways.model.Product;
import za.co.foodaways.model.Store;
import za.co.foodaways.model.StoreUser;
import za.co.foodaways.repository.StoreRepository;
import za.co.foodaways.repository.StoreUserRepository;

@Service
public class AdminService {
    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;
    private final StoreUserRepository userRepository;
    private final StoreRepository storeRepository;

    @Autowired
    public AdminService(EntityManager entityManager, PasswordEncoder passwordEncoder,
                        StoreUserRepository userRepository, StoreRepository storeRepository){
        this.entityManager = entityManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
    }


    public void addNewStoreWithAdmin(Store store, int adminId) {
//        store.setStoreOwner(adminId);
        entityManager.persist(store);
    }

    public void addStoreAdminByUserAndStoreId(int userId, int storeId) {
        // Additional admin for store
        Store storeExists = entityManager.find(Store.class, storeId);
        StoreUser userExists = entityManager.find(StoreUser.class, userId);
        if(storeExists != null && userExists != null){
//            storeExists.setStoreOwner(userId);
        }
    }

    public void addNewProductByStoreId(Product newProduct, int storeId) {
//        newProduct.setStoreId(storeId);
        System.out.println("Display added product "+newProduct.toString());
        entityManager.persist(newProduct);
    }

    public void addOrderByStoreIdAndUserId(Order order, int storeId, int userId) {
        order.setOrderId(storeId);
        order.setUser(getUserHelper());
        System.out.println("Display order "+order.toString());
        entityManager.persist(order);
    }

    public void createUnmappedStoreOwner(StoreUser storeUser) {
        storeUser.setPassword(passwordEncoder.encode(storeUser.getPassword()));
//        storeUser.setRole(new Store(3,"S"));
        System.out.println("Added owner "+storeUser);
        entityManager.persist(storeUser);
    }

    private StoreUser getUserHelper(){
        return userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    private Store getStoreHelper(){
        return null;
    }
}
