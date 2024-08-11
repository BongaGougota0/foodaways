package za.co.foodaways.model;

import jakarta.persistence.*;
import lombok.Setter;

import java.util.ArrayList;

@Entity(name = "orders")
@Setter
public class Order extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    public Integer orderId;
    public int orderStatus;
    public ArrayList<Product> orderItems;

    // Customer Order Mapping.
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = true)
    public StoreUser user;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "store_id", referencedColumnName = "storeId", nullable = false)
    public Store store;

    public double getOrderTotal(){
        return this.orderItems.isEmpty() ?
                0.0 : this.orderItems.stream().mapToDouble(p -> p.productPrice).sum();
    }
}
