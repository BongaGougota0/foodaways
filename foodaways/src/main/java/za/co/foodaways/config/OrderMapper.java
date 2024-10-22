package za.co.foodaways.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import za.co.foodaways.dto.OrderDto;
import za.co.foodaways.mapper.DtoMapper;
import za.co.foodaways.mapper.OrderDtoMapper;
import za.co.foodaways.model.Order;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class OrderMapper {
    DtoMapper dtoMapper;
    public OrderMapper(DtoMapper dtoMapper){
        this.dtoMapper = dtoMapper;
    }
    @Bean
    public OrderDtoMapper getOrderMapper() {
        return new OrderDtoMapper() {
            @Override
            public OrderDto toDto(Order entity) {
                return new OrderDto(entity.getOrderStatus(),
                        entity.getOrderItems()
                                .stream()
                                .map(dtoMapper::toDto)
                                .collect(Collectors.toCollection(ArrayList::new)),
                        entity.user.getUserId(), entity.getStoreOrder().getStoreId());
            }

            @Override
            public Order toEntity(OrderDto dtoObject) {
                return null;
            }
        };
    }
}
