package za.co.foodaways.model;

import lombok.Data;
import za.co.foodaways.dto.ProductDto;
import java.util.*;

@Data
public class Cart {
    public Map<Integer, ProductDto> cartItems = new HashMap<>();
    public double cartTotal;
    public int userId;

    public double getSumOfCartItems(){
        return cartItems.values().stream()
                .mapToDouble(ProductDto::getProductPrice).sum();
    }
}

