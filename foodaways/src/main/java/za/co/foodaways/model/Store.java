package za.co.foodaways.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "store")
@Setter
@Getter
public class Store extends  BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    public Integer storeId;
    public String storeName;
    public String storeNumber;
    public String storeLocation;

//    @OneToMany(mappedBy = "products", fetch = FetchType.LAZY,
//            cascade = CascadeType.PERSIST, targetEntity = Product.class)
//    public Set<Product> products;
//
//    @OneToMany(mappedBy = "orders", fetch = FetchType.LAZY,
//            cascade = CascadeType.PERSIST, targetEntity = Order.class)
//    public Set<Order> orders;
}
