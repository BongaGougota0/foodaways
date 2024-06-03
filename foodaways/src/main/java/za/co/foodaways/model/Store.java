package za.co.foodaways.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Store extends  BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    public Integer id;
    public String storeName;
    public String storeNumber;
    public String storeLocation;
    public int storeOwner;
}
