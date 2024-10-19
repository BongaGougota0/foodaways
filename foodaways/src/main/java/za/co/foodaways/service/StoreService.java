package za.co.foodaways.service;

import org.springframework.stereotype.Service;
import za.co.foodaways.model.Store;
import za.co.foodaways.repository.StoreRepository;

@Service
public class StoreService {

    private final StoreRepository storeRepository;
    public StoreService(StoreRepository storeRepository){
        this.storeRepository = storeRepository;
    }

    public Store findStoreByProductId(int productId){
        return storeRepository.findStoreByProductId(productId);
    }
}
