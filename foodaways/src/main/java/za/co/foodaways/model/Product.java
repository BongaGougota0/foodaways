package za.co.foodaways.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "products")
@Setter
@Getter
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    public Integer productId;
    public String menuItems;
    public String productName;

    @Transient
    public MultipartFile productImage;
    public String productImagePath; // Path to image
    public String imageOfProduct;
    public double productPrice;
    public String productCategory;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", referencedColumnName = "storeId", nullable = false)
    public Store store;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "product_reviews",
            joinColumns = {
                    @JoinColumn(name = "product_id", referencedColumnName = "productId" )},
            inverseJoinColumns = {
                    @JoinColumn(name = "review_id", referencedColumnName = "reviewId")})
    public Set<Review> reviews = new HashSet<>();

    public int getStoreId(){
        return store.getStoreId();
    }
}
