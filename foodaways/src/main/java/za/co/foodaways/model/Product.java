package za.co.foodaways.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    public Integer id;
    public String menuItems;
    public String productImage;
    public double productPrice;
    public int storeId;
}
