package service;

import domain.Nota;
import domain.Student;
import domain.Tema;
import repository.NoteRepository;
import repository.StudentRepository;
import repository.TemaRepository;
import utils.*;
import validate.ValidationException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ServiceTema implements Observable<TemaEvent> {
    private StudentRepository studentRepository;
    private TemaRepository temaRepository;
    private NoteRepository noteRepository=new NoteRepository();
    private ArrayList<Observer<TemaEvent>> observers;

    /**
     *
     * @param studentRepository -repository for students
     * @param temaRepository - repository for homeworks
     */
    public ServiceTema(StudentRepository studentRepository, TemaRepository temaRepository) {
        this.studentRepository = studentRepository;
        this.temaRepository = temaRepository;
        observers=new ArrayList<>();
    }

    /**
     *
     * @param id - id of a student
     * @return - student
     */
    public Student findOneS(int id) {

        return studentRepository.findOne(id);
    }

    /**
     *
     * @param name -name of a student
     * @return -the student
     */
    public Student findOneByNameS(String name) {

        Iterable<Student> students=findAllS();
        for(Student st:students)
        {
            if(st.getNume().equals(name))
            {
                return st;
            }
        }
        return null;
    }

    /**
     *
     * @param id -id of a homework
     * @return -homework
     */
    public Tema findOneT(int id) {
        return temaRepository.findOne(id);
    }

    /**
     *
     * @return all the students
     */
    public Iterable<Student> findAllS() {
        return studentRepository.findAll();

    }

    public Iterable<Nota> findAllNote() {
        return noteRepository.getNote();

    }

    /**
     *
     * @return all the homeworks
     */
    public Iterable<Tema> findAllT() {
        return temaRepository.findAll();
    }

    /**
     *
     * @param id -id
     * @param nume -name
     * @param grupa -group
     * @param email -email
     * @param cadruDidactic -name of the professor
     * @return the new student
     * @throws ValidationException -in case the data in invalid
     */
    public Student saveS(int id, String nume, int grupa, String email, String cadruDidactic) throws ValidationException {
        Student st=studentRepository.save(new Student(id, nume, grupa, email, cadruDidactic));
        studentRepository.writeToFile();
        return st;
    }


    /**
     *
     * @param id -id
     * @param descrierere -description
     * @param due -due date
     * @param given -given date
     * @return -the new homework
     * @throws ValidationException -in case the data in invalid
     */
    public Tema saveT(int id, String descrierere, int due, int given) throws ValidationException {
        Tema t=temaRepository.save(new Tema(id, descrierere, due, given));
        temaRepository.writeToFile();
        return t;
    }
    public Tema forControllerSaveT(Tema t) throws ValidationException {
        temaRepository.save(t);
        temaRepository.writeToFile();
        notifyObservers(new TemaEvent(null, t, ChangeEventType.ADD));
        return t;
    }
    public int size()
    {
        int contor=0;
        for(Tema t: findAllT())
        {
            contor++;
        }
        return contor;
    }

    /**
     *
     * @param id -the id
     * @return the deleted student
     */
    public Student deleteS(int id) {
        Student st=studentRepository.delete(id);
        studentRepository.writeToFile();
        //notifyObservers(new StudentEvent(null, st, ChangeEventType.DELETE));
        return st;
    }

    /**
     *
     * @param id -the id
     * @return -the deleted homework
     */
    public Tema deleteT(int id) {
        Tema t=temaRepository.delete(id);
        temaRepository.writeToFile();
        return t;
    }
    public Tema forControllerDeleteT(int id) {
        Tema t=temaRepository.delete(id);
        temaRepository.writeToFile();
        notifyObservers(new TemaEvent(null, t, ChangeEventType.DELETE));
        return t;
    }

    /**
     *
     * @param id -the id
     * @param nume -new name
     * @param grupa -new group
     * @param email -new email
     * @param cadruDidactic -new prof mail
     * @return -new Student
     * @throws ValidationException -in case the data in invalid
     */
    public Student updateS(int id, String nume, int grupa, String email, String cadruDidactic) throws ValidationException
    {
        Student st= studentRepository.update(new Student(id, nume, grupa, email, cadruDidactic));
        studentRepository.writeToFile();
        return st;
    }

    /**
     *
     * @param id -the id
     * @param descrierere -new description
     * @param due -new due date
     * @param given -new given date
     * @return -new homework
     * @throws ValidationException -in case the data in invalid
     */
    public Tema updateT(int id, String descrierere, int due, int given) throws ValidationException
    {
        Tema t=temaRepository.update(new Tema(id, descrierere, due, given));
        temaRepository.writeToFile();
        return t;
    }
    public Tema forControllerUpdateT(Tema t) throws ValidationException {
        if(temaRepository.update(t)!=null) {
            temaRepository.writeToFile();
            notifyObservers(new TemaEvent(null, t, ChangeEventType.UPDATE));
            return t;
        }
        else
            return null;
    }

    /**
     *
     * @param id -the id of the homework
     */
    public void prelungireTema(int id)
    {
        Tema tema=findOneT(id);
        try {
            forControllerUpdateT(new Tema(tema.getId(), tema.getDescriere(), tema.getDue()+1, tema.getGiven()));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        //tema.prelungire();
        //temaRepository.writeToFile();
        //notifyObservers(new TemaEvent(null, tema, ChangeEventType.UPDATE));
    }
    public void sendEmailToAll(String subject,String message)
    {
        for(Student st:findAllS())
        {
            SendEmail sendEmail=new SendEmail(st.getEmail(),message,subject);
            sendEmail.start();
        }

    }


    @Override
    public void addObserver(Observer<TemaEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<TemaEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(TemaEvent temaEvent) {
        observers.forEach(obs -> obs.update(temaEvent));
    }

}
