package za.co.foodaways.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Entity(name = "customer_orders")
@Setter
@Getter
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    public Integer orderId;
    public String orderStatus;
    @Transient
    public ArrayList<Product> orderItems;

    @Column(name = "order_items")
    public String order_items;
    public double orderTotal;

    // Customer Order Mapping.
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = true)
    public StoreUser user;

    // Align the relationship name with Store's mappedBy
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_reference_id", referencedColumnName = "storeId", nullable = false)
    public Store store;

    public void setOrderItemsString(){
        this.order_items = this.orderItems.stream().map(Product::getProductName)
                .collect(Collectors.joining(","));
    }
}
