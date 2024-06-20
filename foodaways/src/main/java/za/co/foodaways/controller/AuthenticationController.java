package za.co.foodaways.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import za.co.foodaways.model.StoreUser;
import za.co.foodaways.service.StoreUserService;

@Controller
public class AuthenticationController {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    StoreUserService storeUserService;
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
    public String register(Model model){
        model.addAttribute("storeUser", new StoreUser());
        return "register.html";
    }

    @RequestMapping(value = "/addUser", method = {RequestMethod.POST})
    public String addNewUser(@Valid @ModelAttribute("storeUser") StoreUser storeUser, Errors errors){
        System.out.println("Input from postman "+ storeUser.toString());
        if(errors.hasErrors()){
            return "redirect:/register";
        }
        storeUser.setPassword(passwordEncoder.encode(storeUser.getPassword()));
        int saved = storeUserService.createUser(storeUser);
        if(saved > 0){
            System.out.println("User is saved! ---------------------------  ");
            return "redirect:/login?login=true";
            }
        System.out.println("Input from postman-- "+ storeUser.toString());
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
