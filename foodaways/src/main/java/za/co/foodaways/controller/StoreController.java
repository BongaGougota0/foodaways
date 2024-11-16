package za.co.foodaways.controller;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import reactor.core.publisher.Flux;
import za.co.foodaways.dto.OrderDto;
import za.co.foodaways.mapper.OrderDtoMapper;
import za.co.foodaways.model.*;
import za.co.foodaways.repository.StoreUserRepository;
import za.co.foodaways.service.*;
import za.co.foodaways.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("store-manager")
public class StoreController {

    ReservationService reservationService;
    StoreUserRepository storeUserRepository;
    OrderService orderService;
    ProductsService productsService;
    StoreUserService storeUserService;
    StoreManagerService storeManagerService;
    OrderDtoMapper orderDtoMapper;
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    public StoreController(ReservationService service, OrderService orderService,
                           StoreUserRepository userRepository, ProductsService productsService,
                           StoreUserService storeUserService, StoreManagerService storeManagerService,
                           OrderDtoMapper orderDtoMapper, SimpMessagingTemplate simpMessagingTemplate){
        this.reservationService = service;
        this.orderService = orderService;
        this.storeUserRepository = userRepository;
        this.productsService = productsService;
        this.storeUserService = storeUserService;
        this.storeManagerService = storeManagerService;
        this.orderDtoMapper = orderDtoMapper;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @RequestMapping(value = "/home")
    public ModelAndView storeHome(Model model, HttpSession session, Authentication authentication){
        ModelAndView mav = new ModelAndView("store_manager.html");
        mav.addObject("newProduct", new Product());
        mav.addObject("products", new ArrayList<Product>());
        mav.addObject("productCategories", Arrays.asList("Lunch", "Dinner", "Breakfast"));
        model.addAttribute("roles", authentication.getAuthorities().toString());
        StoreUser userPerson = storeUserRepository.findByEmail(authentication.getName());
        model.addAttribute("storeId", storeUserService.getManagedStoreByAdminId(userPerson.getUserId()).getStoreId());
        session.setAttribute("managedStore", storeUserService.getManagedStoreByAdminId(userPerson.getUserId()));
        session.setAttribute("loggedInUser", userPerson);
        return mav;
    }

    @RequestMapping(value = "/products", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView storeProducts(Model model, Authentication authentication, HttpSession session){
        StoreUser userPerson = (StoreUser) session.getAttribute("loggedInUser");
        model.addAttribute("roles", authentication.getAuthorities().toString());
        ModelAndView mav = new ModelAndView("store_manager.html");
        mav.addObject("newProduct", new Product());
        mav.addObject("products", storeManagerService.getStoreProductsByManagerId(userPerson.getUserId()));
        mav.addObject("productCategories", Arrays.asList("Lunch", "Dinner", "Breakfast"));
        session.setAttribute("managedStore", storeUserService.getManagedStoreByAdminId(userPerson.getUserId()));
        return mav;
    }

    @RequestMapping(value = "/orders", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView storeOrders(Model model, HttpSession session){
        ModelAndView mav = new ModelAndView("store_orders.html");
        Store store = (Store) session.getAttribute("managedStore");
        model.addAttribute("storeId", store.getStoreId());
        model.addAttribute("storeName", store.getStoreName());
        mav.addObject("storeOrders", orderService.getStoreOrdersByStatus(store.getStoreId()));
        return mav;
    }


    @GetMapping(path = "/new-orders/{storeId}/", produces = "text/event-stream")
    public void getStoreOrder(@PathVariable("storeId") int storeId){

    }

    @MessageMapping("/place.order")
    @SendTo("/store-manager/orders")
    public Order placeOrder(@Payload Order order){
        int storeId = order.getOrderItems().stream().findFirst().get().getStoreId();
        String destination = "/store-manager/orders/"+storeId;
        simpMessagingTemplate.convertAndSend(destination,order);
        return order;
    }

    @MessageMapping("/activate-store")
    @SendTo("/store-manager/new-orders/")
    public Order activateStore(@Payload Order order, SimpMessageHeaderAccessor headerAccessor){
        headerAccessor.getSessionAttributes().put("status", "ONLINE");
        return order;
    }

    @RequestMapping(value = "/completed-orders", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView storeCompletedOrders(){
        ModelAndView mav = new ModelAndView("store_orders.html");
        mav.addObject("storeOrders", orderService.getStoreOrdersByStatus(1));
        return mav;
    }

    @RequestMapping(value = "/delivered-orders", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView storeDeliveredOrders(){
        ModelAndView mav = new ModelAndView("store_orders.html");
        mav.addObject("storeOrders", orderService.getStoreOrdersByStatus(1));
        return mav;
    }

    @RequestMapping(value = "/store-manager/sales-details", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView storeSalesDetails(){
        ModelAndView mav = new ModelAndView("store_orders.html");
        mav.addObject("storeOrders", orderService.getStoreOrdersByStatus(1));
        return mav;
    }

    @PostMapping(value = "/store-products-menu")
    public ModelAndView addToMenu(){
        ModelAndView mav = new ModelAndView("storecontroller.html");
        mav.addObject("newProduct", new Order());
        return mav;
    }

    @RequestMapping(value = "/add-new-product", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView addProductToStore(@ModelAttribute("newProduct")Product newProduct,
                                          MultipartFile productImage, Authentication authentication, HttpSession session){
        ModelAndView mav = new ModelAndView("redirect:/store-manager/home");
        StoreUser userPerson = storeUserRepository.findByEmail(authentication.getName());
        Store store = (Store)session.getAttribute("managedStore");
        storeManagerService.addProduct(newProduct, productImage, store, userPerson.getUserId());
        return mav;
    }

    @RequestMapping(value = "/delete-product/{productId}")
    public String deleteProductById(@PathVariable("productId")int productId){
        storeManagerService.deleteProductById(productId);
        return "redirect:/store-manager/home";
    }

    @RequestMapping(value = "/product-edit-page/{productId}")
    public String updateProduct(Model model, @PathVariable("productId") int productId){
        Product toUpdateProduct = productsService.getProductById(productId);
        model.addAttribute("productToUpdate", toUpdateProduct);
        model.addAttribute("productCategories", Arrays.asList("Lunch", "Dinner", "Breakfast"));
        return "product_edit.html";
    }

    @RequestMapping(value = "/update-product", method = {RequestMethod.POST})
    public String updateProductDetails(@RequestParam("productId") int productId,
                                       MultipartFile productImage, @ModelAttribute("updateProduct") Product updateProduct){
        if(!productImage.isEmpty()){
            String newImageName = Utils.writeImage(productImage);
            updateProduct.setImageOfProduct(newImageName);
        }
        storeManagerService.updateProduct(updateProduct, productId);
        return "redirect:/store-manager/home";
    }

    @RequestMapping(value = "/accept")
    public String acceptOrder(@RequestParam("orderId") int orderId){
        orderService.acceptOrderUpdate(orderId);
        return "redirect:/store-manager/home";
    }

    @RequestMapping(value = "/decline")
    public String declineOrder(@RequestParam("orderId") int orderId, @RequestParam("declineReason") String declineReason){
        orderService.declineOrderUpdate(orderId, declineReason);
        return "redirect:/store-manager/home";
    }
}
