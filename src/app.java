import domain.Student;
import domain.Tema;
import repository.StudentRepository;
import repository.TemaRepository;
import service.Service;
import ui.UI;
import validate.StudentValidator;
import validate.TemaValidator;
import validate.ValidationException;
import validate.Validator;

import java.io.FileNotFoundException;

public class app {
    public static void main(String[] args) throws FileNotFoundException, ValidationException {
        String fName1="C:\\Users\\ptido\\IdeaProjects\\Lab3\\src\\repository\\studenti";
        String fName2="teme.txt";
        Validator<Student> validatorStudent=new StudentValidator();
        Validator<Tema> validatorTema=new TemaValidator();
        StudentRepository studentRepository=new StudentRepository(validatorStudent,fName1);
        TemaRepository temaRepository=new TemaRepository(validatorTema,fName2);
        Service service=new Service(studentRepository,temaRepository);
        UI u=new UI(service);
        u.run();
    }
}
