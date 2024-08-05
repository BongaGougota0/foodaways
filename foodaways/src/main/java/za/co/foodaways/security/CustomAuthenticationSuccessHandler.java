package za.co.foodaways.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        System.out.println("-- User authentication : "+authentication.getAuthorities().toString());
        String userDestinationOnSuccesslogin = determineUserSuccessLoginView(authentication);
        response.sendRedirect(userDestinationOnSuccesslogin);
    }

    private String determineUserSuccessLoginView(Authentication authentication) {
        if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            return "/foodaways-admin";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"))) {
            return "/index";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STORE_OWNER"))) {
            return "/store-manager";
        }
        return "/";
    }
}
