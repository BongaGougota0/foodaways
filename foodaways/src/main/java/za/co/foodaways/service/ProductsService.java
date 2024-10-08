package za.co.foodaways.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.foodaways.queries.ProductQueries;
import za.co.foodaways.queries.StoreQueries;
import za.co.foodaways.model.Product;
import za.co.foodaways.model.Store;
import za.co.foodaways.repository.ProductsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductsService {

    private final ProductsRepository productsRepository;
    @PersistenceContext
    private final EntityManager entityManager;
    @Autowired
    public ProductsService(ProductsRepository productsRepository, EntityManager entityManager){
        this.productsRepository = productsRepository;
        this.entityManager = entityManager;
    }

    public ArrayList<Product> getAllProducts(){
        List<Product> productArrayList = productsRepository.findAll();
        return new ArrayList<>(productArrayList);
    }

    public ArrayList<Product> getByBestSellingProducts(){
//        List<Product> productArrayList = productsRepository.findByOrderBySalesDesc();
//        return new ArrayList<>(productArrayList);
        return null;
    }

    public ArrayList<Product> getByBestRating(){
//        List<Product> productArrayList = productsRepository.findByOrderByRatingDesc();
//        return new ArrayList<>(productArrayList);
        return null;
    }

    public void addProduct(Product product){
        Product savedProduct = productsRepository.save(product);
        if(savedProduct != null){
            //Do something
        }
    }

    public void updateProduct(Product updateProduct){
        Optional<Product> productToUpdate = productsRepository.findById(updateProduct.getProductId());
        if(productToUpdate.get() != null){
            productToUpdate.get().productPrice = updateProduct.productPrice;
            productToUpdate.get().menuItems = updateProduct.menuItems;
        }
    }

    public Product getProductById(int productId) {
        return productsRepository.getReferenceById(productId);
    }

    // --------------------------- Store Manager Methods
    public List<Product> getStoreProductsByManagerId(int managerId){
        return productsRepository.findStoreProductsByAdminId(managerId);
    }

    @Transactional
    public void adminAddNewProduct(Product product, int adminId){
        productsRepository.save(product);
    }

    public void adminUpdateProduct(Product updatedProduct, int productId){
        Product product = productsRepository.getReferenceById(productId);

        if(product != null){
            product.setProductName(updatedProduct.getProductName());
            product.setProductPrice(updatedProduct.getProductPrice());
            product.setMenuItems(updatedProduct.getMenuItems());
            product.setProductImagePath(updatedProduct.getProductImagePath());
            productsRepository.save(product);
        }
    }

    public void deleteProductById(int productId) {
        Product product = productsRepository.findById(productId).get();
        if(product != null){
            product.setStore(null);
            productsRepository.delete(product);
        }
    }
}
