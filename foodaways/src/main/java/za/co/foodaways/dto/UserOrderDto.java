package za.co.foodaways.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserOrderDto {
    public int orderId;
    public String orderStatus;
    public String orderItems;

}
