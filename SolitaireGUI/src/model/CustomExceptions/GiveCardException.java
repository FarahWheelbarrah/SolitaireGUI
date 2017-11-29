package model.CustomExceptions;

public class GiveCardException extends Exception {
    public GiveCardException(String errorMessage) {
        super(errorMessage);
    }
}
