package za.co.foodaways.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.foodaways.model.Product;
import za.co.foodaways.repository.ProductsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public ArrayList<Product> getByBestSellingProducts(){
        List<Product> productArrayList = productsRepository.findByOrderBySalesDesc();
        return new ArrayList<>(productArrayList);
    }

    public ArrayList<Product> getByBestRating(){
        List<Product> productArrayList = productsRepository.findByOrderByRatingDesc();
        return new ArrayList<>(productArrayList);
    }

    public void addProduct(Product product){
        Product savedProduct = productsRepository.save(product);
        if(savedProduct != null){
            //Do something
        }
    }

    public void updateProduct(Product updateProduct){
        Optional<Product> productToUpdate = productsRepository.findById(updateProduct.id);
        if(productToUpdate.get() != null){
            productToUpdate.get().productPrice = updateProduct.productPrice;
            productToUpdate.get().menuItems = updateProduct.menuItems;
        }
    }

}
