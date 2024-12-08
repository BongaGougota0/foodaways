package za.co.foodaways.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import za.co.foodaways.model.Store;
import za.co.foodaways.model.StoreUser;
import za.co.foodaways.service.AdminService;
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
        mav.addObject("newStoreObj", new Store()); // if admin clicks add new store.
        Page<Store> pageOfStores = adminservice.getStoresSortByStoreName(pageNum, Optional.ofNullable(sortField));
        List<Store> listOfStores = pageOfStores.getContent();
        int totalPages = pageOfStores.getTotalPages();
        long totalElements = pageOfStores.getTotalElements();
        model.addAttribute("current_page", pageNum);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalPgs", totalElements);
        model.addAttribute("sortField", sortField);
        mav.addObject("listOfStores", listOfStores);
        return mav;
    }

    @RequestMapping(value = "/create-store-manager/", method = {RequestMethod.POST, RequestMethod.GET})
    public String createAdminUser(@RequestBody StoreUser storeUser){
        StoreUser user = adminservice.createUnmappedStoreOwner(storeUser);
        return "";
    }

    @RequestMapping(value = "/add-store/", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView addStore(Model model,
                                 @RequestParam(value = "success", required = false) String success,
                                 @RequestParam(value = "error", required = false) String error){
        if(error != null){
            model.addAttribute("error", "An error occurred when adding a store object.");
        }
        if(success != null){
            model.addAttribute("success", "Store successfully added to foodaways.");
        }
        ModelAndView mav = new ModelAndView("admin_add_store.html");
        mav.addObject("newStoreObj", new Store());
        return mav;
    }

    @PostMapping(value = "/save-new-store")
    public String saveNewStore(@ModelAttribute("newStoreObj") Store newStore){
        Store createdStore = adminservice.createNewStore(newStore);
        if(createdStore != null){
            return "redirect:/foodaways-admin/add-store/?success=success";
        }
        return "redirect:/foodaways-admin/add-store/?error=error";
    }

}
