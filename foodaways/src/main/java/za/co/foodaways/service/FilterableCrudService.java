package za.co.foodaways.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import za.co.foodaways.model.BaseEntity;

public interface FilterableCrudService<E extends BaseEntity> extends CrudService<E>{
    Page<E> findAllEntities(Pageable pageable);
    Page<E> findEntitiesByStatus(String filterString, Pageable pageable);
}
