package za.co.foodaways.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import za.co.foodaways.model.Order;
import za.co.foodaways.service.OrderService;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    @RequestMapping(value = "/new-order")
    public ModelAndView newOrder(@ModelAttribute("newOrder") Order order){
        String orderStatus = orderService.newOrder(order);
        System.out.println("Order status "+orderStatus);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/home");
        return mav;
    }
}
