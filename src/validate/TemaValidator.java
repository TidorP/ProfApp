package validate;

import domain.Tema;

public class TemaValidator implements Validator<Tema> {
    /**
     *
     * @param t - a homework to be validated
     * @throws ValidationException -we throw exception if anything is out of place
     */
    public void validate(Tema t) throws ValidationException{
        if(t.getId()== null || t.getId()<1){
            throw new ValidationException("Id invalid\n");
        }
        if(t.getDescriere().equals("")){
            throw new ValidationException("Descriere invalida..\n");
        }
        if(t.getDue()<1 || t.getDue()>14){
            throw new ValidationException("Date due invalid..\n");
        }
        if(t.getGiven()<1 || t.getGiven()>14){
            throw new ValidationException("Date given invalid..\n");
        }
        if(t.getGiven()>t.getDue()){
            throw new ValidationException("Due must be >=given..\n");
        }
    }
}
