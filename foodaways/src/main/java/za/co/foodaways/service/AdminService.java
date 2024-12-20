package za.co.foodaways.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import za.co.foodaways.model.Store;
import za.co.foodaways.model.StoreUser;

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

    public Page<StoreUser> viewUsers(int pageNumber, String sortFieldName){
        if(!sortFieldName.isBlank()){
            Pageable pageable = PageRequest.of(pageNumber - 1,
                    15, Sort.by(sortFieldName).ascending());
            return usersAdministrationService.findAllEntities(pageable);
        }
        Pageable pageable = PageRequest.of(1,15);
        return usersAdministrationService.findAllEntities(pageable);
    }


    public Store createNewStore(Store store) {
        return storeAdministrationService.getRepository().save(store);
    }


    public StoreUser createUnmappedStoreOwner(StoreUser storeUser) {
        storeUser.setPassword(passwordEncoder.encode(storeUser.getPassword()));
        return usersAdministrationService.getRepository().save(storeUser);
    }

    private StoreUser getUserHelper(){
        return usersAdministrationService.storeUserRepository
                .findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
