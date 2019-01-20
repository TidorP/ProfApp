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

public class Service implements Observable<StudentEvent> {
    private StudentRepository studentRepository;
    private TemaRepository temaRepository;
    private NoteRepository noteRepository=new NoteRepository();
    private ArrayList<Observer<StudentEvent>> observers;

    /**
     *
     * @param studentRepository -repository for students
     * @param temaRepository - repository for homeworks
     */
    public Service(StudentRepository studentRepository, TemaRepository temaRepository) {
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
    public int size()
    {
        int contor=0;
        for(Student st: findAllS())
        {
            contor++;
        }
        return contor;
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

    public Student forControllerSaveS(Student st) throws ValidationException {
        if(findOneByNameS(st.getNume())==null) {
            studentRepository.save(st);
            studentRepository.writeToFile();
            notifyObservers(new StudentEvent(null, st, ChangeEventType.ADD));
            return st;
        }
        else
            return null;
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
    public Student forControllerDeleteS(int id) {
        Student st=studentRepository.delete(id);
        studentRepository.writeToFile();
        notifyObservers(new StudentEvent(null, st, ChangeEventType.DELETE));
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
    public Student forControllerUpdateS(Student st) throws ValidationException {
        studentRepository.update(st);
        studentRepository.writeToFile();
        notifyObservers(new StudentEvent(null, st, ChangeEventType.UPDATE));
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

    /**
     *
     * @param id -the id of the homework
     */
    public void prelungireTema(int id)
    {
        Tema tema=findOneT(id);
        tema.prelungire();
        temaRepository.writeToFile();
    }

    /**
     *
     * @param studentId  -the student id
     * @param temaId - the homework id
     * @param valoare -the mark
     * @param saptamana -the current week
     * @param FeedBack -the feedback
     * @return nota_maxima - the max mark the student with the id studentId can take
     * @throws IOException -in case something goes wrong with the file
     * @throws ValidationException -in case of invalid data
     */
    public int saveNota(int studentId, int temaId, int valoare, int saptamana, String FeedBack) throws IOException, ValidationException {

        if(!noteRepository.findNota_forStudent(studentId,temaId))
            return -1;

        Tema tema=findOneT(temaId);
        Student student=findOneS(studentId);

        if(tema==null || student==null)
            throw new ValidationException("Tema sau student cu acest id nu exista!!!");

        Nota nota;
        int nota_maxima=10;
        if(tema.getDue()-saptamana>=0)
            nota=new Nota(temaId,studentId,valoare,saptamana);
        else if(saptamana-tema.getDue()>2) {
            nota = new Nota(temaId, studentId, 1, saptamana);
            nota_maxima=1;
        }
        else {
            nota = new Nota(temaId, studentId, valoare - 2 * (-(tema.getDue() - saptamana)), saptamana);
            nota_maxima=nota_maxima-2 * (-(tema.getDue() - saptamana));
        }

        noteRepository.addNota(nota);
        String path="C:"+"\\"+"Users"+"\\"+"ptido"+"\\"+"IdeaProjects"+"\\"+"Lab3"+"\\";
        path=path+student.getNume()+".txt";
        String text="Tema:"+tema.getId()+System.lineSeparator()+
                "Nota:"+Integer.toString(nota.getValoare())+System.lineSeparator()+
                "Predata in saptamana:"+Integer.toString(saptamana)+System.lineSeparator()+
                "Deadline:"+Integer.toString(tema.getDue())+System.lineSeparator()+
                "Feedback:"+FeedBack+System.lineSeparator();
        ServiceNote.createFileFolder(student, path, text, noteRepository);
        return nota_maxima;

    }
    public List<Nota> filtByHM(int temaId)
    {
        List<Nota> l=new ArrayList<>();
        noteRepository.getNote().forEach((nota)->{if(nota.getTemaId()==temaId) l.add(nota);});
        return l;
    }
    public List<Nota> filtBySt(String studentName)
    {
        int id=findOneByNameS(studentName).getId();
        List<Nota> l=new ArrayList<>();
        noteRepository.getNote().forEach((nota)->{if(nota.getStudentId()==id) l.add(nota);});
        return l;
    }
    public List<Nota> filtByWeek(int start,int end)
    {
        List<Nota> l=new ArrayList<>();
        noteRepository.getNote().forEach((nota)->{if(nota.getDate()>=start && nota.getDate()<=end) l.add(nota);});
        return l;
    }
    public List<Nota> filtByGroup(int grupa)
    {
        List<Nota> l=new ArrayList<>();
        noteRepository.getNote().forEach((nota)->{if(findOneS(nota.getStudentId()).getGrupa()==grupa) l.add(nota);});
        return l;
    }



    @Override
    public void addObserver(Observer<StudentEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<StudentEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(StudentEvent studentEvent) {
        observers.forEach(obs -> obs.update(studentEvent));
    }

}
