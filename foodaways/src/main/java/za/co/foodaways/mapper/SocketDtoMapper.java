package za.co.foodaways.mapper;

import za.co.foodaways.model.Order;
import za.co.foodaways.model.SocketOrderDto;

@FunctionalInterface
public interface SocketDtoMapper {
    SocketOrderDto getOrderDto();
}
