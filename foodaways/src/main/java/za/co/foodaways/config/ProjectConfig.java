package za.co.foodaways.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;
import za.co.foodaways.dto.ProductDto;
import za.co.foodaways.mapper.DtoMapper;
import za.co.foodaways.mapper.EntityMapper;
import za.co.foodaways.model.Product;
import za.co.foodaways.model.StoreUser;
import za.co.foodaways.repository.StoreUserRepository;
import za.co.foodaways.security.CustomAuthenticationSuccessHandler;

import java.util.List;

@Configuration
public class ProjectConfig {
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    public ProjectConfig(CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    @Bean
    SecurityFilterChain projectSecurityConfig(HttpSecurity http) throws Exception {
        http.csrf(d -> d.disable()).authorizeHttpRequests(
                        (re) -> re.requestMatchers("/index").permitAll()
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/logout").permitAll()
                                .requestMatchers("/register").permitAll()
                                .requestMatchers("/about").permitAll()
                                .requestMatchers("/menu").permitAll()
                                .requestMatchers("/contact").authenticated()
                                .requestMatchers("/addUser").permitAll()
                                .requestMatchers("/assets/images/**").permitAll()
                                .requestMatchers("/assets/**").permitAll()
                                .requestMatchers("video/**").permitAll()
                                .requestMatchers("/in/**").hasRole("CUSTOMER")
                                .requestMatchers("/home").permitAll()
                                .requestMatchers("/product-view/**").permitAll()
                                .requestMatchers("/home/**").hasRole("CUSTOMER")
                                .requestMatchers("/store-manager").hasRole("STORE_OWNER")
                                .requestMatchers("/store-manager/**").hasRole("STORE_OWNER")
                                .requestMatchers("/foodaways-admin").hasRole("ADMIN")
                                .requestMatchers("/foodaways-admin/**").hasRole("ADMIN")
                                .requestMatchers("/error?continue").authenticated())
                .formLogin(loginFormConfigure -> loginFormConfigure.loginPage("/login")
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureUrl("/login?error=true").permitAll())
                .logout(logoutFormConfigure
                        -> logoutFormConfigure.logoutSuccessUrl("/index")
                        .invalidateHttpSession(true).permitAll())
                .httpBasic().disable();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

