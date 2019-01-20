package validate;

import domain.Student;

public class StudentValidator implements Validator<Student>{
    /**
     *
     * @param st - a student to be validated
     * @throws ValidationException -we throw exception if anything is out of place
     */
    public void validate(Student st) throws ValidationException{
        if(st.getId()== null || st.getId()<1){
            throw new ValidationException("Id invalid\n");
        }
        if(st.getNume().equals("")){
            throw new ValidationException("Nume vid..\n");
        }
        if(st.getGrupa()<221 ||st.getGrupa()>227){
            throw new ValidationException("Grupa invalida..\n");
        }
        if(st.getEmail().equals("")){
            throw new ValidationException("Email vid..\n");
        }
        if(st.getCadruDidactic().equals("")){
            throw new ValidationException("Nume cadru didactic vid..\n");
        }
    }

}
