package model;

import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import model.CustomExceptions.BlankInputException;
import model.CustomExceptions.IncorrectInputTypeException;

public class Utilities {
    
    public static void parseInvalidSelection(String selection, String errorMessage) throws BlankInputException, IncorrectInputTypeException {
        if (selection.isEmpty() || selection.equals(""))
            throw new BlankInputException("Please enter a selection value");
        throw new IncorrectInputTypeException(errorMessage);
    }
    
    public static String readString(String prompt) {
        System.out.print(prompt);
        return In.nextLine().trim();
    }
    
    public static int parseInt(String input, String invalidMessage) throws IncorrectInputTypeException, BlankInputException {
        if (input.isEmpty() || input.equals(""))
            throw new BlankInputException("Please enter a value");
        if (!isInt(input))
            throw new IncorrectInputTypeException(invalidMessage);
         return Integer.parseInt(input);
    }
    
    public static boolean isInt(String input) {
        try {
            Integer.parseInt(input);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean isConfirmed(String action) {
        String selection;
        while (!(selection = Utilities.readString("Are you sure you want to " + action + "?(y/n): ")).equals("n")) {
            switch (selection) {
                case "y": return true;
                default:
                try {
                    parseInvalidSelection(selection, "\"" + selection + "\" is not a valid selection");
                } catch (IncorrectInputTypeException | BlankInputException e) {
                    System.out.println(e.getMessage());
                    }
                }
            }
        return false;
    }
    
    public static ObservableList<Card> getFirstCards(Pile pile, int numberOfCards) {
        ObservableList<Card> topCards = FXCollections.observableArrayList();
        for (int i = pile.getCards().size() - 1; i >= pile.getCards().size() - numberOfCards; i--) {
            if (i < 0)
                break;
            topCards.add(0, pile.getCards().get(i));
        }
        return topCards;
    }
    
    public static <T> boolean isAnyOf(T objectToCheck, T[] arrayToCheckFrom) {
        for (T object : arrayToCheckFrom)
            if (object.equals(objectToCheck))
                return true;
        return false;
    }
    
        
    public static <K, R> R getValueUsingKey(K key, Map[] mapArray) { // instead of using type parameter, can also make the function parameter a Node object
        for (Map map : mapArray)                // can also use the AbstractMap class as the type parameter (instead of the Map interface)
            if (map.get(key) != null) {         // can also return a type of Object, in which case you will have to typecast return value in calling function (DON'T DO THIS)
                return (R)map.get(key);
            }
        return null;
    }
}
