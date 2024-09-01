package za.co.foodaways.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import za.co.foodaways.model.Order;
import za.co.foodaways.model.Product;
import za.co.foodaways.model.StoreUser;
import za.co.foodaways.repository.StoreUserRepository;
import za.co.foodaways.service.OrderService;
import za.co.foodaways.service.StoreUserService;

import java.util.List;
import java.util.Optional;

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
        ModelAndView mav = new ModelAndView("order.html");
        mav.addObject("storeOrders", orderList);
        return mav;
    }

    @RequestMapping(value = "/new-order")
    public ModelAndView newOrder(@ModelAttribute("newOrder") Order order){
        // First check all items in the cart belong to the same store (same ID store)
        Optional<Integer> firstProduct = order.orderItems.stream().map(Product::getStoreId).findFirst();
        boolean idsTheSame = order.orderItems.stream()
                .allMatch( product -> product.getStoreId() == firstProduct.orElse(-1));
        if(idsTheSame){
            String orderStatus = orderService.newOrder(order);
            System.out.println("Order created \n Order status "+orderStatus);
        }
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/home");
        return mav;
    }
}
