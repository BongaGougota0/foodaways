package za.co.foodaways.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import za.co.foodaways.model.StoreUser;
import za.co.foodaways.service.StoreUserService;

@Controller
public class AuthenticationController {
    PasswordEncoder passwordEncoder;
    StoreUserService storeUserService;

    public AuthenticationController(PasswordEncoder passwordEncoder, StoreUserService storeUserService){
        this.passwordEncoder = passwordEncoder;
        this.storeUserService = storeUserService;
    }
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public String login(Model model,
                        @RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "register", required = false) String register,
                        @RequestParam(value = "logout", required = false) String logout,
                        @RequestParam(value = "login", required = false) String login){
        String errorMessage = null;
        if(error != null){
            errorMessage = "An error occured please check your authentication details";
        } else if (register != null) {
            errorMessage = "Please register a new account";
        }else if(logout != null){
            errorMessage = "logout successful.";
        } else if (login != null) {
            errorMessage = "Please login to your account";
        }
        model.addAttribute("errorMessage", errorMessage);
        return "login.html";
    }

    @RequestMapping(value = "/register", method = {RequestMethod.GET})
    public String register(Model model,
                           @RequestParam(value = "error", required = false) String error){
        model.addAttribute("storeUser", new StoreUser());
        String errorMessage = null;
        if(error != null){
            errorMessage = "An error occurred when trying to register, please try again.";
        }
        model.addAttribute("errorMessage", errorMessage);
        return "register.html";
    }

    @RequestMapping(value = "/addUser", method = {RequestMethod.POST})
    public String addNewUser(@Valid @ModelAttribute("storeUser") StoreUser storeUser, Errors errors){
        if(errors.hasErrors()){
            return "redirect:/register?error=true";
        }
        storeUser.setPassword(passwordEncoder.encode(storeUser.getPassword()));
        int saved = storeUserService.createUser(storeUser);
        if(saved > 0){
            return "redirect:/login?login=true";
            }
        return "redirect:/login?login=true";
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.GET})
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout=true";
    }
}
