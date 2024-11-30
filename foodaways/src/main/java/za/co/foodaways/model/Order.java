package za.co.foodaways.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Entity(name = "orders")
@Setter
@Getter
public class Order extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    public Integer orderId;
    public String orderStatus;
    public ArrayList<Product> orderItems;

    // Customer Order Mapping.
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = true)
    public StoreUser user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_reference_id", referencedColumnName = "storeId", nullable = false)
    public Store storeOrder;

    public double getOrderTotal(){
        return this.orderItems.isEmpty() ?
                0.0 : this.orderItems.stream().mapToDouble(p -> p.productPrice).sum();
    }
}
