package za.co.foodaways.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import za.co.foodaways.model.Order;
import za.co.foodaways.model.Product;
import za.co.foodaways.model.Store;
import za.co.foodaways.model.StoreUser;
import za.co.foodaways.service.AdminService;
import za.co.foodaways.service.StoreUserService;

@Controller
//@RequestMapping(value = "admin")
public class AdminController {

    private final AdminService adminservice;
    private final StoreUserService storeUserService;
    public AdminController(AdminService adminservice, StoreUserService storeUserService){
        this.adminservice = adminservice;
        this.storeUserService = storeUserService;
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

    @RequestMapping(value = "/add-product/{storeId}", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<String> addProductByStoreId(@RequestBody Product newProduct,
                                                      @PathVariable("storeId") int storeId){
        adminservice.addNewProductByStoreId(newProduct, storeId);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
    @RequestMapping(value = "/create-order/{storeId}/{userId}", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<String> createOrder(@RequestBody Order order,
                                              @PathVariable("storeId") int storeId,
                                              @PathVariable("userId") int userId){
        adminservice.addOrderByStoreIdAndUserId(order, storeId, userId);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
}
