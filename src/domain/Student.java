package domain;

import repository.HasID;

import java.util.Comparator;

public class Student implements HasID<Integer> {
    private int id;
    private String nume;
    private int grupa;
    private String email;
    private String cadruDidactic;

    /**
     *
     * @param id - the id
     * @param nume - the name
     * @param grupa - the group
     * @param email -the email
     * @param cadruDidactic -the name of the professor
     */
    public Student(int id, String nume, int grupa, String email, String cadruDidactic) {
        this.id = id;
        this.nume = nume;
        this.grupa = grupa;
        this.email = email;
        this.cadruDidactic = cadruDidactic;
    }
    /**
     *
     * @param ot -the student to be compared with
     * id must not be null
     * @return the result of the comparator
     */

    public boolean comp(Student ot)
    {
        Comparator<Student> comparator = (p1, p2) -> {

            if(p1.getId()==p2.getId()&& p1.getNume().compareTo(p2.getNume())==0 && p1.getGrupa()==p2.getGrupa()&& p1.getEmail().equals(p2.getEmail()) && p1.getCadruDidactic().equals(p2.getCadruDidactic()))
                return 1;
            else return 0;

        };
        int in=comparator.compare(this,ot);
        return in > 0;
    }
    /**
     * @param ot -the student to be compared with
     * id must not be null
     */
    public void egaleaza(Student ot)
    {
        this.id = ot.getId();
        this.nume = ot.getNume();
        this.grupa = ot.getGrupa();
        this.email = ot.getEmail();
        this.cadruDidactic = ot.getCadruDidactic();
    }

    /**
     *
     * @return - the id of the entity
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id - the id we shall update with
     */

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return - the name of the entity
     */

    public String getNume() {
        return nume;
    }

    /**
     *
     * @param nume - the name of the entity
     */
    public void setNume(String nume) {
        this.nume = nume;
    }

    /**
     *
     * @return - the group
     */

    public int getGrupa() {
        return grupa;
    }

    /**
     *
     * @param grupa - set the group
     */

    public void setGrupa(int grupa) {
        this.grupa = grupa;
    }

    /**
     *
     * @return email - get the email
     */

    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email - set the email
     */

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return cadruDidactic - get the name of the professor
     */

    public String getCadruDidactic() {
        return cadruDidactic;
    }

    /**
     *
     * @param cadruDidactic - set the name of the professor
     */

    public void setCadruDidactic(String cadruDidactic) {
        this.cadruDidactic = cadruDidactic;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", grupa=" + grupa +
                ", email='" + email + '\'' +
                ", cadruDidactic='" + cadruDidactic + '\'' +
                '}';
    }
}
