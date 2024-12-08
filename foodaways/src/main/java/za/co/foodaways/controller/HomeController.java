package za.co.foodaways.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import za.co.foodaways.mapper.DtoMapper;
import za.co.foodaways.model.Product;
import za.co.foodaways.model.Reservation;
import za.co.foodaways.repository.StoreUserRepository;
import za.co.foodaways.service.ProductsService;
import za.co.foodaways.service.ReservationService;

import java.util.ArrayList;


@Controller
@RequestMapping(value = {"","foodaways"})
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

    @RequestMapping(value = "/", method = {RequestMethod.GET})
    public String home(Model model){
        if(productsService.getProductsForMenuDisplay().get("Lunch") != null){
            model.addAttribute("lunchList", productsService.getProductsForMenuDisplay().get("Lunch")
                    .stream().map(dtoMapper::toDto).limit(10));
        }else {
            model.addAttribute("lunchList",new ArrayList<>());
        }
        if(productsService.getProductsForMenuDisplay().get("Dinner") != null){
            model.addAttribute("dinnerList", productsService.getProductsForMenuDisplay().get("Dinner")
                    .stream().map(dtoMapper::toDto).limit(5));
        }  else {
            model.addAttribute("dinnerList",new ArrayList<>());
        }
        if(productsService.getProductsForMenuDisplay().get("Breakfast") != null){
            model.addAttribute("breakfastList", productsService.getProductsForMenuDisplay().get("Breakfast")
                    .stream().map(dtoMapper::toDto).limit(5));
        }else {
            model.addAttribute("breakfastList",new ArrayList<>());
        }
        return "index.html";
    }

    @GetMapping(value = "/about")
    public String about(){
        return "about.html";
    }

    @GetMapping(value = "/menu")
    public String menu(Model model){
        if(productsService.getProductsForMenuDisplay().get("Lunch") != null){
            model.addAttribute("lunchList", productsService.getProductsForMenuDisplay().get("Lunch")
                    .stream().map(dtoMapper::toDto).limit(15));
        }else {
            model.addAttribute("lunchList",new ArrayList<>());
        }
        if(productsService.getProductsForMenuDisplay().get("Dinner") != null){
            model.addAttribute("dinnerList", productsService.getProductsForMenuDisplay().get("Dinner")
                    .stream().map(dtoMapper::toDto).limit(15));
        }  else {
            model.addAttribute("dinnerList",new ArrayList<>());
        }
        if(productsService.getProductsForMenuDisplay().get("Breakfast") != null){
            model.addAttribute("breakfastList", productsService.getProductsForMenuDisplay().get("Breakfast")
                    .stream().map(dtoMapper::toDto).limit(20));
        }else {
            model.addAttribute("breakfastList",new ArrayList<>());
        }
        return "menu.html";
    }

    @GetMapping(value = "/contact")
    public String contact(){
        return "contact.html";
    }

    @GetMapping(value = "/best-rating")
    public String bestSellers(@RequestParam("productCategory") String rating, Model model){
        model.addAttribute("product_filter", "Best Rated");
        model.addAttribute("productList", productsService.getProductsByBestRating(rating)
                .stream().map(dtoMapper::toDto));
        return "products_by_category.html";
    }

    @GetMapping(value = "/products-rated")
    public String bestSellers(@RequestParam("rating") double rating, Model model){
        model.addAttribute("product_filter", "Best Rated");
        model.addAttribute("productList", productsService.getProductsByRatingEqualToAndGreater((int)rating)
                .stream().map(dtoMapper::toDto));
        return "products_by_category.html";
    }

    @GetMapping(value = "/products-by-category")
    public String productByCategory(@RequestParam("category") String category, Model model){
        model.addAttribute("productList",productsService.getProductsByCategory(category).stream().map(dtoMapper::toDto));
        model.addAttribute("category_name", category);
        return "products_by_category.html";
    }

    @PostMapping(value = "/createReservation")
    public ModelAndView createReservation(@ModelAttribute("reservation")Reservation reservation){
        reservationService.addReservation(reservation);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:foodaways/home");
        return mav;
    }

    @GetMapping(value = "/product-view/{productId}")
    public String productView(Model model, @PathVariable("productId") int productId){
        Product product = productsService.getProductById(productId);
        model.addAttribute("product", product);
        return "product_detail.html";
    }
}
