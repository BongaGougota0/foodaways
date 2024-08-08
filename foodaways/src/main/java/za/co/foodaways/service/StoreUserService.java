package za.co.foodaways.service;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.foodaways.model.StoreUser;
import za.co.foodaways.repository.StoreUserRepository;

@Service
public class StoreUserService {
    @Autowired
    StoreUserRepository storeUserRepository;

    public StoreUser findUserByEmail(String userEmail){
        return storeUserRepository.findByEmail(userEmail);
    }


    public int createUser(StoreUser user){
        user.setRoleId(2);
        StoreUser User_ = storeUserRepository.save(user);
        if(User_.getId() > 0){
            return 1;
        }
        return 0;
    }

}
