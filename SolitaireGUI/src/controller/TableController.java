package controller;

import model.Table;
import au.edu.uts.ap.javafx.Controller;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.nio.ByteBuffer;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.util.Callback;
import model.Card;
import model.CustomExceptions.ReserveDeckEmptyException;
import model.GoalPile;
import model.Pile;
import model.Utilities;
import java.util.Random;
import java.util.function.IntPredicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.CustomExceptions.ReceiveCardException;
import model.CustomExceptions.SuccessfulTurnException;
import model.Deck;
import model.Giver;
import model.PlayPile;
import model.ReserveDeck;
import view.Styles;

// remove type parameter T from class declaration
public class TableController <T> extends Controller<Table> {
    
    @FXML private VBox rootVBox;
    @FXML private Button deckBtn;
    @FXML private ListView<Card> reserveDeckLv;
    @FXML private ListView<Card> playPile1Lv;
    @FXML private ListView<Card> playPile2Lv;
    @FXML private ListView<Card> playPile3Lv;
    @FXML private ListView<Card> playPile4Lv;
    @FXML private ListView<Card> playPile5Lv;
    @FXML private ListView<Card> playPile6Lv;
    @FXML private ListView<Card> playPile7Lv;
    @FXML private ListView<Card> goalPile1Lv;
    @FXML private ListView<Card> goalPile2Lv;
    @FXML private ListView<Card> goalPile3Lv;
    @FXML private ListView<Card> goalPile4Lv;
    
    @FXML private Button testBtn;
    
    private ListView<Card>[] playPileLvs;
    private ListView<Card>[] goalPileLvs;
    private ListView<Card>[] receivingPileLvs;
    
    private final HashMap<ListView<Card>, Pile> playPileHashMap = new HashMap<>();
    private final HashMap<ListView<Card>, Pile> goalPileHashMap = new HashMap<>();
    private final HashMap<ListView<Card>, Pile> reserveDeckHashMap = new HashMap<>();
    
    private Dragboard dragboard; // probably temporary
    private double mousePosX; // probably temporary
    private double mousePosY; // probably temporary
    
    public final Stage getStage() { return stage; }
    public final Table getTable() { return model; }
    public final Deck getDeck() { return getTable().getDeck(); } 
    public final ReserveDeck getReserveDeck() { return getTable().getReserveDeck(); }
    public final LinkedList<PlayPile> getPlayPiles() { return getTable().getPlayPiles(); }
    public final LinkedList<GoalPile> getGoalPiles() { return getTable().getGoalPiles(); }
    
