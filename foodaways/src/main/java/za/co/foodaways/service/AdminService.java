package za.co.foodaways.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import za.co.foodaways.dto.StoreAllocationDto;
import za.co.foodaways.dto.StoreDto;
import za.co.foodaways.mapper.BaseEntityDtoMapper;
import za.co.foodaways.model.Store;
import za.co.foodaways.model.StoreUser;
import za.co.foodaways.repository.RoleRepository;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService{
    StoreAdministrationService storeAdministrationService;
    UsersAdministrationService usersAdministrationService;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;


    public AdminService(StoreAdministrationService storeAdministrationService,
            UsersAdministrationService usersAdministrationService, PasswordEncoder passwordEncoder,
                        RoleRepository roleRepository){
        this.storeAdministrationService = storeAdministrationService;
        this.usersAdministrationService = usersAdministrationService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
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


    public Store createNewStore(Store store) {
        return storeAdministrationService.getRepository().save(store);
    }


    public StoreUser createUnmappedStoreOwner(StoreUser storeUser) {
        storeUser.setPassword(passwordEncoder.encode(storeUser.getPassword()));
        return usersAdministrationService.getRepository().save(storeUser);
    }

    private StoreUser getUserHelper(){
        return usersAdministrationService.storeUserRepository
                .findStoreUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
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

    public ArrayList<StoreDto> getStoreWithNoAdmins(){
        BaseEntityDtoMapper<StoreDto, Store> mapper = store -> new StoreDto(store.getStoreId(), store.getStoreName(),
                "NOT_ALLOCATED", store.getStoreNumber(), store.getStoreLocation());
        storeAdministrationService.storeRepository.findStoresWithNoAdmin()
                .stream().map(mapper::getDto).forEach(System.out::println);
        return storeAdministrationService.storeRepository.findStoresWithNoAdmin()
                .stream().map(mapper::getDto).collect(Collectors.toCollection(ArrayList::new));
    }

    public StoreUser assignUserToStoreByStoreId(StoreAllocationDto storeAllocationDto){
        StoreUser storeUser = usersAdministrationService.storeUserRepository.findStoreUserByEmail(storeAllocationDto.userEmail);
        storeUser.setRole(roleRepository.findRoleByRoleName("STORE_OWNER"));
        storeUser.setManagedStore(storeAdministrationService.findById(storeAllocationDto.storeId));
        return usersAdministrationService.getRepository().save(storeUser);
    }
}
