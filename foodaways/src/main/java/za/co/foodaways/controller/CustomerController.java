package za.co.foodaways.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import za.co.foodaways.dto.CartDto;
import za.co.foodaways.dto.OrderDto;
import za.co.foodaways.dto.ProductDto;
import za.co.foodaways.mapper.DtoMapper;
import za.co.foodaways.model.*;
import za.co.foodaways.service.OrderService;
import za.co.foodaways.service.ProductsService;
import za.co.foodaways.service.StoreUserService;
import java.util.*;

@Controller
@RequestMapping("/in")
public class CustomerController {
    OrderService orderService;
    ProductsService productsService;
    StoreUserService userService;
    DtoMapper dtoMapper;
    SimpMessagingTemplate simpMessagingTemplate;

    public CustomerController(OrderService orderService, ProductsService productsService,
                              StoreUserService userService,
                              DtoMapper dtoMapper, SimpMessagingTemplate simpMessagingTemplate){
        this.orderService = orderService;
        this.productsService = productsService;
        this.userService = userService;
        this.dtoMapper = dtoMapper;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    //Storefront on login
    @GetMapping(value = "/foodaways")
    public String customerLoginPage(Model model, HttpSession session, Authentication authentication){
        StoreUser userPerson = userService.getUserByEmail(authentication.getName());
        model.addAttribute("roles", authentication.getAuthorities().toString());
        session.setAttribute("customerCart", new Cart());
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

    @PostMapping(value = "/place-order")
    public ResponseEntity<Map<String, String>> postOrder(@RequestBody OrderDto orderProducts, HttpSession session){
        StoreUser user = (StoreUser) session.getAttribute("loggedInUser");
        SocketOrderDto order = userService.placeCustomerOrder(orderProducts, user);
        Map<String, String> response = new HashMap<>();
        if(order != null){
            response.put("message", order.getOrderStatus());
            response.put("order_id", String.valueOf(order.getOrderId()));
            String destination = "/store-manager/new-orders/"+orderProducts.storeId;
            // Broadcast order to the Store.
            simpMessagingTemplate.convertAndSend(destination, order);
            return ResponseEntity.ok(response);
        }
        response.put("message", OrderStatus.Status.ERROR_PLACING_ORDER.name());
        response.put("order_id", null);
        return ResponseEntity.ok(response);
    }


    //View my orders
    @GetMapping(value = "/orders/{pageNum}")
    public ModelAndView viewMyOrders(HttpSession session,
                                     @PathVariable("pageNum") int pageNum,
                                     @RequestParam("sortField") String sortField,
                                     @RequestParam(value = "success", required = false) String success,
                                     Model model){
        StoreUser user = (StoreUser) session.getAttribute("loggedInUser");
        ModelAndView mav = new ModelAndView("customer_orders.html");
        if(success != null){
            mav.addObject("order_success_toast","Order placed successfully");
        }
        Page<Order> customerOrders = orderService.getCustomerOrdersById(user.getUserId(), pageNum,sortField);
        model.addAttribute("current_page", pageNum);
        model.addAttribute("totalPages", customerOrders.getTotalPages());
        model.addAttribute("totalPgs", customerOrders.getTotalElements());
        model.addAttribute("sortField", sortField);
        mav.addObject("customerOrders", customerOrders.getContent());
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

    @GetMapping(value = "/add-product-to-cart/{productId}")
    public void addProductToCart(HttpSession session, @PathVariable("productId") int productId){
        Cart customerCart = (Cart)session.getAttribute("customerCart");
        if(customerCart.cartItems.containsKey(productId)){
            ProductDto p = customerCart.cartItems.get(productId);
            p.productCount = p.productCount +1;
            customerCart.cartItems.replace(productId,p);
        }else {
            Product product = productsService.getProductById(productId);
            double rating = product.getReviews().stream().mapToDouble(Review::getRating).sum();
            customerCart.cartItems.put(productId,new ProductDto(productId,
                    product.getMenuItems(), product.getProductName(), "",
                    product.getProductImagePath(), product.getProductCategory(),
                    product.getProductPrice(), rating,
                    1, product.getStoreId()));
        }
        ArrayList<ProductDto> productDtos = new ArrayList<>(customerCart.cartItems.values());
        CartDto cartDto = new CartDto(productDtos, customerCart.getCartTotal());
        session.setAttribute("customerCart", cartDto);
        // send event to update cart count by Js.
    }
}
