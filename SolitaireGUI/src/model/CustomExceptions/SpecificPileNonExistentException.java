package model.CustomExceptions;

public class SpecificPileNonExistentException extends Exception {
    public SpecificPileNonExistentException(int number, String listName) {
        super("A " + listName + " of number \"" + number + "\" does not exist");
    }
}
