package za.co.foodaways.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import za.co.foodaways.dto.OrderDto;
import za.co.foodaways.mapper.DtoMapper;
import za.co.foodaways.model.*;
import za.co.foodaways.service.OrderService;
import za.co.foodaways.service.ProductsService;
import za.co.foodaways.service.StoreUserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/in")
public class CustomerController {
    OrderService orderService;
    ProductsService productsService;
    StoreUserService userService;
    DtoMapper dtoMapper;

    public CustomerController(OrderService orderService, ProductsService productsService,
                              StoreUserService userService, DtoMapper dtoMapper){
        this.orderService = orderService;
        this.productsService = productsService;
        this.userService = userService;
        this.dtoMapper = dtoMapper;
    }

    //Storefront on login
    @GetMapping(value = "/foodaways")
    public String customerLoginPage(Model model, HttpSession session, Authentication authentication){
        StoreUser userPerson = userService.getUserByEmail(authentication.getName());
        model.addAttribute("roles", authentication.getAuthorities().toString());
        session.setAttribute("loggedInUser", userPerson);

        if(productsService.getProductsForMenuDisplay().get("Lunch") != null){
            model.addAttribute("lunchList", productsService.getProductsForMenuDisplay().get("Lunch")
                    .stream().map(dtoMapper::toDto).limit(10));
        }else {
            model.addAttribute("lunchList",new ArrayList<>());
        }
        if(productsService.getProductsForMenuDisplay().get("Dinner") != null){
            model.addAttribute("dinnerList", productsService.getProductsForMenuDisplay().get("Dinner")
                    .stream().map(dtoMapper::toDto).limit(5));
        }  else {
            model.addAttribute("dinnerList",new ArrayList<>());
        }
        if(productsService.getProductsForMenuDisplay().get("Breakfast") != null){
            model.addAttribute("breakfastList", productsService.getProductsForMenuDisplay().get("Breakfast")
                    .stream().map(dtoMapper::toDto).limit(5));
        }else {
            model.addAttribute("breakfastList",new ArrayList<>());
        }
        return "index.html";
    }

    //Place order
    @PostMapping(value = "/place-order")
    public ModelAndView placeOrder(@RequestBody OrderDto order, HttpSession session){
        StoreUser user = (StoreUser)session.getAttribute("loggedInUser");
        String orderResult = orderService.customerNewOrder(order, user);
        ModelAndView mav = new ModelAndView();
        if(orderResult.equals(OrderStatus.Status.ORDER_PLACED.toString())){
            mav.setViewName("redirect:/in/my-orders?success=true");
            return mav;
        }
        mav.setViewName("redirect:/in/foodaways");
        return mav;
    }

    //View my orders
    @GetMapping(value = "/my-orders")
    public ModelAndView viewMyOrders(HttpSession session,
                                     @RequestParam(value = "success", required = false) String success){
        StoreUser user = (StoreUser) session.getAttribute("loggedInUser");
        ModelAndView mav = new ModelAndView("customer_orders.html");
        if(success != null){
            mav.addObject("order_success_toast","Order placed successfully");
        }
        ArrayList<Order> customerOrders = orderService.getCustomerOrdersById(user.getUserId());
        mav.addObject("customerOrders", customerOrders);
        return mav;
    }

    //Cancel order
    @PostMapping(value = "/cancel-order")
    public String cancelOrder(@RequestParam("orderId") int orderId){
        orderService.updateOrder(orderId, "CANCEL");
        return "redirect:/in/my-orders";
    }

    //Review Order service
    @PostMapping(value = "/submit-review")
    public String submitReview(@RequestBody Review review){
        userService.submitReview(review);
        return "redirect:/in/foodaways";
    }
}
