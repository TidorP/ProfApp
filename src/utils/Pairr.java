package utils;

public class Pairr<First, Second> {
    private First first;
    private Second second;

    /**
     *
     * @param first -first entity
     * @param second -second entity
     */
    public Pairr(First first, Second second) {
        this.first = first;
        this.second = second;
    }

    /**
     *
     * @param first- first entity
     */
    public void setFirst(First first) {
        this.first = first;
    }

    /**
     *
     * @param second- second entity
     */
    public void setSecond(Second second) {
        this.second = second;
    }

    /**
     *
     * @return first- first entity
     */
    public First getFirst() {
        return first;
    }

    /**
     *
     * @return second- the second entity
     */
    public Second getSecond() {
        return second;
    }

    /**
     *
     * @param first -first
     * @param second -second
     */
    public void set(First first, Second second) {
        setFirst(first);
        setSecond(second);
    }

    /**
     *
     * @param o -an object
     * @return -boolean equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pairr pair = (Pairr) o;

        if (first != null ? !first.equals(pair.first) : pair.first != null) return false;
        if (second != null ? !second.equals(pair.second) : pair.second != null) return false;

        return true;
    }

    /**
     *
     * @return result- hashCode
     */
    @Override
    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        return result;
    }
}

