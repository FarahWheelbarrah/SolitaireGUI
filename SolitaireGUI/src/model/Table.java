package model;

import model.CustomExceptions.*;
import java.util.Iterator;
import java.util.LinkedList;
import javafx.collections.ObservableList;

public class Table {
    private Deck standardDeck;
    private ReserveDeck reserveDeck;
    private LinkedList<PlayPile> playPiles = new LinkedList<PlayPile>();
    private LinkedList<GoalPile> goalPiles = new LinkedList<GoalPile>();
    private final int numberOfPlayPiles;
    private final int numberOfGoalPiles;
    
    public Table(int numberOfPlayPiles, int numberOfGoalPiles) {
        this.numberOfPlayPiles = numberOfPlayPiles;
        this.numberOfGoalPiles = numberOfGoalPiles;
        standardDeck = new Deck();
        reserveDeck = new ReserveDeck(1);
        for (int i = 1; i <= numberOfPlayPiles; i++)
            playPiles.add(new PlayPile(i));
        for (int i = 1; i <= numberOfGoalPiles; i++)
            goalPiles.add(new GoalPile(i));
    }
    
    public final Deck getDeck() { 
        return standardDeck; 
    }
    
    public final ReserveDeck getReserveDeck() {
        return reserveDeck;
    }
    
    public LinkedList<PlayPile> getPlayPiles() {
        return playPiles;
    }
    
    public LinkedList<GoalPile> getGoalPiles() {
        return goalPiles;
    }
    
    public void setUpGame() {
        shuffle();
        deal();
    }
    
    public void shuffle() {
        standardDeck.shuffle();
    }
    
    public void deal() {
        for (PlayPile pile : playPiles)
            pile.deal(standardDeck);
    }
    
    @Override
    public String toString() {
        String tableString = "";
        tableString += standardDeck + "\n";
        tableString += reserveDeck + "\n";
        for (PlayPile pile : playPiles)
            tableString += pile + "\n";
        tableString += "             <-- Bottom     Top -->\n";
        for (GoalPile pile: goalPiles)
            tableString += pile + "\n";
        return tableString.trim();
    }
     
    //public void selectFromPileType() throws SuccessfulTurnException {
     //   String pileTypeSelection;
      //  while (!(pileTypeSelection = Utilities.readString("Select \"Giving\" Pile (r/p/g/b): ")).equals("b")) {
       //     try {
        //    switch (pileTypeSelection) {
         //       case "r": isEmpty(reserveDeck); selectReceivingPileType(reserveDeck); break;
          //      case "p": getGivingPile("Enter Play Pile number to remove card(s) from: ", playPiles); break;
           //     case "g": getGivingPile("Enter Goal Pile number to remove card from: ", goalPiles); break;
            //    default: Utilities.parseInvalidSelection(pileTypeSelection, "\"" + pileTypeSelection + "\" is not a valid selection");
             //   }
           // } catch (BlankInputException | IncorrectInputTypeException | EmptyPileException e) {
            //    System.out.println(e.getMessage());
           // }
       // }
    //}

    //private <T> void getGivingPile(String prompt, LinkedList<T> selectedGivingList) throws SuccessfulTurnException {
     //   String pileNumberSelection;
      //  while (!(pileNumberSelection = Utilities.readString(prompt)).equals("b")) {
       //     try {
        //        Pile givingPile = pile(Utilities.parseInt(pileNumberSelection, "\"" + pileNumberSelection + "\" is not a valid pile number"), selectedGivingList); 
         //       isEmpty(givingPile);
          //      if (givingPile instanceof GoalPile)
           //         invokeReceivingPile("Enter Play Pile number to add card (" + givingPile.getTopCard() + ") to: ", playPiles, givingPile); 
            //    else selectReceivingPileType(givingPile);
            //} 
            //catch (IncorrectInputTypeException | BlankInputException | SpecificPileNonExistentException | EmptyPileException e) {
             //   System.out.println(e.getMessage());
           // }
       // }
   // }
    
