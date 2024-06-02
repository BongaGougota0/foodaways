package za.co.foodaways.model;

import java.util.ArrayList;

public class Order extends BaseEntity{
    public String orderStatus;
    public int userId;
    public ArrayList<Product> orderItems;
    public int orderTo;

    public double getOrderTotal(){
        return this.orderItems.isEmpty() ?
                0.0 : this.orderItems.stream().mapToDouble(p -> p.productPrice).sum();
    }
}
