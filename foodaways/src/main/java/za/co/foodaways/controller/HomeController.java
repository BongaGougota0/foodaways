package za.co.foodaways.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import za.co.foodaways.dto.ProductDto;
import za.co.foodaways.mapper.DtoMapper;
import za.co.foodaways.model.Product;
import za.co.foodaways.model.Reservation;
import za.co.foodaways.model.StoreUser;
import za.co.foodaways.repository.StoreUserRepository;
import za.co.foodaways.service.ProductsService;
import za.co.foodaways.service.ReservationService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    public ReservationService reservationService;
    StoreUserRepository storeUserRepository;
    ProductsService productsService;
    DtoMapper dtoMapper;

    public HomeController(ProductsService service, StoreUserRepository userRepository,
                          ReservationService reservationService, DtoMapper dtoMapper){
        this.storeUserRepository = userRepository;
        this.productsService = service;
        this.reservationService = reservationService;
        this.dtoMapper = dtoMapper;
    }

    @RequestMapping(value = {"/index"}, method = {RequestMethod.GET})
    public String home(Model model){
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("dinner", productsService.getAllProducts()
                .stream().map(dtoMapper::toDto).collect(Collectors.toList()));
        return "index.html";
    }

    @RequestMapping(value = "/home", method = {RequestMethod.GET})
    public String loggedInHome(Model model){
        model.addAttribute("lunchList", productsService.getProductsForMenuDisplay().get("Lunch").stream().map(dtoMapper::toDto));
        model.addAttribute("dinner", productsService.getProductsForMenuDisplay().get("Dinner").stream().map(dtoMapper::toDto));
        model.addAttribute("breakfast", productsService.getProductsForMenuDisplay().get("Breakfast").stream().map(dtoMapper::toDto));
        return "index.html";
    }

    @RequestMapping(value = "/about")
    public String about(){
        return "about.html";
    }

    @RequestMapping(value = "/menu")
    public String menu(Model model){
        model.addAttribute("breakfastProducts", productsService.getProductsForMenuDisplay().get("Breakfast").stream().map(dtoMapper::toDto));
        model.addAttribute("dinnerProducts", productsService.getProductsForMenuDisplay().get("Dinner").stream().map(dtoMapper::toDto));
        model.addAttribute("lunchProducts", productsService.getProductsForMenuDisplay().get("Lunch").stream().map(dtoMapper::toDto));
        return "menu.html";
    }

    @RequestMapping(value = "/contact")
    public String contact(){
        return "contact.html";
    }

    @RequestMapping(value = "/best-sellers")
    public String bestSellers(){
        return "index.html";
    }

    @RequestMapping(value = "/news")
    public String news(){
        return "news.html";
    }

    @PostMapping(value = "/createReservation")
    public ModelAndView createReservation(@ModelAttribute("reservation")Reservation reservation){
        reservationService.addReservation(reservation);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/home");
        return mav;
    }

    @GetMapping(value = "/product-view/{productId}")
    public String productView(Model model, @PathVariable("productId") int productId){
        Product product = productsService.getProductById(productId);
        model.addAttribute("product", product);
        return "productDetail.html";
    }
}
