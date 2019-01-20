package repository;

public interface HasID<ID> {
    /**
     *
     * @return - the id
     */
    ID getId();

    /**
     *
     * @param id - set the id
     */
    void setId(ID id);
}
