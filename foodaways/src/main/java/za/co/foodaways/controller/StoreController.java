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
import za.co.foodaways.model.*;
import za.co.foodaways.repository.StoreUserRepository;
import za.co.foodaways.service.OrderService;
import za.co.foodaways.service.ProductsService;
import za.co.foodaways.service.ReservationService;
import za.co.foodaways.service.StoreUserService;

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
    StoreUserService storeUserService;
    @Autowired
    public StoreController(ReservationService service, OrderService orderService,
                           StoreUserRepository userRepository, ProductsService productsService, StoreUserService storeUserService){
        this.reservationService = service;
        this.orderService = orderService;
        this.storeUserRepository = userRepository;
        this.productsService = productsService;
        this.storeUserService = storeUserService;
    }

    @RequestMapping(value = "/store-manager", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView storeManagerHome(Model model, Authentication authentication, HttpSession session){
        StoreUser userPerson = storeUserRepository.findByEmail(authentication.getName());
        model.addAttribute("roles", authentication.getAuthorities().toString());
        ModelAndView mav = new ModelAndView("store_manager.html");
        mav.addObject("newProduct", new Product());
        mav.addObject("products", productsService.getStoreProductsByManagerId(userPerson.getUserId()));
        session.setAttribute("loggedInUser", userPerson);
        session.setAttribute("managedStore", storeUserService.getManagedStoreByAdminId(userPerson.getUserId()));
        return mav;
    }

    @RequestMapping(value = "/testPost")
    public String testPost(Model model, @ModelAttribute("reservation") Reservation reservation){
        model.addAttribute("reservation", new Reservation());
        return "store_layout.html";
    }

    @RequestMapping(value = "/store-manager/orders", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView storeOrders(){
        ModelAndView mav = new ModelAndView("order.html");
        mav.addObject("storeOrders", orderService.getStoreOrdersByStatus(1));
        return mav;
    }
    @RequestMapping(value = "/store-manager/completed", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView storeCompletedOrders(){
        ModelAndView mav = new ModelAndView("order.html");
        mav.addObject("storeOrders", orderService.getStoreOrdersByStatus(1));
        return mav;
    }

    @RequestMapping(value = "/store-manager/delivered", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView storeDeliveredOrders(){
        ModelAndView mav = new ModelAndView("order.html");
        mav.addObject("storeOrders", orderService.getStoreOrdersByStatus(1));
        return mav;
    }

    @RequestMapping(value = "/store-manager/sales-details", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView storeSalesDetails(){
        ModelAndView mav = new ModelAndView("order.html");
        mav.addObject("storeOrders", orderService.getStoreOrdersByStatus(1));
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
                                          MultipartFile productImage, Authentication authentication, HttpSession session){
        ModelAndView mav = new ModelAndView("redirect:/store-manager");
        System.out.println("View store id from http_session " +session.getAttribute("managedStore").toString());
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
                Store store = (Store)session.getAttribute("managedStore");
                newProduct.setImageOfProduct(fileName);
                newProduct.setStore(store);
                productsService.adminAddNewProduct(newProduct, userPerson.getUserId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else if(productImage == null || productImage.isEmpty()) {
            productsService.adminAddNewProduct(newProduct, userPerson.getUserId());
        }
        return mav;
    }

    @RequestMapping(value = "/store-manager/delete-product/{productId}")
    public String deleteProductById(@PathVariable("productId")int productId){
        productsService.deleteProductById(productId);
        return "redirect:/store-manager";
    }

    @RequestMapping(value = "/store-manager/update-product/{productId}")
    public String updateProduct(Model model, @PathVariable("productId") int productId){
        // Get product and goto product view
        Product toUpdate = productsService.getProductById(productId);
        if(toUpdate != null){
            model.addAttribute("productToUpdate", toUpdate);
            return  "redirect:/store-manager/product-edit-page/"+toUpdate.getProductId();
        }
        return "redirect:/store-manager";
    }

    @RequestMapping(value = "/store-manager/product-edit-page/{productId}")
    public ModelAndView editProductPage(Model model, @PathVariable("productId") int productId){
        ModelAndView mav = new ModelAndView("edit_product_page.html");
        mav.addObject("toUpdateProduct",model.getAttribute("productToUpdate"));
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
