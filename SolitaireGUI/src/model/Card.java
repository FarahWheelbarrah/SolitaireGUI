package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Card {
    private final String suit;
    private final int number;
    private final String color;
    private BooleanProperty faceUp = new SimpleBooleanProperty(); // might need to declare this property as "transient" and implement the Serializable interface (PROBABLY NOT)
  
    public Card(String suit, int number, String color) {
        this.suit = suit;
        this.number = number;
        this.color = color;
    }
    
    private String value() {
        switch (number) {
            case 1: return "A";
            case 11: return "J";
            case 12: return "Q";
            case 13: return "K";
            default: return "" + number;
        }
    }
    
    private String color() {
        if (color.equals("Red"))
            return "R";
        else 
            return "B";
    }
    
    private String suit() {
        switch (suit) {
            case "Hearts": return "H";
            case "Diamonds": return "D";
            case "Spades": return "S";
            case "Clubs": return "C";
        }
        return null;
    }
    
    public boolean hasNumber(int number) {
        return this.number == number;
    }
    
    public final String getSuit() {
        return suit;
    }
    
    public final int getNumber() {
        return number;
    }
    
    public final boolean isFaceUp() {
        return faceUp.get();
    }
    
    public final String getColor() {
        return color;
    }
    
    public boolean hasColor(String color) {
        return this.color.equals(color);
    }
    
    public boolean hasSuit(String suit) {
        return this.suit.equals(suit);
    }
    
    public boolean hasDifference(int number, int difference) {
        return number - this.number == difference;
    }
    
    public final void setFaceUp(boolean faceUp) {
        this.faceUp.set(faceUp);
    }
    
    public BooleanProperty faceUpProperty() {
        return faceUp;
    }
    
    @Override
    public String toString() {
        if (isFaceUp())
            return value() + "-" + suit() + "-" + color();
        else 
            return "FDC"; // FDC: "Face Down Card"
    }
}
