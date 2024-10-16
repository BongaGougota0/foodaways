package za.co.foodaways.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductDto {
    public Integer productId;
    public String menuItems;
    public String productName;
    public String productImagePath;
    public String imageOfProduct;
    public String productCategory;
    public double productPrice;
    public double productAverageRating;
    public int productCount;
}
