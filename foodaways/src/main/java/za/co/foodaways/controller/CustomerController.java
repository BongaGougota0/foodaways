package za.co.foodaways.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import za.co.foodaways.model.*;
import za.co.foodaways.service.OrderService;
import za.co.foodaways.service.ProductsService;
import za.co.foodaways.service.StoreUserService;
import java.util.ArrayList;
import java.util.Optional;

@Controller
public class CustomerController {
    OrderService orderService;
    ProductsService productsService;
    StoreUserService userService;

    public CustomerController(OrderService orderService, ProductsService productsService,
                              StoreUserService userService){
        this.orderService = orderService;
        this.productsService = productsService;
        this.userService = userService;
    }

    //Storefront on login
    @RequestMapping(value = "/in/foodaways", method = {RequestMethod.GET})
    public String customerLoginPage(Model model, HttpSession session, Authentication authentication){
        StoreUser userPerson = userService.getUserByEmail(authentication.getName());
        ArrayList<Product> products = productsService.getAllProducts();
        model.addAttribute("roles", authentication.getAuthorities().toString());
        model.addAttribute("specialProducts", products);
        session.setAttribute("loggedInUser", userPerson);
        return "index.html";
    }

    //Place order
    @RequestMapping(value = "/place-order", method = {RequestMethod.POST})
    public ModelAndView placeOrder(@RequestBody Order order, HttpSession session){
        StoreUser user = (StoreUser)session.getAttribute("loggedInUser");
        Optional<Integer> firstProduct = order.orderItems.stream().map(Product::getStoreId).findFirst();
        boolean idsTheSame = order.orderItems.stream()
                .allMatch( product -> product.getStoreId() == firstProduct.orElse(-1));
        if(idsTheSame){
            order.setUser(user);
            String orderStatus = orderService.customerNewOrder(order, user.getUserId());
            System.out.println("Order created \n Order status "+orderStatus);
        }
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/home");
        return mav;
    }

    //View my orders
    @RequestMapping(value = "/my-orders")
    public ModelAndView viewMyOrders(HttpSession session){
        StoreUser user = (StoreUser) session.getAttribute("loggedInUser");
        ModelAndView mav = new ModelAndView();
        ArrayList<Order> customerOrders = orderService.getCustomerOrdersById(user.getUserId());
        mav.addObject("customerOrders", customerOrders);
        return mav;
    }

    //Cancel order
    @PostMapping(value = "/cancel-order")
    public String cancelOrder(@RequestParam("orderId") int orderId){
        orderService.updateOrder(orderId, "CANCEL");
        return "redirect:/my-orders";
    }

    //Review Order service
    @PostMapping(value = "/submit-review")
    public String submitReview(@RequestBody Review review){
        userService.submitReview(review);
        return "redirect:/home";
    }
}
