package repository;

import validate.ValidationException;
import validate.Validator;

import java.util.HashMap;

public abstract class AbstractRepository<ID , E extends HasID<ID>> implements CrudRepository<ID,E> {
    private Validator<E> v;
    private HashMap<ID,E> map;
    /**
     *
     * @param v - a validator
     */
    public AbstractRepository(Validator<E> v) {
        this.v = v;
        map=new HashMap<>();
    }

    /**
     *
     * @param id -the id of the entity to be returned
     * id must not be null
     * @return - the entity of that specific id
     */
    @Override
    public E findOne(ID id){
        return map.get(id);
    }

    /**
     *
     * @return all entities
     */
    @Override
    public Iterable<E> findAll(){
        return map.values();
    }

    /**
     *
     * @param entity
     * entity must be not null
     * @return - null- if the given entity is saved
     * otherwise returns the entity (id already exists)
     * @throws ValidationException - if the entity is not valid
     * @throws IllegalArgumentException
     *      * if the given entity is null. *
     */
    @Override
    public E save(E entity) throws ValidationException {
        E e=findOne(entity.getId());
        if(e!=null)
            throw new ValidationException("Id Existent");
        v.validate(entity);
        map.put(entity.getId(),entity);
        return entity;
    }

    /**
     * removes the entity with the specified id
     * @param id
     * id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws IllegalArgumentException
     * if the given id is null.
     */
    @Override
    public E delete(ID id){
        return map.remove(id);
    }
    /**
     *
     * @param entity
     * entity must not be null
     * @return null - if the entity is updated,
     * otherwise returns the entity - (e.g id does not exist).
     * @throws IllegalArgumentException
     * if the given entity is null.
     * @throws javax.management.RuntimeErrorException
     * if the entity is not valid.
     */
    @Override
    public E update(E entity){
        try {
            v.validate(entity);
            if (findOne(entity.getId()) == null) {
                return null;
            } else {
                return map.replace(entity.getId(), entity);
            }
        }catch(ValidationException e){
            return null;
        }
    }
    public long size(){
        return map.size();
    }


}
