package za.co.foodaways.controller;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import za.co.foodaways.model.Order;
import za.co.foodaways.model.Product;
import za.co.foodaways.model.Reservation;
import za.co.foodaways.model.StoreUser;
import za.co.foodaways.repository.StoreUserRepository;
import za.co.foodaways.service.OrderService;
import za.co.foodaways.service.ProductsService;
import za.co.foodaways.service.ReservationService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class StoreController {

    ReservationService reservationService;
    StoreUserRepository storeUserRepository;
    OrderService orderService;
    ProductsService productsService;
    @Autowired
    public StoreController(ReservationService service, OrderService orderService,
                           StoreUserRepository userRepository, ProductsService productsService){
        this.reservationService = service;
        this.orderService = orderService;
        this.storeUserRepository = userRepository;
        this.productsService = productsService;
    }

    @RequestMapping(value = "/store-manager", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView storeManagerHome(Model model, Authentication authentication, HttpSession session){
        StoreUser userPerson = storeUserRepository.findByEmail(authentication.getName());
        model.addAttribute("roles", authentication.getAuthorities().toString());
        ModelAndView mav = new ModelAndView("store_manager.html");
        mav.addObject("newProduct", new Product());
        mav.addObject("products", productsService.getStoreProductsByManagerId(userPerson.getUserId()));
        session.setAttribute("loggedInUser", userPerson);
        return mav;
    }

    @RequestMapping(value = "/testPost")
    public String testPost(Model model, @ModelAttribute("reservation") Reservation reservation){
        model.addAttribute("reservation", new Reservation());
        return "store_layout.html";
    }

    @RequestMapping(value = "/orders")
    public ModelAndView storeOrders(){
        ModelAndView mav = new ModelAndView("order.html");
        return mav;
    }

    @PostMapping(value = "/store-products-menu")
    public ModelAndView addToMenu(){
        ModelAndView mav = new ModelAndView("storecontroller.html");
        mav.addObject("newProduct", new Order());
        return mav;
    }

    @RequestMapping(value = "/store-manager/add-new-product", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView addProductToStore(@ModelAttribute("newProduct")Product newProduct,
                                          MultipartFile productImage, Authentication authentication){
        ModelAndView mav = new ModelAndView("redirect:/store-manager");
        StoreUser userPerson = storeUserRepository.findByEmail(authentication.getName());
        String uploadDirectory = "src/main/resources/static/assets/images";
        if(!productImage.isEmpty()){
            String fileName = productImage.getOriginalFilename();
            try {
                File newDir = new File(uploadDirectory);
                if(!newDir.exists()){newDir.mkdirs();}
                byte[] fileBytes = productImage.getBytes();
                Path path = Paths.get(uploadDirectory, fileName);
                Files.write(path, fileBytes);
                newProduct.setProductImagePath(String.valueOf(path));
                newProduct.setProductName(fileName);
                productsService.adminAddNewProduct(newProduct, userPerson.getUserId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else if(productImage == null || productImage.isEmpty()) {
            productsService.adminAddNewProduct(newProduct, userPerson.getUserId());
        }
        return mav;
    }

    @RequestMapping(value = "/records")
    public String records(){
        return "order.html";
    }

    @RequestMapping(value = "/store-manager/update-product", method = {RequestMethod.POST})
    public String updateProductDetails(@ModelAttribute("updateProduct") Product updateProduct, int productId){
        productsService.adminUpdateProduct(updateProduct, productId);
        return "redirect:/store-manager";
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
