package za.co.foodaways.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.foodaways.model.Review;
import za.co.foodaways.model.Product;
import za.co.foodaways.repository.ProductsRepository;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductsService {

    private final ProductsRepository productsRepository;
    @Autowired
    public ProductsService(ProductsRepository productsRepository){
        this.productsRepository = productsRepository;
    }

    public ArrayList<Product> getAllProducts(){
        List<Product> productArrayList = productsRepository.findAll();
        return new ArrayList<>(productArrayList);
    }

    public Map<String, List<Product>> getProductsForMenuDisplay(){
        return productsRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        Product::getProductCategory,
                        Collectors.toCollection(ArrayList::new)
                ));
    }

    public ArrayList<Product> getProductsByCategory(String productCategory){
        return productsRepository.findByProductCategory(productCategory);
    }

    public ArrayList<Product> getProductsByBestRating(String filter){
        return productsRepository.findByProductCategory(filter).stream()
                .filter( product -> product.getReviews()
                            .stream().mapToDouble(Review::getRating)
                        .average().orElse(0.00) >= 3.00
                ).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Product> getProductsByRatingEqualToAndGreater(int filter){
        return productsRepository.getProductsByRatingEqualToAndGreater().stream()
                .filter( product -> product.getReviews()
                        .stream().mapToDouble(Review::getRating)
                        .average().orElse(0.00) >= filter
                ).collect(Collectors.toCollection(ArrayList::new));
    }


    public Product getProductById(int productId) {
        boolean check = productsRepository.findById(productId).isPresent();
        if(productsRepository.findById(productId).isEmpty()){
            throw new RuntimeException("Product not found");
        }
        return productsRepository.findById(productId).get();
    }


}
