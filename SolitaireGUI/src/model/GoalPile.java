package model;

import java.util.LinkedList;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.CustomExceptions.ReceiveCardException;
import model.CustomExceptions.SuccessfulTurnException;

public class GoalPile extends Pile implements Giver, Receiver<ObservableList<Card>> {
    private StringProperty suit = new SimpleStringProperty();
    
    public GoalPile(int number) {
        super(number);
    }
    
    private final void setSuit(String suit) {
        this.suit.set(suit);
    }
    
    public final String getSuit() {
        return suit.get();
    }
    
    @Override
    public String toString() {
        return "Goal Pile " + number + printSuit() + ": " + super.toString();
    }
    
    private String printSuit() {
        if (getSuit() != null && !getSuit().isEmpty())
            return " (" + getSuit() + ")";
        else 
            return "";
    }
    
    private boolean canAddToEmpty(Card card) {
        return cards.isEmpty() && card.hasNumber(1);
    }
    
    private boolean canAddToNonEmpty(Card card) {
        return !cards.isEmpty() && getTopCard().hasDifference(card.getNumber(), 1) && card.hasSuit(getSuit());
    }
    
    @Override
    public void give(ObservableList<Card> cardsToMove) throws SuccessfulTurnException, ReceiveCardException {
        //receiver.receive(getTopCard());
        remove(cardsToMove);
        //throw new SuccessfulTurnException();
    }
    
    @Override
    public void receive(ObservableList<Card> cardsToReceive) throws ReceiveCardException {
        Card cardToReceive = cardsToReceive.get(0);
        if (!canAddToEmpty(cardToReceive) && !canAddToNonEmpty(cardToReceive))
            throw new ReceiveCardException(getErrorMessage(cardToReceive));
        cards.add(cardToReceive);
        if (numOfCards() == 1)
            setSuit(cardToReceive.getSuit());
    }
    
    @Override
    public void remove(ObservableList<Card> cardsToRemove) {
        super.remove(cardsToRemove);
        if (isEmpty())
            setSuit(null);
    }
    
    private String getErrorMessage(Card cardToReceive) {
        String errorMessage = "Cannot move card (" + cardToReceive + "): ";
        String stringBasedOnReceiver = ((numOfCards() > 1) ? " top " : " "); 
        if (isEmpty())
            errorMessage += "Goal Pile " + number + " is empty - Card must be an Ace to add to this pile";
        else
            errorMessage += "Card must be one value higher than and be the same suit as" + stringBasedOnReceiver + "card in receiving pile (Goal Pile " + number + " - " + getTopCard() + ")";
        return errorMessage;
    }

    public boolean isComplete() {
        return numOfCards() == 13;
    }
    
    // can also get an observableList containing the top card of the goalPile in the following way:
    /*public ObservableList<Card> getFirstCard() {
        ObservableList<Card> topCard = FXCollections.observableArrayList();
        if (!cards.isEmpty())
            topCard.add(getTopCard());
        return topCard;
    }*/
}
