package za.co.foodaways.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import za.co.foodaways.dto.StoreAllocationDto;
import za.co.foodaways.dto.StoreUserDto;
import za.co.foodaways.mapper.BaseEntityDtoMapper;
import za.co.foodaways.model.Store;
import za.co.foodaways.model.StoreUser;
import za.co.foodaways.service.AdminService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "foodaways-admin")
public class AdminController {

    private final AdminService adminservice;
    private final BaseEntityDtoMapper<StoreUserDto, StoreUser> baseEntityDtoMapper;

    public AdminController(AdminService adminservice,
                           BaseEntityDtoMapper<StoreUserDto, StoreUser> baseEntityDtoMapper){
        this.adminservice = adminservice;
        this.baseEntityDtoMapper = baseEntityDtoMapper;
    }

    @RequestMapping("")
    public ModelAndView foodawaysAdminLanding(){
        ModelAndView mav = new ModelAndView("admin_home.html");
        return mav;
    }

    @RequestMapping("/users/{pageNum}")
    public ModelAndView viewUsers(Model model, @PathVariable("pageNum") int pageNum,
                                   @RequestParam("sortField") String sortField){
        ModelAndView mav = new ModelAndView("admin_users.html");
        Page<StoreUser> pageOfUsers = adminservice.viewUsers(pageNum, sortField);
        List<StoreUserDto> listOfUsers = pageOfUsers.getContent()
                .stream().filter(user -> user.getRole().roleId == 3) //get only store admin.
                .map(baseEntityDtoMapper::getDto).collect(Collectors.toCollection(ArrayList::new));
        int totalPages = pageOfUsers.getTotalPages();
        long totalElements = pageOfUsers.getTotalElements();
        model.addAttribute("current_page", pageNum);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalPgs", totalElements);
        model.addAttribute("sortField", sortField);
        mav.addObject("listOfUsers", listOfUsers);
        return mav;
    }

    @RequestMapping("/stores/{pageNum}")
    public ModelAndView viewStores(Model model, @PathVariable("pageNum") int pageNum,
                                   @RequestParam("sortField") String sortField){
        ModelAndView mav = new ModelAndView("admin_stores.html");
        mav.addObject("newStoreObj", new Store()); // if admin clicks add new store.
        Page<Store> pageOfStores = adminservice.getStoresSortByStoreName(pageNum, Optional.ofNullable(sortField));
        int totalPages = pageOfStores.getTotalPages();
        long totalElements = pageOfStores.getTotalElements();
        model.addAttribute("current_page", pageNum);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalPgs", totalElements);
        model.addAttribute("sortField", sortField);
        mav.addObject("storeList", adminservice.getStoreWithNoAdmins());
        return mav;
    }

    @RequestMapping(value = "/stores-allocate/{storeId}", method = {RequestMethod.POST})
    public String allocateAdminToStore(Model model, @PathVariable(value = "storeId", required = false) int storeId,
                                       @RequestBody StoreAllocationDto storeAllocationDto){ //better variable could've been made. lazy
        StoreUser user = adminservice.assignUserToStoreByStoreId(storeAllocationDto);
        return "redirect:/foodaways-admin/stores/1?sortField=storeName";
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
