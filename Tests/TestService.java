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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;
public class TestService {
    @Test
    public void ServiceAddStudent() throws FileNotFoundException, ValidationException {
        Validator<Student> validator=new StudentValidator();
        Validator<Tema> validatorTema=new TemaValidator();
        StudentRepository studentRepository=new StudentRepository(validator);
        TemaRepository temaRepository=new TemaRepository(validatorTema);
        Service service=new Service(studentRepository,temaRepository);
        try{
            service.saveS(1,"Parker",226,"email","Popescu");
            service.saveS(2,"Peter",226,"email2","Popescu");
            Iterable<Student> all = service.findAllS();
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
    public void ServiceDeleteStudent() throws FileNotFoundException, ValidationException {
        Validator<Student> validator=new StudentValidator();
        Validator<Tema> validatorTema=new TemaValidator();
        StudentRepository studentRepository=new StudentRepository(validator);
        TemaRepository temaRepository=new TemaRepository(validatorTema);
        Service service=new Service(studentRepository,temaRepository);
        try{
            service.saveS(1,"Parker",226,"email","Popescu");
            service.saveS(2,"Peter",226,"email2","Popescu");
            service.deleteS(1);
            Iterable<Student> all = service.findAllS();
            List<Student> students=new ArrayList<Student>();
            all.forEach(students::add);
            assertEquals(students.get(0).getEmail(),"email2");
        }
        catch (ValidationException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void ServiceUpdateStudent() throws FileNotFoundException, ValidationException {
        Validator<Student> validator=new StudentValidator();
        Validator<Tema> validatorTema=new TemaValidator();
        StudentRepository studentRepository=new StudentRepository(validator);
        TemaRepository temaRepository=new TemaRepository(validatorTema);
        Service service=new Service(studentRepository,temaRepository);
        try{
            service.saveS(1,"Parker",226,"email","Popescu");
            service.saveS(2,"Peter",226,"email2","Popescu");
            service.updateS(1,"OtherParker",226,"emailOther","Popescu");
            Iterable<Student> all = service.findAllS();
            List<Student> students=new ArrayList<Student>();
            all.forEach(students::add);
            assertEquals(students.get(0).getNume(),"OtherParker");
        }
        catch (ValidationException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testPrelungireTema() throws ValidationException {
        Validator<Student> validator=new StudentValidator();
        Validator<Tema> validatorTema=new TemaValidator();
        StudentRepository studentRepository=new StudentRepository(validator);
        TemaRepository temaRepository=new TemaRepository(validatorTema);
        Service service=new Service(studentRepository,temaRepository);
        service.saveT(1,"lab1",3,5);
        service.prelungireTema(1);
        Iterable<Tema> all = service.findAllT();
        List<Tema> teme=new ArrayList<Tema>();
        all.forEach(teme::add);
        assertEquals(teme.get(0).getDue(),4);

    }
    @Test
    public void testSaveNota() throws IOException, ValidationException {
        Validator<Student> validator=new StudentValidator();
        Validator<Tema> validatorTema=new TemaValidator();
        StudentRepository studentRepository=new StudentRepository(validator);
        TemaRepository temaRepository=new TemaRepository(validatorTema);
        Service service=new Service(studentRepository,temaRepository);
        service.saveS(1,"Parker",226,"email","Popescu");
        service.saveT(1,"tema no 2",2,3);
        int nota_max=service.saveNota(1,1,10,2,"GoodJob");
        assertEquals(nota_max,10);
        nota_max=service.saveNota(1,1,10,2,"GoodJob");
        assertEquals(nota_max,-1);

        //file exists
        File f = new File("C:"+"\\"+"Users"+"\\"+"ptido"+"\\"+"IdeaProjects"+"\\"+"Lab3"+"\\"+"Parker"+".txt");
        assertTrue(f.exists());
        Scanner sc = new Scanner(f);
        sc.nextLine();
        String[] splited;
        splited = sc.nextLine().split(":");

        //check the grade
        assertEquals(splited[1],"10");
    }
}