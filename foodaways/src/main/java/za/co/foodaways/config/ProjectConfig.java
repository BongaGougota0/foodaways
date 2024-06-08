package za.co.foodaways.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectConfig {
    @Bean
    SecurityFilterChain projectSecurityConfig(HttpSecurity http) throws Exception {
        http.csrf(d -> d.disable()).authorizeHttpRequests(
                (re)-> re.requestMatchers("/","/index","/home").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/register").permitAll()
                .requestMatchers("/assets/**").permitAll()
                .requestMatchers("/in/**").authenticated())
                .formLogin(loginFormConfigure -> loginFormConfigure.loginPage("/login")
                        .defaultSuccessUrl("/home")
                        .failureUrl("/login?error=true").permitAll())
                        .logout(logoutFormConfigure
                                -> logoutFormConfigure.logoutSuccessUrl("/login?logout=true").permitAll()
                                .invalidateHttpSession(true).permitAll())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
