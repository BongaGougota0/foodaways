package za.co.foodaways.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

@AllArgsConstructor
@Getter
public class CartDto {
    public ArrayList<ProductDto> cartItems;
    public double cartTotal;
}
