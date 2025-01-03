package za.co.foodaways.mapper;

public interface BaseMapper <D, E>{
    D toDto(E entity);
    E toEntity(D dtoObject);
}
