package za.co.foodaways.mapper;

import org.mapstruct.Mapper;
import za.co.foodaways.dto.OrderDto;
import za.co.foodaways.model.Order;

@Mapper(componentModel = "spring")
public interface OrderDtoMapper extends BaseMapper <OrderDto, Order>{
}
