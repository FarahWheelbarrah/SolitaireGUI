package program;

import au.edu.uts.ap.javafx.ViewLoader;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Table;

public class SolitaireGUIApplication extends Application {
    
    public static void main(String[] args) { launch(args); }
    
    @Override
    public void start(Stage stage) throws Exception {
         ViewLoader.showStage(new Table(7, 4), "/view/table.fxml", "Solitaire", stage);
    }
    
}
