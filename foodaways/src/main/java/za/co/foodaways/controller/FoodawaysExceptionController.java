package za.co.foodaways.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FoodawaysExceptionController {
    @ExceptionHandler(Exception.class)
    public String foodAwaysGlobalException(Model model, Exception exception){
        model.addAttribute("exceptionMessage", exception.getMessage());
        return "error_page.html";
    }
}
