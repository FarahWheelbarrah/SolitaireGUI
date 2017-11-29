package model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Deck {
    private ObservableList<Card> cards = FXCollections.observableArrayList();
    
    public Deck() {
        for (String suit : new String[] { "Spades", "Diamonds", "Hearts", "Clubs" })
            for (int number = 1; number <= 13; number++)
                cards.add(new Card(suit, number, color(suit)));
    }
    
    public ObservableList<Card> getCards() {
        return cards;
    }
    
    public void shuffle() {
        Collections.shuffle(cards);
    }
    
    public Card removeTopCard() {
        return cards.remove(cards.size() - 1);
    }
    
    private String color(String suit) {
        if (suit.equals("Hearts") || suit.equals("Diamonds"))
            return "Red";
        else 
            return "Black";
    }
    
    @Override
    public String toString() {
        String standardDeckString = "Deck: ";
        if (!cards.isEmpty())
            standardDeckString += "(Cards Remaining: " + cards.size() + ")";
        else
            standardDeckString += "(Empty)";
        return standardDeckString;
    }
    
    public boolean isEmpty() {
        return cards.isEmpty();
    }
    
    public LinkedList<Card> getCardsToGive() {
        LinkedList<Card> cardsToGive = new LinkedList<Card>();
        for (int i = cards.size() - 1; i >= cards.size() - 3; i--) {
            if (i < 0)
                break;
            cards.get(i).setFaceUp(true);
            cardsToGive.add(cards.get(i));
        }
        cards.removeAll(cardsToGive);
        return cardsToGive;
    }
    
    public void receiveCardsFromReserveDeck(ObservableList<Card> cardsToReceive) {
        for (Card card : cardsToReceive) {
            card.setFaceUp(false);
            cards.add(0, card);
        }
    }
}
