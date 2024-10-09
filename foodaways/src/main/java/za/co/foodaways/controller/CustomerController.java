package za.co.foodaways.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import za.co.foodaways.model.Product;
import za.co.foodaways.model.StoreUser;
import za.co.foodaways.service.OrderService;
import za.co.foodaways.service.ProductsService;
import za.co.foodaways.service.StoreUserService;
import java.util.ArrayList;

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

    @GetMapping(value = "/product-view/{productId}")
    public String productView(Model model, @PathVariable("productId") int productId){
        Product product = productsService.getProductById(productId);
        model.addAttribute("product", product);
        return "productDetail.html";
    }

    //Place order
    @PostMapping(value = "/place-order")
    public ModelAndView placeOrder(){
        ModelAndView mav = new ModelAndView();
        return mav;
    }

    //View my orders
    @RequestMapping(value = "/my-orders")
    public ModelAndView viewMyOrders(){
        ModelAndView mav = new ModelAndView();
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
    public ModelAndView submitView(){
        ModelAndView mav = new ModelAndView();
        return mav;
    }
}
