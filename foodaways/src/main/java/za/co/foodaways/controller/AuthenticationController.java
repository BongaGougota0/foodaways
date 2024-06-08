package za.co.foodaways.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthenticationController {
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
        return "loginRegistration.html";
    }

    @RequestMapping(value = "/register", method = {RequestMethod.GET, RequestMethod.POST})
    public String register(Model model){
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