    @FXML private void initialize() {
        playPileLvs = new ListView[] {playPile1Lv, playPile2Lv, playPile3Lv, playPile4Lv, playPile5Lv, playPile6Lv, playPile7Lv};
        goalPileLvs = new ListView[] {goalPile1Lv, goalPile2Lv, goalPile3Lv, goalPile4Lv};
        receivingPileLvs = new ListView[] {playPile1Lv, playPile2Lv, playPile3Lv, playPile4Lv, playPile5Lv, playPile6Lv, playPile7Lv, goalPile1Lv, goalPile2Lv, goalPile3Lv, goalPile4Lv};
        
        // set up Deck Button
        deckBtn.textProperty().bind(Bindings.createStringBinding(() -> (getDeck().isEmpty() ? "Restock" : "Draw"), getDeck().getCards()));
        deckBtn.disableProperty().bind(Bindings.and(Bindings.isEmpty(getDeck().getCards()), Bindings.isEmpty(getReserveDeck().getCards())));
        
        // set Up reserveDeck ListView (and reserveDeck HashMap)
        reserveDeckHashMap.put(reserveDeckLv, getReserveDeck());
        reserveDeckLv.prefWidthProperty().bind(Bindings.createIntegerBinding(() -> Utilities.getFirstCards(getReserveDeck(), 3).size() * 100 + 2, getReserveDeck().getCards()));
        reserveDeckLv.itemsProperty().bind(Bindings.createObjectBinding(() -> Utilities.getFirstCards(getReserveDeck(), 3), getReserveDeck().getCards()));
        reserveDeckLv.setCellFactory(param -> new UniformCell() {
            @Override
            public void updateItem(Card item, boolean empty) {
                super.updateItem(item, empty);
                setPrefWidth(100);
                setAlignment(Pos.CENTER);
                if (!empty) // can also write if (item != null) (may not even need this if statement - if (!empty))
                    disableProperty().bind(Bindings.notEqual(item, Bindings.valueAt(getReserveDeck().getCards(), getReserveDeck().getCards().size() - 1)));
            }
        });
        
        // set up playPile ListViews (and playPile HashMap)
        for (PlayPile playPile : getPlayPiles()) {
            playPileHashMap.put(playPileLvs[getPlayPiles().indexOf(playPile)], playPile);
            playPileLvs[getPlayPiles().indexOf(playPile)].setItems(playPile.getCards());
            playPileLvs[getPlayPiles().indexOf(playPile)].getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            playPileLvs[getPlayPiles().indexOf(playPile)].setCellFactory(param -> new UniformCell() {
                @Override
                public void updateItem(Card item, boolean empty) {
                    super.updateItem(item, empty); // maybe use isItemChanged() around this statement
                    setPrefHeight(23.95); // 266 / 11 - 0.05
                    disableProperty().bind(Bindings.createBooleanBinding(() -> (empty ? empty : !item.faceUpProperty().get()), emptyProperty()));
                }
            });
            playPile.getCards().addListener((ListChangeListener.Change<? extends Card> c) -> {
                if (playPile.getCards().size() * 23.95 > playPileLvs[getPlayPiles().indexOf(playPile)].getHeight())
                    playPileLvs[getPlayPiles().indexOf(playPile)].scrollTo(playPile.getCards().size() - 1);
            });
        }
        
        // set up goalPile ListViews (and goalPile HashMap)
        for (GoalPile goalPile : getGoalPiles()) {
            goalPileHashMap.put(goalPileLvs[getGoalPiles().indexOf(goalPile)], goalPile);
            goalPileLvs[getGoalPiles().indexOf(goalPile)].itemsProperty().bind(Bindings.createObjectBinding(() -> Utilities.getFirstCards(goalPile, 1), goalPile.getCards()));
            goalPileLvs[getGoalPiles().indexOf(goalPile)].setCellFactory(param -> new UniformCell() {
                @Override
                public void updateItem(Card item, boolean empty) {
                    super.updateItem(item, empty);
                    setPrefHeight(50);
                    setAlignment(Pos.CENTER);
                }
            });
        }
        
        // maybe use nested for loop (as below) and delete receivingPileLvs array
        // set up "receiving" ListViews EventHandlers
        for (ListView listView : receivingPileLvs)
            listView.setOnDragOver(new DragHandler() {
                @Override
                public void handle(DragEvent event) {
                    super.handle(event);
                    if (!getSourceNode().equals(getTargetNode()) && !(Utilities.isAnyOf(getSourceNode(), goalPileLvs) && Utilities.isAnyOf(getTargetNode(), goalPileLvs))) {
                        event.acceptTransferModes(TransferMode.MOVE);
                        getTargetNode().setStyle(Styles.DRAG_BORDER_COLOR);
                    }
                    event.consume();
            }
        });
        
        for (ListView<Card>[] listViewArray : new ListView[][] {playPileLvs, goalPileLvs})
            for (ListView<Card> listView : listViewArray)
                listView.setOnDragDropped(new DragHandler() {
                    @Override
                    public void handle(DragEvent event) {
                        super.handle(event);
                        try {
                            // temporary; here is where you will typecast the Piles into givers and receivers and call methods on them to transfer cards between them
                            // or maybe define a new method in the table class called "moveCards", that takes two parameters, the "giving" pile and the receiving pile,
                            // and typecasts them to a giver and a receiver, and makes the transfer in that method
                            getTable().moveCards(getSourcePile(), getTargetPile(), getSourceNode().getSelectionModel().getSelectedItems());
                        } catch (ReceiveCardException ex) {
                            System.out.println("error in moving cards");
                        } catch (SuccessfulTurnException ex) {
                            System.out.println("success");
                        }
                        // System.out.println(getSourcePile());
                        // System.out.println(getTargetPile());
                        getTargetNode().setStyle(null); // <-- probably unnecessary line of code
                    }
                });
        
        // set Up Root Node Eventhandler
        rootVBox.setOnDragDone(new DragHandler() {
            @Override
            public void handle(DragEvent event) {
               super.handle(event); // could have also taken the event and passed it as a constuctor parameter to the DragHandler class instead of doing it this way (which 
               // is making the DragHandler class implement the EventHandler<DragEvent> interface and overriding the handle() method)
               // System.out.println("-------");
               // for (Card card : getEventSource().getSelectionModel().getSelectedItems())
               //     System.out.println(card);
               getSourceNode().getSelectionModel().clearSelection();
               getSourceNode().setStyle(null);
               dragboard = null;
               // following lines will probably be removed when you implement borders for the listViews
                    // getSourceNode().getParent().requestFocus(); <-- may have to uncomment this line at some point
                    // other ways to accomplish the above line:
                    // Node node = (Node)event.getSource();
                    // node.requestFocus();
                    // rootVBox.requestFocus();
               // following line will probably be done in a CSS stylesheet
               // getEventSource().setStyle(null);
               // remove the "focused borders" on all of the ListCells and the ListViews, 
               // then make it so that when you hover over a ListView, it will have a red border, and if you press on a ListView, 
               // while the press is happening, the border will turn blue (if you release the mousebutton on the listCell, the border of the ListView
               // will turn back to red. If you then decide to initiate a drag, the ListView border will remain blue, and when you let go over the Drag (onDragDone)
               // the ListView border will disappear, unless you let go over a listView, in which case the ListView's border will turn to red
               // make it so that the border will not show up if you hover over an empty "giving" pile
               // OLD PLAN:
               // add code to remove the focus of the source ListView (maybe sourceListView.getParent()... something like this) (in which case it would be better to store the source ListView
               // in a variable)
            }
        });
        
        
        
        



        // for testing (temporary) vvvvvvvvvvvvvvvvvvvvvvvvvvv
        
        
        
        // border testing (might have to create a whole new array containing ALL of the ListViews in order to apply this to them (or just do it through FXML attributes))
        // reserveDeckLv.setOnMouseEntered(event -> {
        //    if (!((ListView<Card>)event.getSource()).getItems().isEmpty())
        //    ((ListView<Card>)event.getSource()).setStyle("-fx-border-color: green");
        //});
        
        // random line of code
        //playPile7Lv.setOnMousePressed(value);
        
        // these eventHandlers have to be assigned in the cellFactories, because we only want the border to change to green if a cell is selected (NOT the entire listView)
        // playPile7Lv.setOnMousePressed(event -> playPile7Lv.setStyle("-fx-border-color: green"));
        // playPile7Lv.setOnMouseReleased(event -> playPile7Lv.setStyle(null));
        
        // draghandlers will be implemented like this for all the piles that require them (create a new array that contains all the playPiles and all the goalPiles)
        // playPile7Lv.setOnDragOver(new DragHandler() {
        //  @Override
        //  public void handle(DragEvent event) {
        //        super.handle(event);
        //        if (!getEventSource().equals(event.getSource()) && !(Utilities.isAnyOf(getEventSource(), goalPileLvs) && Utilities.isAnyOf(getEventTarget(), goalPileLvs))) 
        //            event.acceptTransferModes(TransferMode.MOVE);
        //        event.consume();
        //    }
        // });
        
        // REMEMBER TO GET THE PARENT NODE BY CALLING stage.getScene().getRoot()
        // AND THEN ON THIS ROOT NODE, ADDING AN ONDRAGDONE HANDLER 
        
        //reserveDeckLv.setOnMouseReleased(event -> reserveDeckLv.getSelectionModel().clearSelection());
        
        
        // needs work (eventually will be assigned to each of the piles in a loop that loops through each of the ListViews i.e. an array of all of the ListViews that receive cards)
        /*goalPile4Lv.setOnDragOver(event -> {
                ListCell<Card> sourceListCell = (ListCell<Card>)event.getGestureSource();
                ListView<Card> sourceListView = sourceListCell.getListView();
                System.out.println(sourceListView);
                 if (!sourceListView.equals(goalPile4Lv)) {
                     event.acceptTransferModes(TransferMode.MOVE);
                     System.out.println("it worked");
                     //System.out.println(event.getGestureTarget()); 
                     // make the border change green or some other colour if the move can be made
                     // define an inner class for this EventHandler that will be reused by other parts in the code (maybe use generics)
                 }
                 event.consume();
             });*/
        
        // temporary testing of scrolling to end of list on play pile 7 before implementing it for all of the playPiles
        //playPile7Lv.getItems().addListener((ListChangeListener.Change<? extends Card> c) -> {
          //  if (playPile7Lv.getItems().size() * 23.95 > playPile7Lv.getHeight())
            //    playPile7Lv.scrollTo(playPile7Lv.getItems().size() - 1);
        //});
       
        // for (Pile goalPile : getGoalPiles())
        //     for (int i = 1; i <= 13; i++) {
        //         Card card = new Card("Spades", i, "Black");
        //         card.setFaceUp(true);
        //         goalPile.getCards().add(card);
        //     }
        
        testBtn.disableProperty().bind(Bindings.isEmpty(getReserveDeck().getCards()));
        
        //reserveDeckLv.setOnMouseExited(event -> reserveDeckLv.setStyle(null));
        
            

        // for testing (temporary) ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ 
        
        
        
        
        
        
        
        getTable().setUpGame();
    }

