package za.co.foodaways.service;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.foodaways.model.Roles;
import za.co.foodaways.model.StoreUser;
import za.co.foodaways.repository.RoleRepository;
import za.co.foodaways.repository.StoreUserRepository;

@Service
public class StoreUserService {
    @Autowired
    StoreUserRepository storeUserRepository;
    @Autowired
    RoleRepository roleRepository;

    public StoreUser findUserByEmail(String userEmail){
        return storeUserRepository.findByEmail(userEmail);
    }

    public int createUser(StoreUser user){
        user.setRole(roleRepository.findRoleByRoleName("CUSTOMER"));
        StoreUser savedUser = storeUserRepository.save(user);
        if(savedUser.userId > 0){
            return 1;
        }
        return 0;
    }

}
