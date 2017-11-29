package model;

import javafx.collections.ObservableList;
import model.CustomExceptions.ReceiveCardException;
import model.CustomExceptions.SuccessfulTurnException;

public interface Giver {
   public void give(ObservableList<Card> receiver) throws SuccessfulTurnException, ReceiveCardException;
}
