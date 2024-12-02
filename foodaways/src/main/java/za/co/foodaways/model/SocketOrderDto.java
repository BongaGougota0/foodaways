package za.co.foodaways.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class SocketOrderDto {
    int orderId;
    String orderStatus;
    String orderRecipient; // Order.StoreUser.fullName
    String orderItems; // Order.orderItems

    String deliveryAddressId; // used only for getting geo coordinates | Order.StoreUser.Address.locationName
    String deliveryAddress; // Order.StoreUser.Address.locationName
}
