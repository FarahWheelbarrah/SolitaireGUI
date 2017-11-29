package model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class Pile {
    protected ObservableList<Card> cards = FXCollections.observableArrayList();
    protected final int number;
    
    public Pile(int number) {
        this.number = number;
    }
    
    public final int getNumber() {
        return number;
    }
    
    public ObservableList<Card> getCards() {
        return cards;
    }
    
    @Override 
    public String toString() {
        String pileString = "";
        if (isEmpty())
            pileString += "(Empty)";
        else {
            for (Card card: cards)
                pileString += card + " ";
        }
        return pileString.trim();
    }
    
    public boolean hasNumber(int number) {
        return this.number == number;
    }
    
    public boolean isEmpty() {
        return cards.isEmpty();
    }  
    
    public int numOfCards() {
        return cards.size();
    }
    
    public Card getTopCard() {
        return cards.get(cards.size() - 1);
    }
    
    public int getNumOfFaceUpCards() {
        int numOfFaceUpCards = 0;
        for (Card card : cards)
             if (card.isFaceUp())
                 numOfFaceUpCards++;
        return numOfFaceUpCards;
    }
    
    public boolean hasMoreThanNFaceUpCards(int number) {
        return getNumOfFaceUpCards() > number;
    }
    
    public boolean hasList(ObservableList<Card> cards) {
        return this.cards.equals(cards);
    }
    
    public void remove(ObservableList<Card> cardsToRemove) {
        cards.removeAll(cardsToRemove);
    }
}
