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
    private final PasswordEncoder passwordEncoder;
    private final StoreUserRepository storeUserRepository;
    @Autowired
    public FoodawaysAuthenticationProvider(StoreUserRepository repo, PasswordEncoder encoder){
        this.storeUserRepository = repo;
        this.passwordEncoder = encoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        StoreUser storeUser = storeUserRepository.findByEmail(email);
        if(storeUser != null && storeUser.userId > 0
                && passwordEncoder.matches(password, storeUser.getPassword())){
            return new UsernamePasswordAuthenticationToken(email,
                    null, getGrantedAuthorities(storeUser));
        }
        else {
            throw new BadCredentialsException("Invalid User details");
        }
    }

    private List<GrantedAuthority> getGrantedAuthorities(StoreUser user) {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        if(user.getRole().getRole() == 1){
            grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            return grantedAuthorityList;
        }
        if(user.getRole().getRole() == 2 ? grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_CUSTOMER")) :
        grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_STORE_OWNER")));
        return grantedAuthorityList;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
