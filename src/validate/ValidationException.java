package validate;

public class ValidationException extends Exception{
    /**
     *
     * @param s - a string message to print out
     */
    public ValidationException(String s){
        super(s);
    }
}