    //private <T> void selectReceivingPileType(Pile givingPile) throws SuccessfulTurnException {
      //  String pileTypeSelection;
      //  while (!(pileTypeSelection = Utilities.readString("Select \"Receiving\" Pile (p/g/b): ")).equals("b")) {
       //     try {
        //    switch (pileTypeSelection) {
         //       case "p": invokeReceivingPile("Enter Play Pile number to add card" + addingOneOrMore(givingPile) + " to: ", playPiles, givingPile); break;
          //      case "g": invokeReceivingPile("Enter Goal Pile number to add card (" + givingPile.getTopCard() + ") to: ", goalPiles, givingPile); break;
           //     default: Utilities.parseInvalidSelection(pileTypeSelection, "\"" + pileTypeSelection + "\" is not a valid selection");
            //    }
            //} catch (BlankInputException | IncorrectInputTypeException e) {
             //   System.out.println(e.getMessage());
           // }
       // }
   // }
    
    //private <T> void invokeReceivingPile(String prompt, LinkedList<T> selectedReceivingList, Pile givingPile) throws SuccessfulTurnException {
     //   String pileNumberSelection;
      //  while (!(pileNumberSelection = Utilities.readString(prompt)).equals("b")) {
       //     try {
        //        Pile receivingPile = pile(Utilities.parseInt(pileNumberSelection, "\"" + pileNumberSelection + "\" is not a valid pile number"), selectedReceivingList); 
         //       isSamePile(givingPile, receivingPile);
          //      Giver giver = (Giver)givingPile;
           //     Receiver receiver = (Receiver)receivingPile;
            //    giver.give(receiver);
           // } catch (IncorrectInputTypeException | BlankInputException | SpecificPileNonExistentException | SamePileException | ReceiveCardException e) {
           //     System.out.println(e.getMessage());
           // }
       // }
   // }
    
    private <T> Pile pile(int number, LinkedList<T> selectedList) throws SpecificPileNonExistentException {
        for (Iterator<T> it = selectedList.iterator(); it.hasNext();) {
            Pile pile = (Pile)it.next();
            if (pile.hasNumber(number))
                return (Pile)pile;
        }
        throw new SpecificPileNonExistentException(number, getPileTypeName(selectedList.size()));
    }
    
    private String getPileTypeName(int listSize) {
        if (listSize == numberOfGoalPiles)
            return "Goal Pile";
        else 
            return "Play Pile";
    }
    
    private void isEmpty(Pile givingPile) throws EmptyPileException {
        if (givingPile.isEmpty()) 
            throw new EmptyPileException();
    }
    
    private void isSamePile(Pile givingPile, Pile receivingPile) throws SamePileException {
        if (givingPile.equals(receivingPile))
            throw new SamePileException();
    }
    
    private String addingOneOrMore(Pile givingPile) {
        if (givingPile instanceof ReserveDeck) 
            return " (" + givingPile.getTopCard() + ")";
        else if (givingPile instanceof PlayPile && !givingPile.hasMoreThanNFaceUpCards(1))
                return " (" + givingPile.getTopCard() + ")"; 
        else 
            return "(s)";
    }
    
    public void fillDecks() throws ReserveDeckEmptyException {
        if (standardDeck.isEmpty()) {
            if (reserveDeck.isEmpty())
                throw new ReserveDeckEmptyException();
            standardDeck.receiveCardsFromReserveDeck(reserveDeck.getCards());
            reserveDeck.removeCards();
            }
        else 
            reserveDeck.receiveCards(standardDeck.getCardsToGive());
    }
    
    public boolean hasWon() {
        for (GoalPile goalPile : goalPiles) {
            if (!goalPile.isComplete())
                return false;
        }
        return true;
    }
    
    public Pile getCorrespondingPile(ObservableList<Card> listViewCards, LinkedList<Pile> listOfPilesToCheckFrom) {
        for (Pile pile : listOfPilesToCheckFrom)
            if (pile.hasList(listViewCards))
                return pile;
        return null;
    }
    
    public void moveCards(Pile pileFrom, Pile pileTo, ObservableList<Card> cardsToMove) throws ReceiveCardException, SuccessfulTurnException {
        ((Receiver)pileTo).receive(cardsToMove);
        pileFrom.remove(cardsToMove);

        // temporary:
        System.out.println(pileFrom);
        System.out.println(pileTo);
        for (Card card : cardsToMove)
            System.out.print(card + " ");
        System.out.println();
    }
}