    private class UniformCell extends ListCell<Card> {
        @Override
        protected void updateItem(Card item, boolean empty) {
             super.updateItem(item, empty);
             textProperty().bind(Bindings.createStringBinding(() -> (empty ? null : item.toString()), emptyProperty()));
             
             // still needs work vvvvvvvvvvvvvvvvvvvvvvvvv
             
             // make it so a blue border is applied to the ListView when you press and hold on a ListView item
             setOnMousePressed(event -> {
                 getListView().getSelectionModel().selectRange(getListView().getItems().indexOf(item), getListView().getItems().size());
                 getListView().setStyle(Styles.DRAG_BORDER_COLOR);
             });
             // make it so the blue border disappears when you release the mouse button on the ListView item
             setOnMouseReleased(event -> {
                 getListView().getSelectionModel().clearSelection();
                 
                 
                 

                // testing - way to prevent the "dragging bug" vvvvvvvvvvvvvvvvvvvv
                 
                 //System.out.println("released");
                 //if (getListView().getBoundsInLocal().contains(event.getSceneX(), event.getSceneY()))
                     //System.out.println("Upper Left X: " + getListView().getBoundsInLocal().getMinX());
                     //System.out.println("Upper Left Y: " + getListView().getBoundsInLocal().getMinY());
                     //System.out.println("Lower Right X: " + getListView().getBoundsInLocal().getMaxX());
                     //System.out.println("Lower Right Y: " + getListView().getBoundsInLocal().getMaxY());
                     //System.out.println(getListView().getLayoutY());
                     //System.out.println(getListView().localToScene(getListView().getBoundsInLocal()));
                     //System.out.println(getListView().localToScene(getListView().getBoundsInLocal()).contains(event.getSceneX(), event.getSceneY()));
                     //System.out.println("Cursor X: " + event.getSceneX());
                     //System.out.println("Cursor Y: " + event.getSceneY());
                     getListView().setStyle((getListView().localToScene(getListView().getBoundsInLocal()).contains(event.getSceneX(), event.getSceneY()) ? Styles.HOVER_BORDER_COLOR : null));
                 
                 // testing - way to prevent the "dragging bug" ^^^^^^^^^^^^^^^^^^^^
                 
                 
                 
                 
                // rootVBox.requestFocus();
             });
             
             // IN PROGRESS: way to apply css to the nodes (change their background color) when they are hovered over in the same way that they are selected when they are pressed
             // setOnMouseEntered(event -> {
             // for (Node node : ((Node)getListView().getChildrenUnmodifiable()).lookupAll(""))
             //         System.out.println(node);
             // });

             // might need to put an if (item != null), so that the onDragDetected field is only set for non-empty cells (ACTUALLY PROBABLY NOT)
             setOnDragDetected(event -> {
                 ClipboardContent cc = new ClipboardContent();
                 cc.putString(item.toString());
                 dragboard = startDragAndDrop(TransferMode.MOVE);
                 dragboard.setContent(cc);
                 // System.out.println("i am currently in the process of dragging");
                 // following line will probably be done in a CSS stylesheet (MAYBE)
                 // ().setStyle("-fx-border-color: green");
                 getListView().setStyle(Styles.DRAG_BORDER_COLOR);
             });
             
             // still needs work ^^^^^^^^^^^^^^^^^^^^^^^^^
             
        }
    }
    
    
    
    
    
