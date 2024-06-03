package za.co.foodaways.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.ArrayList;

@Entity(name = "order")
public class Order extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    public Long id;
    public String orderStatus;
    public int userId;
    public ArrayList<Product> orderItems;
    public int orderTo;

    public double getOrderTotal(){
        return this.orderItems.isEmpty() ?
                0.0 : this.orderItems.stream().mapToDouble(p -> p.productPrice).sum();
    }
}
