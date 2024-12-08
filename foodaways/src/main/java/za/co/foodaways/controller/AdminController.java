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
import java.util.Optional;

@Controller
@RequestMapping(value = "foodaways-admin")
public class AdminController {

    private final AdminService adminservice;

    public AdminController(AdminService adminservice){
        this.adminservice = adminservice;
    }

    @RequestMapping("")
    public ModelAndView foodawaysAdminLanding(){
        ModelAndView mav = new ModelAndView("admin_home.html");
        return mav;
    }

    @RequestMapping("/stores/{pageNum}")
    public ModelAndView viewStores(Model model, @PathVariable("pageNum") int pageNum,
                                   @RequestParam("sortField") String sortField){
        ModelAndView mav = new ModelAndView("admin.html");
        Page<Store> pageOfStores = adminservice.getStoresSortByStoreName(pageNum, Optional.ofNullable(sortField));
        List<Store> listOfStores = pageOfStores.getContent();
        int totalPages = pageOfStores.getTotalPages();
        long totalElements = pageOfStores.getTotalElements();
        model.addAttribute("current_page", pageNum);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalPages", totalElements);
        model.addAttribute("sortField", sortField);
        mav.addObject("stores", listOfStores);
        return mav;
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