    @FXML public void testRemove() {
         // test method for removing the last card of a goalPile
         //getTable().getGoalPiles().get(0).getCards().remove(getTable().getGoalPiles().get(0).getCards().size() - 1);
    
        // test method for removing the last card of the reserveDeck
        //if (!getTable().getReserveDeck().getCards().isEmpty())
          //getTable().getReserveDeck().getCards().remove(getTable().getReserveDeck().getCards().size() - 1);
        
        // test method for adding cards to the first goalPile
        //if (!getTable().getGoalPiles().get(0).isComplete()) {
          //  Card card = new Card("Clubs", getTable().getGoalPiles().get(0).getCards().size() + 1, "Black");
          //  card.setFaceUp(true);
          //  getTable().getGoalPiles().get(0).getCards().add(card);
        //}
        
        // test method for adding 1 card to all of the PlayPiles (testing the scrollTo() method/procedure as well)
        for (PlayPile playPile : getTable().getPlayPiles()) {
            Card card = new Card("Hearts", getTable().getPlayPiles().indexOf(playPile) + 1, "Red");
            card.setFaceUp(true);
            playPile.getCards().add(card);
        }
        //playPile7Lv.scrollTo(playPile7Lv.getItems().size() - 1);
 
        // test method for flipping all the playPile cards to a faceUp state
        //for (Pile playPile : getPlayPiles())
          //  for (Card card : playPile.getCards())
            //    card.setFaceUp(true);

        // test method for adding a card to playPile 7 
        //Card card = new Card("Hearts", 1, "Red");
        //card.setFaceUp(true);
        //getTable().getPlayPiles().get(6).getCards().add(card);
        
        // test method for removing a card from playPile 7 
        //getTable().getPlayPiles().get(6).getCards().remove(getTable().getPlayPiles().get(6).getCards().size() - 1);
        //System.out.println(playPile7Lv.getItems().size());
    }
    
    
    
    
    
    
    @FXML public void fillDecks(ActionEvent event) throws ReserveDeckEmptyException {
        getTable().fillDecks();
    }
    
