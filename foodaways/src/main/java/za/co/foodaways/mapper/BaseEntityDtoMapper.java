package za.co.foodaways.mapper;

import za.co.foodaways.model.BaseEntity;

@FunctionalInterface
public interface BaseEntityDtoMapper<T,E extends BaseEntity> {
    T getDto(E e);
}
