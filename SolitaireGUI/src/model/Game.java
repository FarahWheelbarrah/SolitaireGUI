package model;

import model.CustomExceptions.BlankInputException;
import model.CustomExceptions.IncorrectInputTypeException;
import model.CustomExceptions.ReserveDeckEmptyException;
import model.CustomExceptions.SuccessfulTurnException;

public class Game {
    private Table activeTable;
    private int turnCount;
    
    // public static void main(String[] args) {
    //    new Game().start();
    // }
    
    public Game() {
        activeTable = new Table(7, 4);
    }
    
    // private void start() {
    //     setUpGame();
    //     printGameField();
    //     startMainMenuGameplay();
    // }

    // private void startMainMenuGameplay() {
    //     String selection;
    //     while (!isRestartingOrExiting((selection = Utilities.readString("Make Choice (m/d/p/r/x): ")), "r")) {
    //         try {
    //         switch (selection) {
    //             case "m": selectFromPileType(); break; // move a card
    //             case "d": fillDecks(); break; // fill reserve deck (or deck)
    //             case "p": printGameField(); break; 
    //             default: Utilities.parseInvalidSelection(selection, "\"" + selection + "\" is not a valid selection"); 
    //             }
    //         } catch (BlankInputException | IncorrectInputTypeException | ReserveDeckEmptyException e) {
    //             System.out.println(e.getMessage());
    //         } catch (SuccessfulTurnException e) {
    //             turnCount++;
    //             printGameField();
    //             if (e.hasGoalPileAsReceiver() && hasWon())
    //                 break;
    //         } 
    //     }
    //     if (isRestartingOrExiting(selection, "r"))
    //         restartOrExit(selection);
    //     else {
    //         // the player has won the game 
    //         System.out.println("Congratulations! You won the game in " + turnCount + " turns");
    //         endGameMenu();
    //     }
    // }
    
    private boolean isRestartingOrExiting(String input, String restartKey) {
        return input.equals("x") || input.equals(restartKey);
    }

    // private void restartOrExit(String selection) {
    //     switch (selection) {
    //         case "r": if (Utilities.isConfirmed("restart")) new Game().start(); else startMainMenuGameplay(); break;
    //         default: if (!Utilities.isConfirmed("exit")) startMainMenuGameplay();
    //     }
    // }
    
    private void setUpGame() {
        activeTable.setUpGame();
    }
    
    private void printGameField() {
        System.out.println(this);
    }
    
    // private void selectFromPileType() throws SuccessfulTurnException {
    //     activeTable.selectFromPileType();
    // }
    
    @Override
    public String toString() {
        return "______________________________\nTurns: " + turnCount + "\n" + activeTable + "\n______________________________";
    }
    
    private void fillDecks() throws ReserveDeckEmptyException, SuccessfulTurnException {
        activeTable.fillDecks();
        throw new SuccessfulTurnException(null);
    }
    
    private boolean hasWon() {
        return activeTable.hasWon();
    }
    
    // private void endGameMenu() {
    //     String selection;
    //     while (!isRestartingOrExiting((selection = Utilities.readString("Start a new game OR exit (s/x): ")), "s")) {
    //         try { 
    //             Utilities.parseInvalidSelection(selection, "\"" + selection + "\" is not a valid selection");
    //         } catch (BlankInputException | IncorrectInputTypeException e) {
    //             System.out.println(e.getMessage());
    //         }
    //     }
    //     if (selection.equals("s"))
    //         new Game().start();
    // }
}