package za.co.foodaways.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import za.co.foodaways.model.Store;
import za.co.foodaways.repository.StoreRepository;
import java.util.Optional;

@Service
public class StoreService implements CrudService<Store>{

    StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository){
        this.storeRepository = storeRepository;
    }

    public Store findStoreByProductId(int productId){
        return storeRepository.findStoreByProductId(productId);
    }

    @Override
    public JpaRepository<Store, Integer> getRepository() {
        return this.storeRepository;
    }

    @Override
    public Store findById(int id) {
        Optional<Store> store = getRepository().findById(id);
        return store.get();
    }
}