    // try make this class typesafe (i.e. compatible with ANY handler) (maybe use a type parameter in this class which extends a class that all Nodes have in common (MAYBE!!!))
    // maybe typecast the variables in the overriden handle(DragEvent event) method instead of doing it here, or go back to the "EventContainer" idea you had before (BOTH MAYBE!!!)
    // the content of the previous line might be implemented in order to guard against "foreign" objects (i.e. files, images, Strings, etc - anything other than cards/ListCell<Card>
    // objects being dragged from the user's computer desktop to one of the ListViews)
    private class DragHandler implements EventHandler<DragEvent> {
        private DragEvent event;
        private final HashMap[] hashMapArray = {playPileHashMap, goalPileHashMap, reserveDeckHashMap};
        
        @Override
        public void handle(DragEvent event) {
            this.event = event;
        }
        
        public ListView<Card> getSourceNode() {
            return ((ListCell<Card>)event.getGestureSource()).getListView();
        }
        
        public ListView<Card> getTargetNode() {
            return (ListView<Card>)event.getSource();
        }
        
        public Pile getSourcePile() {
            return Utilities.getValueUsingKey(getSourceNode(), hashMapArray);
        }
        
        public Pile getTargetPile() {
            return Utilities.getValueUsingKey(getTargetNode(), hashMapArray);
        }
    }

    @FXML public void applyHoverBorder(MouseEvent event) {
        if (!((ListView<Card>)event.getSource()).getItems().isEmpty())
            ((ListView<Card>)event.getSource()).setStyle(Styles.HOVER_BORDER_COLOR);
        // could do this with the DragHandler class if you rework it as described as follows: 
        // you make it so that the DragHandler inner class does not implement the EventHandler interface, so that you can 
        // just put the event into the constructor parameter of the DragHandler class and then use this DragHandler class in FXML annotated methods
    }

