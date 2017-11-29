package model.CustomExceptions;

public class EmptyPileException extends Exception {
    public EmptyPileException() {
        super("\"Giving\" pile cannot be empty");
    }
}
