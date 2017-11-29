package model.CustomExceptions;

public class IncorrectInputTypeException extends Exception {
    public IncorrectInputTypeException(String errorMessage) {
        super(errorMessage);
    }
}
