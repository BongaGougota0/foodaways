package za.co.foodaways.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class StoreController {
    @RequestMapping(value = "/store-orders")
    public String registerStore(){
        return "order.html";
    }

    @RequestMapping(value = "/records")
    public String records(){
        return "orders.html";
    }

    @RequestMapping(value = "/update-status")
    public String updateOrderStatus(){
        return "redirect:/records";
    }

    @RequestMapping(value = "/accept")
    public String acceptOrder(){
        return "redirect:/records";
    }

    @RequestMapping(value = "/decline")
    public String declineOrder(){
        return "redirect:/records";
    }
}
