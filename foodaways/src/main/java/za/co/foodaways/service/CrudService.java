package za.co.foodaways.service;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.foodaways.model.BaseEntity;

public interface CrudService<E extends BaseEntity>{

    JpaRepository<E, Integer> getRepository();

    E findById(int id);

    default E addNewEntity(E entity){
        return getRepository().save(entity);
    }
}
