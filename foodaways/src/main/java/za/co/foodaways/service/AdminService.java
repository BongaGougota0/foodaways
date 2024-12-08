package za.co.foodaways.service;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import za.co.foodaways.model.Order;
import za.co.foodaways.model.Product;
import za.co.foodaways.model.Store;
import za.co.foodaways.model.StoreUser;
import za.co.foodaways.repository.StoreRepository;
import za.co.foodaways.repository.StoreUserRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AdminService{
    StoreAdministrationService storeAdministrationService;
    UsersAdministrationService usersAdministrationService;
    PasswordEncoder passwordEncoder;


    public AdminService(StoreAdministrationService storeAdministrationService,
            UsersAdministrationService usersAdministrationService, PasswordEncoder passwordEncoder){
        this.storeAdministrationService = storeAdministrationService;
        this.usersAdministrationService = usersAdministrationService;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<Store> getStoresSortByStoreName(int pageNumber, Optional<String> sortFieldName){
        if(sortFieldName.isPresent()){
            Pageable pageable = PageRequest.of(pageNumber - 1,
                    15, Sort.by(sortFieldName.get()).ascending());
            return storeAdministrationService.findAllEntities(pageable);
        }
        Pageable pageable = PageRequest.of(1,15);
        return storeAdministrationService.findAllEntities(pageable);
    }


    public void addNewStoreWithAdmin(Store store, int adminId) {
        storeAdministrationService.getRepository().save(store);
    }

    public void addStoreAdminByUserAndStoreId(int userId, int storeId) {

    }

    public void createUnmappedStoreOwner(StoreUser storeUser) {
        storeUser.setPassword(passwordEncoder.encode(storeUser.getPassword()));
        usersAdministrationService.getRepository().save(storeUser);
    }

    private StoreUser getUserHelper(){
        return usersAdministrationService.storeUserRepository
                .findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
