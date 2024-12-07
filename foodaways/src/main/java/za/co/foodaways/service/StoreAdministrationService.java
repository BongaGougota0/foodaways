package za.co.foodaways.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import za.co.foodaways.model.Store;
import za.co.foodaways.repository.StoreRepository;
import java.util.Optional;

@Service
public class StoreAdministrationService implements FilterableCrudService<Store> {

    StoreRepository storeRepository;

    public StoreAdministrationService(StoreRepository storeRepository){
        this.storeRepository = storeRepository;
    }

    @Override
    public Page<Store> findAllEntities(Pageable pageable) {
        Pageable pgbl = PageRequest.of(0,15);
        return storeRepository.findAll(pgbl);
    }

    @Override
    public Page<Store> findEntitiesByStatus(Optional<String> filterString, Pageable pageable) {
        if(filterString.isPresent()){
            Pageable pgblFiltered = PageRequest.of(0, 15,
                    Sort.by(filterString.get()));
            Page<Store> storePg = this.storeRepository.findStoreByOrderNumbers(pgblFiltered);
            return storePg;
        }
        Pageable pgbl = PageRequest.of(0, 15);
        Page<Store> storePgs = getRepository().findAll(pgbl);
        return storePgs;
    }

    @Override
    public JpaRepository<Store, Integer> getRepository() {
        return this.storeRepository;
    }

    @Override
    public Store findById(int id) {
        return getRepository().findById(id).get();
    }
}
