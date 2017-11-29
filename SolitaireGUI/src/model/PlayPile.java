package model;

import model.CustomExceptions.BlankInputException;
import model.CustomExceptions.IncorrectInputTypeException;
import model.CustomExceptions.ReceiveCardException;
import model.CustomExceptions.SuccessfulTurnException;
import model.CustomExceptions.GiveCardException;
import java.util.LinkedList;
import javafx.collections.ObservableList;

public class PlayPile <T> extends Pile implements Giver, Receiver<T> {
    private final int startingSize;
    
    public PlayPile(int number) {
        super(number);
        startingSize = number;
    }
    
    public void deal(Deck deck) {
        Card card;
        for (int i = 1; i <= startingSize; i++) {
            card = deck.removeTopCard();
            if (i == startingSize)
                card.setFaceUp(true);
            cards.add(card);
        }
    }
    
    public final int getStartingSize() {
        return startingSize;
    }
    
    @Override 
    public String toString() { 
        return "Play Pile " + number + ": " + super.toString();
    }

    private boolean canAddToEmpty(Card card) {
        return cards.isEmpty() && card.hasNumber(13);
    }

    private boolean canAddToNonEmpty(Card card) {
        return !cards.isEmpty() && card.hasDifference(getTopCard().getNumber(), 1) && !card.hasColor(getTopCard().getColor());
    }
    
    @Override
    public void give(ObservableList<Card> cardsToMove) throws SuccessfulTurnException, ReceiveCardException {
        //if (receiver instanceof PlayPile && getNumOfFaceUpCards() > 1)
          //  giveCards(receiver); 
        //else {
            //receiver.receive(getTopCard());
            remove(cardsToMove);
            //throw new SuccessfulTurnException(receiver);
        //}
    }
    
    private void giveCards(Receiver receiver) throws SuccessfulTurnException {
        String cardNumberSelection;
        while (!(cardNumberSelection = Utilities.readString("Enter number of face up cards to remove from top of Play Pile " + number + " (from 1 to " + getNumOfFaceUpCards() + "): ")).equals("b")) {
            try {
                  LinkedList<Card> cardsToGive = getCardsToMove(Utilities.parseInt(cardNumberSelection, "\"" + cardNumberSelection + "\" is not a valid selection"));
                  receiver.receive(cardsToGive);
                  //remove(cardsToGive);
                  throw new SuccessfulTurnException(receiver);
            } catch (IncorrectInputTypeException | BlankInputException | GiveCardException | ReceiveCardException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    private LinkedList<Card> getCardsToMove(int numOfCards) throws GiveCardException {
        LinkedList<Card> cardsToMove = new LinkedList<Card>();
        if (numOfCards > numOfCards() || numOfCards < 1)
            throw new GiveCardException("Number cannot be out of range of number of cards in Play Pile " + number);
        for (int i = numOfCards() - 1; i >= numOfCards() - numOfCards; i--) {
            if (!cards.get(i).isFaceUp())
                throw new GiveCardException("All cards for transfer must be face up");
            cardsToMove.addFirst(cards.get(i));
        }
        return cardsToMove; 
    }
    
    @Override
    public void receive(T cardOrCardsToReceive) throws ReceiveCardException {
        if (cardOrCardsToReceive instanceof Card)
            receive((Card)cardOrCardsToReceive);
        else
            receive((ObservableList<Card>)cardOrCardsToReceive);
    }
    
    private void receive(Card cardToReceive) throws ReceiveCardException {
        if (!canAddToEmpty(cardToReceive) && !canAddToNonEmpty(cardToReceive))
            throw new ReceiveCardException(getErrorMessage(cardToReceive));
        cards.add(cardToReceive);
    }
    
    private void receive(ObservableList<Card> cardsToReceive) throws ReceiveCardException {
        Card topCard = cardsToReceive.get(0);
        if (!canAddToEmpty(topCard) && !canAddToNonEmpty(topCard))
            throw new ReceiveCardException(getErrorMessage(cardsToReceive));
        cards.addAll(cardsToReceive);
    }
    
    @Override
    public void remove(ObservableList<Card> cardsToRemove) {
        super.remove(cardsToRemove);
        flipTopCard();
    }
    
    private void remove(Card cardToRemove) {
        cards.remove(cardToRemove);
        flipTopCard();
    }
    
    private void flipTopCard() {
        if (!isEmpty() && !getTopCard().isFaceUp())
            getTopCard().setFaceUp(true);
    }
    
    private String getErrorMessage(ObservableList<Card> cardsToReceive) {
        String pluralOrNot1 = ((cardsToReceive.size() > 1) ? "s" : "");
        String pluralOrNot2 = ((pluralOrNot1.equals("s")) ? "Bottom card" : "Card");
        String stringBasedOnReceiver = ((numOfCards() > 1) ? " top " : " ");
        String errorMessage = "Cannot move card" + pluralOrNot1 + " (" + printOutCards(cardsToReceive) + "):";
        if (isEmpty())
            errorMessage += " Play Pile " + number + " is empty - " + pluralOrNot2 + " must be a King to add to this pile";
        else
            errorMessage += " " + pluralOrNot2 + " must be one value lower than and be a different color to" + stringBasedOnReceiver + "card in receiving pile (Play Pile " + number + " - " + getTopCard() + ")";
        return errorMessage;
    }
    
    private String getErrorMessage(Card card) {
        String errorMessage = "Cannot move card (" + card + "): ";
        String stringBasedOnReceiver = ((numOfCards() > 1) ? " top " : " ");
        if (isEmpty())
            errorMessage += "Play Pile " + number + " is empty - Card must be a King to add to this pile";
        else
            errorMessage += "Card must be one value lower than and be a different color to" + stringBasedOnReceiver + "card in receiving pile (Play Pile " + number + " - " + getTopCard() + ")";
        return errorMessage;
    }
    
    private String printOutCards(ObservableList<Card> cardsToReceive) {
        String cardString = "";
        for (Card card : cardsToReceive)
            cardString += card + " ";
        return cardString.trim();
    }
}