    @FXML public void removeHoverBorder(MouseEvent event) {
        // look at this if statement again, YOU MIGHT NOT EVEN NEED THE FIRST CONDITION, MAYBE TRY TO RETHINK IT
        if (dragboard == null && ((ListView<Card>)event.getSource()).getSelectionModel().isEmpty())
            ((ListView<Card>)event.getSource()).setStyle(null);
    }
    
    @FXML public void removeDragBorder(DragEvent event) {
        if (!((ListView<Card>)event.getSource()).equals(((ListCell<Card>)event.getGestureSource()).getListView()))
            ((ListView<Card>)event.getSource()).setStyle(null);
        // can also write: if ((ListView<Card>)event.getSource() != ((ListCell<Card>)event.getGestureSource()).getListView())
        // could do this with the DragHandler class if you rework it as described as follows: 
        // you make it so that the DragHandler inner class does not implement the EventHandler interface, so that you can 
        // just put the event into the constructor parameter of the DragHandler class and then use this DragHandler class in FXML annotated methods
    }

    






    // probably temporary - MOST LIKELY WILL DELETE
    @FXML public void trackMousePos(MouseEvent event) {
            mousePosX = event.getSceneX();
            mousePosY = event.getSceneY();
            System.out.println("X coordinate: " + mousePosX);
            System.out.println("Y coordinate: " + mousePosY);
    }
    
    // maybe (PROBABLY) delete this function (and thus, delete the function (of the same name) from the Table class)
    public Pile getCorrespondingPile(ObservableList<Card> listViewCards, LinkedList<Pile> listOfPilesToCheckFrom) {
        return getTable().getCorrespondingPile(listViewCards, listOfPilesToCheckFrom);
    }
    
    // ideas to get the list of ListCells for the ListViews (NEEDS WORK): 
    // in the updateItem method, add the following code:
    // if (!empty && !listCellListList.get(getPlayPiles().indexOf(playPile)).contains(this))
    //                    if (!initDone)
    //                        listCellListList.get(getPlayPiles().indexOf(playPile)).add(0, this);
    //                    else 
    //                        listCellListList.get(getPlayPiles().indexOf(playPile)).add(this);
    //                else 
    //                    listCellListList.get(getPlayPiles().indexOf(playPile)).remove(this);
    // the boolean value "initDone" will be declared as an instance variable in this class (the TableController class): private boolean initDone;
    // you can declare the set of Lists as an ObservableList of ObservableLists: private final ObservableList<ObservableList<ListCell<Card>>> listCellListList = FXCollections.observableArrayList();
    // and then fill it up like so:
    // for (int i = 1; i <= getPlayPiles().size(); i++)
    //        listCellListList.add(FXCollections.observableArrayList());
    // OR:
    // you can (more preferably) use an array of ObservableLists, like so: // ObservableList<ListCell<Card>>[] listCellListArray = new ObservableList[getPlayPiles().size()];
    // and then fill it up like so:
    // for (ObservableList list : listCellListArray)
    //      list = FXCollections.observableArrayList(); (doesn't seem to work; figure out why this is) (maybe you have to using an array index to loop through the array instead of a
    // for-each loop)
    // TO TEST IT:
    // for (ObservableList<ListCell<Card>> list : listCellListList) {
    //        System.out.println("------");
    //        for (ListCell<Card> cell : list)
    //           System.out.println(cell.getItem());
    //    }
    // ANOTHER IDEA IS TO USE THE CHILDREN OF THE LISTVIEW TO GET THE LISTCELLS:
    // (BUT YOU WILL MOST LIKELY NOT DO IT LIKE THIS)
    // System.out.println(playPile7Lv.getChildrenUnmodifiable());
    // for (Node node : playPile7Lv.getChildrenUnmodifiable()) 
    //    for (Node node2 : ((VirtualFlow)node).getChildrenUnmodifiable())
    //        for (Node node3 : ((Parent)node).getChildrenUnmodifiable())
    //            System.out.println(node3);
}