package model.CustomExceptions;

public class BlankInputException extends Exception {
    public BlankInputException(String errorMessage) {
        super(errorMessage);
    }
}
