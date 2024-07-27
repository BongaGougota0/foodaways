package za.co.foodaways.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import za.co.foodaways.model.Order;
import za.co.foodaways.model.Product;
import za.co.foodaways.service.OrderService;
import java.util.Optional;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
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
