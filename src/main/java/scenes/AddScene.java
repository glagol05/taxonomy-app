package scenes;

import controllers.Navigation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class AddScene {

    public static Scene create(Navigation navigation) {
        BorderPane root = new BorderPane();
        Scene addScene = new Scene(root, 800, 600);
        HBox bottomBar = new HBox();

        Button addEntry = new Button("add");
        addEntry.setOnAction(e -> navigation.showAddScene());
        
        Button viewEntries = new Button("view");
        viewEntries.setOnAction(e -> navigation.showViewScene());
        Button findEntry = new Button("find");
        findEntry.setOnAction(e -> navigation.showFindScene());
        Button home = new Button("home");
        home.setOnAction(e -> navigation.showMainScene());

        bottomBar.getChildren().addAll(addEntry, viewEntries, findEntry, home);
        root.setBottom(bottomBar);

        return addScene;
    }
    
}
