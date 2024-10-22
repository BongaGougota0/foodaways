package za.co.foodaways.controller;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import za.co.foodaways.dto.OrderDto;
import za.co.foodaways.mapper.DtoMapper;
import za.co.foodaways.mapper.OrderDtoMapper;
import za.co.foodaways.model.OrderStatus;
import za.co.foodaways.service.OrderService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Controller
public class FoodawaysEventController {

    OrderService orderService;
    DtoMapper dtoMapper;
    OrderDtoMapper orderDtoMapper;
    public FoodawaysEventController(OrderService orderService, DtoMapper dtoMapper, OrderDtoMapper orderDtoMapper){
        this.orderService = orderService;
        this.dtoMapper = dtoMapper;
        this.orderDtoMapper = orderDtoMapper;
    }

    @GetMapping(path = "/store-manager/{storeId}/orders", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<ArrayList<OrderDto>>> getStoreOrder(@PathVariable("storeId") int storeId){
        //First fetch from DB
        ArrayList<OrderDto> listOfOrders = orderService.getStoreOrders(storeId)
                .stream()
                .map(orderDtoMapper::toDto).collect(Collectors.toCollection(ArrayList::new));
        // events received by Js function.
        return Flux.fromStream(listOfOrders.stream())
                .filter( o -> o.orderStatus.equalsIgnoreCase(OrderStatus.Status.ORDER_PLACED.name()))
                .map(order -> ServerSentEvent.<ArrayList<OrderDto>> builder()
                        .retry(Duration.ofMillis(3000))
                        .build())
                .delayElements(Duration.ofMillis(5000));
    }
}
