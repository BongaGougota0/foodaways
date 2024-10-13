package za.co.foodaways.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import za.co.foodaways.queries.ProductQueries;
import za.co.foodaways.queries.StoreQueries;
import za.co.foodaways.model.Product;
import za.co.foodaways.model.Store;
import za.co.foodaways.repository.ProductsRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public HashMap<String,ArrayList<Product>> getProductsForMenuDisplay(HashMap<String, ArrayList<Product>> productsMap){
        HashMap<String, ArrayList<Product>> products = new HashMap<>();
        ArrayList<Product> lunch = (ArrayList<Product>) productsMap.get("Lunch")
                .stream().filter(p -> p.getProductCategory()
                        .equals("Lunch")).limit(10)
                .toList();
        ArrayList<Product> dinner = (ArrayList<Product>) productsMap.get("Lunch")
                .stream().filter(p -> p.getProductCategory()
                        .equals("Lunch")).limit(10)
                .toList();
        ArrayList<Product> breakFast = (ArrayList<Product>) productsMap.get("Lunch")
                .stream().filter(p -> p.getProductCategory()
                        .equals("Lunch")).limit(10)
                .toList();
        products.put("Lunch", lunch);
        products.put("dinner", lunch);
        products.put("breakFast", lunch);
        return products;
    }

    public HashMap<String, ArrayList<Product>> getProductsForMenuDisplay(){
         HashMap<String, ArrayList<Product>> products = (HashMap<String, ArrayList<Product>>) productsRepository.findAll().stream()
                 .filter(
                         product -> product.getProductCategory().equals("Dinner")
                 || product.getProductCategory().equals("Lunch") || product.getProductCategory().equals("BreakFast"))
                 .collect(Collectors.groupingBy(
                         Product::getProductCategory,Collectors.collectingAndThen(Collectors.toList(), ArrayList::new)
                 ));
        return getProductsForMenuDisplay(products);
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

    public void addProduct(Product newProduct, MultipartFile productImage, Store store, int storeAdminId){
        String uploadDirectory = "src/main/resources/static/assets/images";
        if(!productImage.isEmpty()){
            String fileName = productImage.getOriginalFilename();
            try {
                File newDir = new File(uploadDirectory);
                if(!newDir.exists()){newDir.mkdirs();}
                byte[] fileBytes = productImage.getBytes();
                Path path = Paths.get(uploadDirectory, fileName);
                Files.write(path, fileBytes);
                newProduct.setProductImagePath(String.valueOf(path));
                newProduct.setImageOfProduct(fileName);
                newProduct.setStore(store);
                this.adminAddNewProduct(newProduct, storeAdminId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else if(productImage == null || productImage.isEmpty()) {
            this.adminAddNewProduct(newProduct, storeAdminId);
        }

        Product savedProduct = productsRepository.save(newProduct);
        if(savedProduct != null){
            //Do something
        }
    }

    public Product getProductById(int productId) {
        boolean check = productsRepository.findById(productId).isPresent();
        if(productsRepository.findById(productId).isEmpty()){
            throw new RuntimeException("Product not found");
        }
        return productsRepository.findById(productId).get();
    }

    // --------------------------- Store Manager Methods
    public void updateProduct(Product updateProduct, int productId){
        Product productToUpdate = productsRepository.findById(productId).get();
       productToUpdate.setProductPrice(updateProduct.getProductPrice());
       productToUpdate.setProductName(updateProduct.getProductName());
       productToUpdate.setProductCategory(updateProduct.getProductCategory());
       productToUpdate.setMenuItems(updateProduct.getMenuItems());

       //image update : check first
        // delete old
        if(updateProduct.imageOfProduct != null & !productToUpdate.getImageOfProduct().equals(updateProduct.imageOfProduct)){
            productToUpdate.setImageOfProduct(updateProduct.getImageOfProduct());
        }
        productsRepository.save(productToUpdate);
    }
    public List<Product> getStoreProductsByManagerId(int managerId){
        return productsRepository.findStoreProductsByAdminId(managerId);
    }

    @Transactional
    public void adminAddNewProduct(Product product, int adminId){
        productsRepository.save(product);
    }

    public void deleteProductById(int productId) {
        Product product = productsRepository.findById(productId).get();
        if(product != null){
            product.setStore(null);
            productsRepository.delete(product);
        }
    }
}
