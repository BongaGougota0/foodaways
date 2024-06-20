package za.co.foodaways.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import za.co.foodaways.model.Order;
import za.co.foodaways.model.Reservation;
import za.co.foodaways.service.OrderService;
import za.co.foodaways.service.ReservationService;

@Controller
public class StoreController {
    @Autowired
    ReservationService reservationService;
    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/testPost")
    public String testPost(Model model, @ModelAttribute("reservation") Reservation reservation){
        model.addAttribute("reservation", new Reservation());
        return "store_layout.html";
    }

    @RequestMapping(value = "/new-order")
    public ModelAndView newOrder(@ModelAttribute("newOrder") Order order){
        String orderStatus = orderService.newOrder(order);
        System.out.println("Order status "+orderStatus);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/home");
        return mav;
    }


    @RequestMapping(value = "/store-orders")
    public ModelAndView storeOrders(){
        ModelAndView mav = new ModelAndView("order.html");

        return mav;
    }

    @RequestMapping(value = "/records")
    public String records(){
        return "order.html";
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
