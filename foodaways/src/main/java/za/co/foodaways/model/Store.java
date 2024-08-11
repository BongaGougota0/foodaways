package za.co.foodaways.model;

import jakarta.persistence.*;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
public class Store extends  BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    public Integer id;
    public String storeName;
    public String storeNumber;
    public String storeLocation;


    public int storeOwner;

    @OneToMany(mappedBy = "products", fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST, targetEntity = Product.class)
    public Set<Product> products;

    @OneToMany(mappedBy = "orders", fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST, targetEntity = Order.class)
    public Set<Order> orders;
}
