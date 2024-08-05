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
import za.co.foodaways.service.MasterService;

@Controller
public class MasterController {

    private final MasterService masterService;
    @Autowired
    public MasterController(MasterService masterService){
        this.masterService = masterService;
    }

    @RequestMapping(value = "/create-admin-user/", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<String> createAdminUser(@RequestBody StoreUser storeUser){
        System.out.println("------------------------------- Rest controller called --------------------------------------");
        masterService.createUnmappedStoreOwner(storeUser);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
    @RequestMapping(value = "/add-store/{adminId}", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<String> addStore(@RequestBody Store store,
                                           @PathVariable("adminId") int adminId){
        masterService.addNewStoreWithAdmin(store, adminId);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @RequestMapping(value = "/add-store-admin/{userId}/{storeId}", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<String> addStoreAdmin(@PathVariable("userId") int userId,
                                                @PathVariable("storeId") int storeId){
        masterService.addStoreAdminByUserAndStoreId(userId, storeId);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @RequestMapping(value = "/add-product/{storeId}", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<String> addProductByStoreId(@RequestBody Product newProduct,
                                                      @PathVariable("storeId") int storeId){
        masterService.addNewProductByStoreId(newProduct, storeId);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
    @RequestMapping(value = "/create-order/{storeId}/{userId}", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<String> createOrder(@RequestBody Order order,
                                              @PathVariable("storeId") int storeId,
                                              @PathVariable("userId") int userId){
        masterService.addOrderByStoreIdAndUserId(order, storeId, userId);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
}
