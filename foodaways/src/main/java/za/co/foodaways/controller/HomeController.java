package za.co.foodaways.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import za.co.foodaways.dto.ProductDto;
import za.co.foodaways.mapper.DtoMapper;
import za.co.foodaways.model.Product;
import za.co.foodaways.model.Review;
import za.co.foodaways.repository.StoreUserRepository;
import za.co.foodaways.service.ProductsService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping(value = {"","foodaways"})
public class HomeController {
    StoreUserRepository storeUserRepository;
    ProductsService productsService;
    DtoMapper dtoMapper;

    public HomeController(ProductsService service, StoreUserRepository userRepository, DtoMapper dtoMapper){
        this.storeUserRepository = userRepository;
        this.productsService = service;
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

    @GetMapping(path = "/carousel-items")
    public ResponseEntity<Map<String, ArrayList<ProductDto>>> getCarouselProducts() {
        ArrayList<ProductDto> carouselProducts = productsService.getAllProducts().stream()
                .limit(4).map(dtoMapper::toDto).collect(Collectors.toCollection(ArrayList::new));
        Map<String, ArrayList<ProductDto>> frontCarousel = new HashMap<>();
        frontCarousel.put("carousel_items", carouselProducts);
        return ResponseEntity.ok(frontCarousel);
    }

    @GetMapping(value = "/best-rating/{pageNum}")
    public ModelAndView displayProductsByRatings(Model model, @PathVariable(value = "pageNum") int pageNum,
                                                 @RequestParam("sortField") String sortField){
        ModelAndView mav = new ModelAndView("products_by_category.html");
        Page<Product> productsByRatingDesc = productsService.getProductsByBestRating(pageNum, sortField);
        ArrayList<Product> products = productsByRatingDesc.getContent().stream()
                .filter(p -> p.getReviews().stream().mapToDouble(Review::getRating).sum() > 3)
                .collect(Collectors.toCollection(ArrayList::new));
        model.addAttribute("current_page", pageNum);
        model.addAttribute("totalPages", productsByRatingDesc.getTotalPages());
        model.addAttribute("totalPgs", productsByRatingDesc.getTotalElements());
        model.addAttribute("sortField", sortField);
        mav.addObject("productList", products);
        model.addAttribute("category_name", sortField);
        model.addAttribute("productCategories", "throw_away_var");
        return mav;
    }

    @GetMapping(value = "/products-by-category/{pageNum}")
    public String productByCategory(@PathVariable(value = "pageNum") int pageNum,
                                    @RequestParam("category") String category, Model model){
        Page<Product> productPage = productsService.getProductsByCategory(pageNum, category);
        model.addAttribute("productList", productPage.getContent().stream().map(dtoMapper::toDto));
        model.addAttribute("category_name", category);
        model.addAttribute("productCategories", category);
        model.addAttribute("current_page", pageNum);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalPgs", productPage.getTotalElements());
        model.addAttribute("sortField", category);
        return "products_by_category.html";
    }

    @GetMapping(value = "/product-view/{productId}")
    public String productView(Model model, @PathVariable("productId") int productId){
        Product product = productsService.getProductById(productId);
        model.addAttribute("product", product);
        return "product_detail.html";
    }
}
