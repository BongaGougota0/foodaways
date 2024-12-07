package za.co.foodaways.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import za.co.foodaways.model.BaseEntity;

import java.util.Optional;

public interface FilterableCrudService<E extends BaseEntity> extends CrudService<E>{
    Page<E> findAllEntities(Pageable pageable);
    Page<E> findEntitiesByStatus(Optional<String> filterString, Pageable pageable);
}
