package scenes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import controllers.Navigation;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import controllers.Queries;
import models.Creature;

public class ViewScene {

    public static Scene create(Navigation navigation) {
        Queries queries = new Queries();
        List <Creature> creatures;
        try {
            creatures = queries.getAllInDomain("Eukaryota");
        } catch (SQLException e) {
            e.printStackTrace();
            creatures = List.of();
        }

        BorderPane root = new BorderPane();
        Scene viewScene = new Scene(root, 800, 600);
        ListView<Text> animalList = new ListView<>();
        HBox bottomBar = new HBox(10);
        bottomBar.setPrefHeight(50);
        bottomBar.setMinWidth(600);
        bottomBar.setMaxWidth(600);
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

        Text sampleText = new Text(creatures.get(0).species());
        animalList.getItems().addAll(sampleText);
        root.setRight(animalList);
        root.setBottom(bottomBar);
        BorderPane.setAlignment(bottomBar, Pos.CENTER);

        return viewScene;
    }
    
}
