package za.co.foodaways.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import za.co.foodaways.dto.ProductDto;
import za.co.foodaways.dto.StoreUserDto;
import za.co.foodaways.mapper.BaseEntityDtoMapper;
import za.co.foodaways.mapper.DtoMapper;
import za.co.foodaways.model.Product;
import za.co.foodaways.model.Review;
import za.co.foodaways.model.StoreUser;
import za.co.foodaways.security.CustomAuthenticationSuccessHandler;
import za.co.foodaways.service.StoreService;

@Configuration
public class ProjectConfig {
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final StoreService storeService;

    @Autowired
    public ProjectConfig(CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler, StoreService storeService) {
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.storeService = storeService;
    }

    @Bean
    SecurityFilterChain projectSecurityConfig(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        (re) -> re.requestMatchers(
                                "/foodaways").permitAll()
                                .requestMatchers("/logout").permitAll()
                                .requestMatchers("/register").permitAll()
                                .requestMatchers("/all-products").permitAll()
                                .requestMatchers("/addUser").permitAll()
                                .requestMatchers("/assets/images/**").permitAll()
                                .requestMatchers("/assets/**").permitAll()
                                .requestMatchers("video/**").permitAll()
                                .requestMatchers("/carousel-items").permitAll()
                                .requestMatchers("/foodaways/**").permitAll()
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/home").permitAll()
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/in/**").hasRole("CUSTOMER")
                                .requestMatchers("/in/my-orders").hasRole("CUSTOMER")
                                .requestMatchers("/in/cancel-order").hasRole("CUSTOMER")
                                .requestMatchers("/in/submit-review").hasRole("CUSTOMER")
                                .requestMatchers("/in/place-order/").permitAll()
                                .requestMatchers("/in/view-cart/").hasRole("CUSTOMER")
                                .requestMatchers("/in/add-product-to-cart/**").hasRole("CUSTOMER")
                                .requestMatchers("/store-manager/home").hasRole("STORE_OWNER")
                                .requestMatchers("/store-manager/home/**").hasRole("STORE_OWNER")
                                .requestMatchers("/store-manager/products/**").hasRole("STORE_OWNER")
                                .requestMatchers("/store-manager/orders/**").hasRole("STORE_OWNER")
                                .requestMatchers("/store-manager/new-orders/**").permitAll()
                                .requestMatchers("/store-manager/add-new-product").hasRole("STORE_OWNER")
                                .requestMatchers("/store-manager/product-edit-page/**").hasRole("STORE_OWNER")
                                .requestMatchers("/store-manager/store-products-menu").hasRole("STORE_OWNER")
                                .requestMatchers("/store-manager/completed-orders/**").hasRole("STORE_OWNER")
                                .requestMatchers("/store-manager/inprogress-orders/**").hasRole("STORE_OWNER")
                                .requestMatchers("/store-manager/delivered-orders/**").hasRole("STORE_OWNER")
                                .requestMatchers("/store-manager/sales-details/**").hasRole("STORE_OWNER")
                                .requestMatchers("/store-manager/update-product").permitAll()
                                .requestMatchers("/store-manager/reject-order/").permitAll()
                                .requestMatchers("/store-manager/accept-order/").permitAll()
                                .requestMatchers("/foodaways-admin").hasRole("ADMIN")
                                .requestMatchers("/foodaways-admin/**").hasRole("ADMIN")
//                                .requestMatchers("/foodaways-admin/save-new-store").hasRole("ADMIN")
                                .requestMatchers("/error?continue").authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(loginFormConfigure -> loginFormConfigure.loginPage("/login")
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureUrl("/login?error=true").permitAll())
                .logout(logoutFormConfigure
                        -> logoutFormConfigure.logoutSuccessUrl("/foodaways/")
                        .invalidateHttpSession(true).permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DtoMapper dtoMapper(){
        return new DtoMapper() {
            @Override
            public ProductDto toDto(Product entity) {
                return new ProductDto(entity.getProductId(), entity.getMenuItems(), entity.getProductName(),
                        entity.getProductImagePath(), entity.getImageOfProduct(), entity.getProductCategory(),
                        entity.getStore().getStoreName(), entity.getProductPrice(),
                        entity.reviews.stream().mapToInt(Review::getRating).sum(),0, entity.store.getStoreId());
            }

            @Override
            public Product toEntity(ProductDto dto) {
                Product product = new Product();
                product.setProductId(dto.getProductId());
                product.setProductCategory(dto.getProductCategory());
                product.setImageOfProduct(dto.getImageOfProduct());
                product.setProductName(dto.getProductName());
                product.setProductPrice(dto.getProductPrice());
                product.setMenuItems(dto.getMenuItems());
                product.setStore(storeService.findById(dto.storeId));
                return product;
            }
        };
    }

    @Bean
    public BaseEntityDtoMapper<StoreUserDto, StoreUser> getStoreUserDtoMapper(){
        return storeUser -> new StoreUserDto(storeUser.getUserId(),
                storeUser.getFullName(),
                storeUser.getPhoneNumber(), storeUser.getEmail(), storeUser.getRole().getRoleId());
    }
}

