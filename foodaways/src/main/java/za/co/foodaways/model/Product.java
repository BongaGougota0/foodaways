package za.co.foodaways.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Entity
@Setter
@Getter
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    public Integer id;
    public String menuItems;
    public String productName;
    public int productStore;
    @Transient
    public MultipartFile productImage; // Path to image
    public String productImagePath; // Path to image
    public double productPrice;
    public int storeId;
    public int rating;
    public int sales;
}
