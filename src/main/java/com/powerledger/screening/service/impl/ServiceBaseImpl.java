package com.powerledger.screening.service.impl;

import com.powerledger.screening.core.EntityBase;
import com.powerledger.screening.core.RepositoryBase;
import com.powerledger.screening.core.ServiceBase;
import com.powerledger.screening.exception.BadRequestException;
import com.powerledger.screening.exception.ResourceNotFoundException;
import com.powerledger.screening.util.EntityUtils;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.ParameterizedType;

@NoArgsConstructor
public class ServiceBaseImpl<T extends EntityBase, P> implements ServiceBase<T, P> {

    private RepositoryBase<T, P> repository;

    @Autowired
    public ServiceBaseImpl(RepositoryBase<T, P> repository) {
        this.repository = repository;
    }

    /**
     * Find all entities of type T
     *
     * @param pageable Pagination criteria
     * @return list of the T
     */
    @Override
    public Flux<T> getAll(Pageable pageable) {
        return repository.findByIsDeletedFalse(pageable);
    }

    /**
     * Find an entity by id
     *
     * @param id unique id of the entity (primary key)
     * @return an object of type T
     * @throws ResourceNotFoundException if entity not found with supplied id
     */
    @Override
    public Mono<T> getById(P id) {
        return this.getEntity(id);
    }

    @Override
    public Mono<T> getByIdThoDeleted(P id) {
        return repository.findById(id).switchIfEmpty(Mono.empty());
    }

    /**
     * Create a new entity of type T
     *
     * @param entity of type T (required)
     * @return created object of type T
     * @throws BadRequestException if required parameters are missing
     */
    @Override
    @Transactional
    public Mono<T> create(T entity) {
        if (entity == null) {
            return Mono.error(new BadRequestException("Entity can not be null"));
        }
        //entity.setDateCreated(null);
        entity.setIsDeleted(false);
        return repository.save(entity);
    }

    /**
     * Update an existing entity of type T by id
     *
     * @param id     unique id of the entity (primary key)
     * @param entity (required)
     * @return updated object of type T
     * @throws BadRequestException     if required parameters are missing
     * @throws ResourceNotFoundException if entity not found with supplied id
     */
    @Override
    @Transactional
    public Mono<T> update(P id, T entity) {
        if (entity == null) {
            return Mono.error( new BadRequestException("entity can not be null"));
        }
        Mono<T> existingEntity = this.getEntity(id);
        checkExistence(existingEntity, id);
        entity.setId(null);

        return existingEntity
                .map(existingEnt -> {
                    EntityUtils.copyNonNullProperties(entity, existingEnt);
                    return existingEnt;
                })
                .flatMap(existingEnt -> repository.save(existingEnt));
    }

    /**
     * Set the delete flag true for the entity by id
     *
     * @param id (required)
     * @return deleted object of type T
     * @throws BadRequestException     if required parameters are missing
     * @throws ResourceNotFoundException if entity not found with supplied id
     */
    @Override
    @Transactional
    public Mono<T> delete(P id) {
        if (id == null) {
            Mono.error(new BadRequestException("id can not be null"));
        }
        Mono<T> entity = this.getEntity(id);
        checkExistence(entity, id);

        return entity
                .map(ent -> {
                    ent.setIsDeleted(true);
                    return ent;
                })
                .flatMap(ent -> repository.save(ent));
    }

    private Mono<T> getEntity(P id) {
        return repository.findByIdAndIsDeletedFalse(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("entity not found")));
    }

    private void checkExistence(Mono<T> entity, P id) {
        entity.switchIfEmpty(Mono.error( new ResourceNotFoundException("entity not found")));
    }

    private String getEntityName() {
        String className = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]
                .getTypeName();

        String entityName = "";

        try {
            entityName = Class.forName(className).getSimpleName();
        } catch (Exception ignored) {
        }
        return entityName;
    }
}