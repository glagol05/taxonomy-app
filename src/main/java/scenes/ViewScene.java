package scenes;

import controllers.Navigation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class ViewScene {

    public static Scene create(Navigation navigation) {
        BorderPane root = new BorderPane();
        Scene viewScene = new Scene(root, 800, 600);
        ListView<Text> animalList = new ListView<>();
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

        Text sampleText = new Text("Hiyyyaa test");
        animalList.getItems().addAll(sampleText);
        root.setRight(animalList);
        root.setBottom(bottomBar);

        return viewScene;
    }
    
}
