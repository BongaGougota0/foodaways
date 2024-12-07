package za.co.foodaways.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import za.co.foodaways.model.StoreUser;
import za.co.foodaways.repository.StoreRepository;
import za.co.foodaways.repository.StoreUserRepository;
import java.util.Optional;

@Service
public class UsersAdministrationService implements FilterableCrudService<StoreUser> {
    StoreUserRepository storeUserRepository;

    public UsersAdministrationService(StoreUserRepository storeUserRepository){
        this.storeUserRepository = storeUserRepository;
    }
    @Override
    public Page<StoreUser> findAllEntities(Pageable pageable) {
        return null;
    }

    @Override
    public Page<StoreUser> findEntitiesByStatus(Optional<String> filterString, Pageable pageable) {
        Pageable pgbl = PageRequest.of(0,15, Sort.by(filterString.get()));
        Page<StoreUser> userPage = storeUserRepository.findUsersByFilter(pgbl);
        return userPage;
    }

    @Override
    public JpaRepository<StoreUser, Integer> getRepository() {
        return this.storeUserRepository;
    }

    @Override
    public StoreUser findById(int id) {
        Optional<StoreUser> user =  this.storeUserRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }
        return null;
    }
}
