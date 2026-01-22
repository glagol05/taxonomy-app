package scenes;

import controllers.Navigation;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class FindScene {

    public static Scene create(Navigation navigation) {
        BorderPane root = new BorderPane();
        Scene findScene = new Scene(root, 1200, 800);
        HBox bottomBar = new HBox(10);
        bottomBar.setPrefHeight(50);
        bottomBar.setMinWidth(800);
        bottomBar.setMaxWidth(800);
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setBackground(new Background(new BackgroundFill(Color.SKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

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
        BorderPane.setAlignment(bottomBar, Pos.CENTER);

        return findScene;
    }
    
}
