package za.co.foodaways.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import za.co.foodaways.model.Roles;
import za.co.foodaways.model.StoreUser;
import za.co.foodaways.repository.StoreUserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class FoodawaysAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    StoreUserRepository storeUserRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String phoneNumber = authentication.getName();
        String password = authentication.getCredentials().toString();

        StoreUser storeUser = storeUserRepository.findByPhoneNumber(phoneNumber);
        if(storeUser != null
                && storeUser.id > 0
                && passwordEncoder.matches(password, storeUser.getPassword())){
            return new UsernamePasswordAuthenticationToken(phoneNumber,
                    null, getGrantedAuthorities(storeUser));
        }
        else {
            throw new BadCredentialsException("Invalid User details");
        }
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities(StoreUser user) {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        if(user.roleId == 2 ? grantedAuthorityList.add(new SimpleGrantedAuthority("Role_"+Roles.Role.CUSTOMER.getRoleName())) :
        grantedAuthorityList.add(new SimpleGrantedAuthority("Role_"+Roles.Role.STORE_OWNER.getRoleName())));
        return grantedAuthorityList;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
