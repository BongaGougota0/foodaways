package za.co.foodaways.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import za.co.foodaways.model.Reservation;
import za.co.foodaways.service.ReservationService;

@Controller
public class StoreController {
    @Autowired
    ReservationService reservationService;

    @RequestMapping(value = "/testPost")
    public String testPost(Model model, @ModelAttribute("reservation") Reservation reservation){
        model.addAttribute("reservation", new Reservation());
        return "store_layout.html";
    }


    @RequestMapping(value = "/store-orders")
    public ModelAndView registerStore(){
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
