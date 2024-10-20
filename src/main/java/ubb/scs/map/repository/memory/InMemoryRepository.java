package ubb.scs.map.repository.memory;


import ubb.scs.map.domain.Entity;
import ubb.scs.map.domain.validators.ValidationException;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.Repository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {

    private final Validator<E> validator;
    protected Map<ID,E> entities;

    /**
     * @param validator use a specific validator
     */
    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<>();
    }

    /**
     * @param id -the id of the entity to be returned id must not be null
     *           id must not be null
     * @return the entity with the specified id
     */
    @Override
    public E findOne(ID id) {
        if (id==null)
            throw new IllegalArgumentException("id must be not null");
        return entities.get(id);
    }

    /**
     * @return all entities
     */
    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) throws ValidationException {
        if(entity==null)
            throw new IllegalArgumentException("Entity cannot be null");
        validator.validate(entity);
        if(entities.containsKey(entity.getId()))
            return entity;
        else{
            entities.put(entity.getId(),entity);
            return null;
        }
    }

    @Override
    public E delete(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must be not null!");
        }
        return entities.remove(id);
    }

    @Override
    public E update(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null!");
        }
        validator.validate(entity);
        if (entities.containsKey(entity.getId())) {
            entities.put(entity.getId(), entity);
            return entity;
        } else {
            return null;
        }
    }
}