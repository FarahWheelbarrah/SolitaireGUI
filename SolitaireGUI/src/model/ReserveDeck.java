package model;

import model.CustomExceptions.ReceiveCardException;
import model.CustomExceptions.SuccessfulTurnException;
import java.util.LinkedList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReserveDeck extends Pile implements Giver {

    public ReserveDeck(int number) {
        super(number);
    }
    
    @Override 
    public String toString() {
        String deckString = "Reserve Deck: ";
        if (isEmpty()) 
            deckString += "(Empty)";
        else {
            for (int i = numOfCards() - 3; i <= numOfCards() - 1; i++)
                if (i >= 0)
                    deckString += cards.get(i) + " ";
            deckString += "(Cards Remaining: " + numOfCards() + ")";
        }
        return deckString.trim(); 
    }

    @Override
    public void give(ObservableList<Card> cardsToGive) throws SuccessfulTurnException, ReceiveCardException {
        //receiver.receive(getTopCard());
        cards.removeAll(cardsToGive);
        // throw new SuccessfulTurnException(receiver);
    }
    
    public void receiveCards(LinkedList<Card> cardsToReceive) {
        cards.addAll(cardsToReceive);
    }
    
    public void removeCards() {
        cards.clear();
    }
}
