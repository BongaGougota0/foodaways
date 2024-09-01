package za.co.foodaways.service;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.foodaways.model.Roles;
import za.co.foodaways.model.StoreUser;
import za.co.foodaways.repository.RoleRepository;
import za.co.foodaways.repository.StoreUserRepository;

import java.util.Optional;

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
        if(savedUser.getUserId() > 0){
            return 1;
        }
        return 0;
    }

    public StoreUser getUserByEmail(String userEmail){
        return storeUserRepository.findByEmail(userEmail);
    }

    public StoreUser getUserById(int userId){
        Optional<StoreUser> user = storeUserRepository.findById(userId);
        return user.get();
    }

}
