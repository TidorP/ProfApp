import domain.Student;
import domain.Tema;
import org.junit.Test;
import repository.AbstractRepository;
import repository.StudentRepository;
import repository.TemaRepository;
import service.Service;
import validate.StudentValidator;
import validate.TemaValidator;
import validate.ValidationException;
import validate.Validator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
public class TestRepository {
    @Test
    public void addStudent() throws FileNotFoundException, ValidationException {
        Validator<Student> validator=new StudentValidator();
        AbstractRepository<Integer,Student> studentRepository=new StudentRepository(validator);
        try{
            studentRepository.save(new Student(1,"Parker",226,"email","Popescu"));
            studentRepository.save(new Student(2,"Peter",226,"email2","Popescu"));
            Iterable<Student> all = studentRepository.findAll();
            List<Student> students=new ArrayList<Student>();
            all.forEach(students::add);
            assertEquals(students.get(0).getGrupa(),226);
            assertEquals(students.get(1).getEmail(),"email2");
        }
        catch (ValidationException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void deleteStudent() throws FileNotFoundException, ValidationException {
        Validator<Student> validator=new StudentValidator();
        AbstractRepository<Integer,Student> studentRepository=new StudentRepository(validator);
        try{
            studentRepository.save(new Student(1,"Parker",226,"email","Popescu"));
            studentRepository.save(new Student(2,"Peter",226,"email2","Popescu"));
            studentRepository.delete(1);
            Iterable<Student> all = studentRepository.findAll();
            List<Student> students=new ArrayList<Student>();
            all.forEach(students::add);
            assertEquals(students.get(0).getEmail(),"email2");
        }
        catch (ValidationException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void updateStudent() throws FileNotFoundException, ValidationException {
        Validator<Student> validator=new StudentValidator();
        AbstractRepository<Integer,Student> studentRepository=new StudentRepository(validator);
        try{
            studentRepository.save(new Student(1,"Parker",226,"email","Popescu"));
            studentRepository.save(new Student(2,"Peter",226,"email2","Popescu"));
            studentRepository.update(new Student(1,"OtherParker",226,"emailOther","Popescu"));
            Iterable<Student> all = studentRepository.findAll();
            List<Student> students=new ArrayList<Student>();
            all.forEach(students::add);
            assertEquals(students.get(0).getNume(),"OtherParker");
        }
        catch (ValidationException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void setStudent() throws FileNotFoundException, ValidationException {
        Student st=new Student(1,"Parker",226,"email","Popescu");
        st.setId(2);
        assertEquals((int)st.getId(),2);
        st.setNume("Bob");
        assertEquals(st.getNume(),"Bob");
        st.setGrupa(227);
        assertEquals((int)st.getGrupa(),227);
        st.setEmail("email2");
        assertEquals(st.getEmail(),"email2");
        st.setCadruDidactic("Serban");
        assertEquals(st.getCadruDidactic(),"Serban");
    }
    @Test
    public void compStudent() throws FileNotFoundException, ValidationException {
        Student st1=new Student(1,"Parker",226,"email","Popescu");
        Student st2=new Student(1,"Parker",226,"email","Popescu");
        assertTrue(st1.comp(st2));
        Student st3=new Student(3,"Parker",226,"email","Popescu");
        assertFalse(st1.comp(st3));
    }

}
