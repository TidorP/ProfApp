package domain;

import repository.HasID;

import java.util.Objects;

public class Tema implements HasID<Integer> {
    private int nr;
    private String descriere;
    private int due;
    private int given;

    /**
     *
     * @param nr - the unique identification number
     * @param descriere - the description
     * @param due - the due date
     * @param given - the given date
     */
    public Tema(int nr, String descriere, int due, int given) {
        this.nr = nr;
        this.descriere = descriere;
        this.due = due;
        this.given = given;
    }

    /**
     *
     * @return nr - the number
     */

    public Integer getId() {
        return nr;
    }

    /**
     *
     * @param nr -set the number
     */

    public void setId(Integer nr) {
        this.nr = nr;
    }

    /**
     *
     * @return descriere - get description
     */

    public String getDescriere() {
        return descriere;
    }

    /**
     *
     * @param descriere - set the description
     */

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    /**
     *
     * @return due - the due date
     */

    public int getDue() {
        return due;
    }

    /**
     *
     * @param due - set the due date
     */

    public void setDue(int  due) {
        this.due = due;
    }

    /**
     *
     * @return given - return the given date
     */

    public int getGiven() {
        return given;
    }

    /**
     *
     * @param given - set the given date
     */

    public void setGiven(int given) {
        this.given = given;
    }

    /**
     *
     * we increase the due date
     */
    public void prelungire()
    {
        this.due++;
    }

    /**
     *
     * @param o -a generic object
     * @return true if the object are the same exactly , false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tema tema = (Tema) o;
        return nr == tema.nr;
    }

    /**
     *
     * @return -the hash code for the entity
     */

    @Override
    public int hashCode() {
        return Objects.hash(nr);
    }

    @Override
    public String toString() {
        return "Tema{" +
                "nr=" + nr +
                ", descriere='" + descriere + '\'' +
                ", due=" + due +
                ", given=" + given +
                '}';
    }
}
