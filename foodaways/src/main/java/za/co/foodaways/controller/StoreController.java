package za.co.foodaways.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoreController {
    @RequestMapping(value = "/store-registration")
    public String registerStore(){
        return "index.html";
    }
}
