package model.CustomExceptions;

public class ReserveDeckEmptyException extends Exception {
    public ReserveDeckEmptyException() {
        super("Cannot restock Deck - Reserve Deck is empty (both the Reserve Deck and the Deck are empty)");
    }
}
