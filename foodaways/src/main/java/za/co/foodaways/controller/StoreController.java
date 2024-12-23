package za.co.foodaways.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
import za.co.foodaways.dto.OrderDto;
import za.co.foodaways.dto.OrderProcess;
import za.co.foodaways.model.*;
import za.co.foodaways.service.*;
import za.co.foodaways.utils.Utils;

import java.util.*;


@Controller
@RequestMapping("store-manager")
public class StoreController {

    StoreUserService storeUserService;
    StoreManagerService storeManagerService;
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public StoreController(StoreUserService storeUserService, StoreManagerService storeManagerService,
                           SimpMessagingTemplate simpMessagingTemplate){
        this.storeUserService = storeUserService;
        this.storeManagerService = storeManagerService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @RequestMapping(value = "/home/{pageNum}")
    public ModelAndView storeHome(Model model, @PathVariable(value = "pageNum") int pageNum,
                                  @RequestParam("sortField") String sortField,
                                  HttpSession session, Authentication authentication){
        ModelAndView mav = new ModelAndView("store_manager.html");
        mav.addObject("newProduct", new Product());
        mav.addObject("productCategories", Arrays.asList("Lunch", "Dinner", "Breakfast"));
        model.addAttribute("roles", authentication.getAuthorities().toString());
        StoreUser userPerson = storeUserService.findUserByEmail(authentication.getName());
        Store managedStore = storeManagerService.getManagedStoreById(userPerson.getUserId());
        Page<Product> pageOfProducts = storeManagerService.getStoreProductsById(managedStore.getStoreId(), pageNum, sortField);
        model.addAttribute("totalOrderElements", pageOfProducts.getTotalElements());
        model.addAttribute("totalPages", pageOfProducts.getTotalPages());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortField", sortField);
        mav.addObject("products", pageOfProducts.getContent());
        model.addAttribute("storeId", managedStore.getStoreId());
        session.setAttribute("managedStore", managedStore);
        session.setAttribute("loggedInUser", userPerson);
        return mav;
    }

    @RequestMapping(value = "/products/{pageNum}", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView storeProducts(Model model, Authentication authentication,
                                      @RequestParam(value = "sortField") String sortField,
                                      @PathVariable(value = "pageNum") int pageNum, HttpSession session){
        StoreUser userPerson = (StoreUser) session.getAttribute("loggedInUser");
        model.addAttribute("roles", authentication.getAuthorities().toString());
        ModelAndView mav = new ModelAndView("store_products.html");
        Store store  = (Store) session.getAttribute("managedStore");
        Page<Product> pageOfProducts = storeManagerService.getStoreProductsById(store.getStoreId(), pageNum,sortField);
        model.addAttribute("totalOrderElements", pageOfProducts.getTotalElements());
        model.addAttribute("totalPages", pageOfProducts.getTotalPages());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortField", sortField);
        mav.addObject("products", pageOfProducts.getContent());
        mav.addObject("newProduct", new Product());
        mav.addObject("productCategories", Arrays.asList("Lunch", "Dinner", "Breakfast"));
        return mav;
    }

    @RequestMapping(value = "/orders/{pageNum}", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView storeOrders(Model model,
                                    @RequestParam(value = "sortField") String sortField,
                                    @PathVariable(value = "pageNum") int pageNum,
                                    HttpSession session){
        ModelAndView mav = new ModelAndView("store_orders.html");
        mav.addObject("newProduct", new Product());
        Store store = (Store) session.getAttribute("managedStore");
        model.addAttribute("storeId", store.getStoreId());
        model.addAttribute("storeName", store.getStoreName());
        Page<Order> orders = storeManagerService.getAllStoreOrders(store.getStoreId(), pageNum, sortField);
        List<Order> listOfOrders = orders.getContent();
        model.addAttribute("totalOrderElements", orders.getTotalElements());
        model.addAttribute("totalPages", orders.getTotalPages());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortField", sortField);
        mav.addObject("storeOrders", listOfOrders);
        return mav;
    }


    @SendTo("/store-manager/new-orders/")
    public Order placeOrder(@Payload OrderDto orderDto, HttpSession session){
        int storeId = orderDto.storeId;
        Store store = (Store)session.getAttribute("managedStore");
        Order order = new Order();
        order.setOrderStatus("ORDER_PLACED");
        order.setStore(store);
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

    @RequestMapping(value = "/completed-orders/{pageNum}", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView storeCompletedOrders(Model model,
                                             @RequestParam(value = "sortField") String sortField,
                                             @PathVariable(value = "pageNum") int pageNum,HttpSession session){
        ModelAndView mav = new ModelAndView("store_orders.html");
        Store store = (Store)session.getAttribute("managedStore");
        Page<Order> orderPage = storeManagerService.getInProgressOrders(store.getStoreId(), pageNum, sortField);
        model.addAttribute("totalOrderElements", orderPage.getTotalElements());
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortField", sortField);
        mav.addObject("storeOrders", orderPage.getContent());
        mav.addObject("newProduct", new Product());
        return mav;
    }

    @RequestMapping(value = "/inprogress-orders/{pageNum}", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView inProgressOrders(Model model,
                                             @RequestParam(value = "sortField") String sortField,
                                             @PathVariable(value = "pageNum") int pageNum,
                                         HttpSession session){
        ModelAndView mav = new ModelAndView("store_progress_orders.html");
        Store store = (Store)session.getAttribute("managedStore");
        Page<Order> orderPage = storeManagerService.getInProgressOrders(store.getStoreId(), pageNum);
        model.addAttribute("totalOrderElements", orderPage.getTotalElements());
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortField", sortField);
        mav.addObject("storeOrders", orderPage.getContent());
        mav.addObject("newProduct", new Product());
        return mav;
    }

    @RequestMapping(value = "/delivered-orders/{pageNum}", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView storeDeliveredOrders(Model model,
                                             @RequestParam(value = "sortField") String sortField,
                                             @PathVariable(value = "pageNum") int pageNum, HttpSession session){
        ModelAndView mav = new ModelAndView("store_delivered.html");
        Store store = (Store)session.getAttribute("managedStore");
        Page<Order> orderPage = storeManagerService.getCompletedOrders(store.getStoreId(),pageNum, sortField);
        model.addAttribute("totalOrderElements", orderPage.getTotalElements());
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortField", sortField);
        mav.addObject("newProduct", new Product());
        mav.addObject("storeOrders", orderPage.getContent());
        return mav;
    }

    @RequestMapping(value = "/sales-details/{pageNum}", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView storeSalesDetails(Model model, @PathVariable(value = "pageNum") int pageNum,
                                          @RequestParam("sortField") String sortField, HttpSession session){
        ModelAndView mav = new ModelAndView("store_orders.html");
        mav.addObject("newProduct", new Product());
        Store store = (Store)session.getAttribute("managedStore");
        Page<Order> orderPage = storeManagerService.getCompletedOrders(store.getStoreId(),pageNum, sortField);
        model.addAttribute("totalOrderElements", orderPage.getTotalElements());
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortField", sortField);
        mav.addObject("newProduct", new Product());
        mav.addObject("completedOrders", orderPage.getContent());
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
        Store store = (Store)session.getAttribute("managedStore");
        StoreUser user = (StoreUser)session.getAttribute("loggedInUser");
        storeManagerService.addProduct(newProduct, productImage, store, user.getUserId());
        return mav;
    }

    @RequestMapping(value = "/delete-product/{productId}")
    public String deleteProductById(@PathVariable("productId")int productId){
        storeManagerService.deleteProductById(productId);
        return "redirect:/store-manager/home";
    }

    @RequestMapping(value = "/product-edit-page/{productId}")
    public String updateProduct(Model model, @PathVariable("productId") int productId){
        Product toUpdateProduct = storeManagerService.getstoreProductById(productId);
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

    @PostMapping(value = "/accept-order/")
    public ResponseEntity<Map<String,String>> acceptOrder(@RequestBody OrderProcess orderProcess){
        Order order = storeManagerService.updateOrder(Integer.parseInt(orderProcess.getOrderId()),
                orderProcess.getOrderStatus());
        Map<String, String> responseData = new HashMap<>();
        responseData.put("orderId", String.valueOf(order.getOrderId()));
        responseData.put("status", order.getOrderStatus());
        responseData.put("request", "complete");
        return ResponseEntity.ok(responseData);
    }


    @PostMapping(value = "/reject-order/")
    public ResponseEntity<Map<String, String>> declineOrder(@RequestBody OrderProcess orderProcess){
        Order order = storeManagerService.updateOrder(Integer.parseInt(orderProcess.getOrderId()),
                OrderStatusEnum.ORDER_DECLINED.name());
        Map<String, String> responseData = new HashMap<>();
        responseData.put("orderId", String.valueOf(order.getOrderId()));
        responseData.put("status", order.getOrderStatus());
        responseData.put("request", "complete");
        return ResponseEntity.ok(responseData);
    }
}
