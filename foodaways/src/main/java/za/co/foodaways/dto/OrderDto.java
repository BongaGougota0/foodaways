package za.co.foodaways.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import za.co.foodaways.model.Product;
import java.util.ArrayList;

@AllArgsConstructor
@Getter
public class OrderDto {
    public int orderStatus;
    public ArrayList<ProductDto> orderItems;
    public int userId;
    public int storeId;
}
