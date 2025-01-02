package za.co.foodaways.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import za.co.foodaways.exceptions.UserExistsException;

@ControllerAdvice
public class FoodawaysExceptionController {

    @ExceptionHandler(UserExistsException.class)
    public String userExistsExceptionHandler(Model model){
        model.addAttribute("exceptionMessage", "Email already exists, please login");
        model.addAttribute("exceptionTitle", "Login instead?");
        return "error_page.html";
    }

    @ExceptionHandler(Exception.class)
    public String foodAwaysGlobalException(Model model, Exception exception){
        model.addAttribute("exceptionMessage", exception.getMessage());
        model.addAttribute("exceptionTitle", "There was an error on our side.");
        return "error_page.html";
    }
}
