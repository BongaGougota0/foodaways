package za.co.foodaways.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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

    public Page<Product> getProductsByCategory(int pageNum, String productCategory){
        Pageable pageable = PageRequest.of(pageNum, 10, Sort.by(productCategory).ascending());
        return productsRepository.findByProductCategory(productCategory, pageable);
    }

    public Page<Product> getProductsByBestRating(int pageNum, String filter){
        Pageable pageable = PageRequest.of(pageNum, 10, Sort.by(filter).ascending());
        return productsRepository.getProductsByRatingEqualToAndGreater(pageable);
    }

    public Page<Product> getProductsByRatingEqualToAndGreater(int pageNum, String sortField){
        Pageable pageable = PageRequest.of(pageNum, 10, Sort.by(sortField).ascending());
        return productsRepository.getProductsByRatingEqualToAndGreater(pageable);
    }


    public Product getProductById(int productId) {
        boolean check = productsRepository.findById(productId).isPresent();
        if(productsRepository.findById(productId).isEmpty()){
            throw new RuntimeException("Product not found");
        }
        return productsRepository.findById(productId).get();
    }


}
