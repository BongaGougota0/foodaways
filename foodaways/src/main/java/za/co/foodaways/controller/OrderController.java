package za.co.foodaways.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import za.co.foodaways.model.Order;
import za.co.foodaways.service.OrderService;
import za.co.foodaways.service.StoreUserService;

import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final StoreUserService storeUserService;

    @Autowired
    public OrderController(OrderService orderService, StoreUserService storeUserService){
        this.orderService = orderService;
        this.storeUserService = storeUserService;
    }

    @RequestMapping(value = "/store-orders/", method = {RequestMethod.GET})
    public ModelAndView displayOrderPage(Authentication authentication){
        int storeId = storeUserService.findUserByEmail(authentication.getName()).managedStore.storeId;
        List<Order> orderList = orderService.getStoreOrders(storeId);
        ModelAndView mav = new ModelAndView("store_orders.html");
        mav.addObject("storeOrders", orderList);
        return mav;
    }

}
