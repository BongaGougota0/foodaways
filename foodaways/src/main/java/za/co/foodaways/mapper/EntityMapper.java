package za.co.foodaways.mapper;

import org.mapstruct.Mapper;
import za.co.foodaways.dto.ProductDto;
import za.co.foodaways.model.Product;

@Mapper(componentModel = "spring")
public interface EntityMapper extends BaseMapper<Product, ProductDto>{
}
