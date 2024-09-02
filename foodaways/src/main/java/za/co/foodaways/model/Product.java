package za.co.foodaways.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

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
    public double productPrice;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", referencedColumnName = "storeId", nullable = false)
    public Store store;

    public int getStoreId(){
        return store.storeId;
    }
}
