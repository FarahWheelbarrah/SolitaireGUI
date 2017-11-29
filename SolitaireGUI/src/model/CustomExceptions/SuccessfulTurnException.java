package model.CustomExceptions;

import model.Receiver;
import model.GoalPile;

public class SuccessfulTurnException extends Exception {
    private Receiver receiver;
    
    public SuccessfulTurnException(Receiver receiver) {
        this.receiver = receiver;
    }
    
    public boolean hasGoalPileAsReceiver() {
        return receiver instanceof GoalPile;
    }
}
