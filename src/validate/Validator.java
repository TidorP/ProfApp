package validate;


public interface Validator<E> {
    /**
     *
     * @param entity - we valdiate a generic entity
     * @throws ValidationException -if anything is out of place
     */
    void validate(E entity) throws ValidationException;
}