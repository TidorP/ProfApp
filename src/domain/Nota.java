package domain;

import repository.HasID;
import utils.Pairr;


public class Nota implements HasID<Pairr<Integer,Integer>> {
    private Pairr<Integer,Integer> id;
    private int temaId;
    private int studentId;
    private int valoare;
    private int date;

    /**
     *
     * @param tema -tema
     * @param student -student
     * @param valoarre -the value
     * @param date -date
     */
    public Nota(int tema, int student, int valoarre, int date) {
        this.temaId = tema;
        this.studentId = student;
        this.valoare = valoarre;
        this.date = date;

        this.id=new Pairr<>(student,tema);
    }

    /**
     *
     * @return id -the id consisting in studentId, temaId
     */
    @Override
    public Pairr<Integer, Integer> getId() {
        return id;
    }

    /**
     *
     * @param id - set the id
     */
    @Override
    public void setId(Pairr<Integer, Integer> id) {
        this.id = id;
    }

    /**
     *
     * @return temaid -the id of the homework
     */
    public int getTemaId() {
        return temaId;
    }

    /**
     *
     * @param temaId -tema id
     */
    public void setTemaId(int temaId) {
        this.temaId = temaId;
    }

    /**
     *
     * @return studentId -student Id
     */
    public int getStudentId() {
        return studentId;
    }

    /**
     *
     * @param studentId - the student id
     */
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    /**
     *
     * @return valoare -the mark
     */
    public int getValoare() {
        return valoare;
    }

    /**
     *
     * @param valoare -set the mark
     */
    public void setValoare(int valoare) {
        this.valoare = valoare;
    }

    /**
     *
     * @return date -the actual date
     */
    public int getDate() {
        return date;
    }

    /**
     *
     * @param date -the date
     */
    public void setDate(int date) {
        this.date = date;
    }
}
