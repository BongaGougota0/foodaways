package za.co.foodaways.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import za.co.foodaways.model.Order;
import za.co.foodaways.model.Product;
import za.co.foodaways.model.Store;
import za.co.foodaways.model.StoreUser;
import za.co.foodaways.service.AdminService;
import za.co.foodaways.service.StoreUserService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "foodaways-admin")
public class AdminController {

    private final AdminService adminservice;
    private final StoreUserService storeUserService;
    public AdminController(AdminService adminservice, StoreUserService storeUserService){
        this.adminservice = adminservice;
        this.storeUserService = storeUserService;
    }

    @RequestMapping("/")
    public Model foodawaysAdminLanding(Model model){
        Page<Store> page = adminservice.getAllFoodawaysStores();
        int totalPages = page.getTotalPages();
        List<Store> listOfStores = page.getContent();
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("stores", listOfStores);
        return model;
    }

    @RequestMapping(value = "/create-admin-user/", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<String> createAdminUser(@RequestBody StoreUser storeUser){
        adminservice.createUnmappedStoreOwner(storeUser);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
    @RequestMapping(value = "/add-store/{adminId}", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<String> addStore(@RequestBody Store store,
                                           @PathVariable("adminId") int adminId){
        adminservice.addNewStoreWithAdmin(store, adminId);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @RequestMapping(value = "/add-store-admin/{userId}/{storeId}", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<String> addStoreAdmin(@PathVariable("userId") int userId,
                                                @PathVariable("storeId") int storeId){
        adminservice.addStoreAdminByUserAndStoreId(userId, storeId);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

}
