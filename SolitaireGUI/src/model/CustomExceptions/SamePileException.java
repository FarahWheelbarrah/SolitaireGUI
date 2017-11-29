package model.CustomExceptions;

public class SamePileException extends Exception {
    public SamePileException() {
        super("\"Giving\" and \"Receiving\" piles cannot be the same");
    }
}
