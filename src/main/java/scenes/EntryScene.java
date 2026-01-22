package scenes;

import java.sql.SQLException;

import controllers.Navigation;
import controllers.Queries;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import models.Creature;

public class EntryScene {
    
    public static Scene create(Navigation navigation, String speciesName, ViewScene viewSceneController) {
        Queries queries = new Queries();
        Creature speciesInfo = null;
        try {
            speciesInfo = queries.getCreature(speciesName);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        BorderPane root = new BorderPane();
        Scene entryScene = new Scene(root, 1200, 800);

        HBox topBar = new HBox();
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.TOP_LEFT);
        topBar.setSpacing(10);

        Button returnButton = new Button("← Return");
        returnButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        returnButton.setOnAction(e -> {
            viewSceneController.creatureButton(
                    (BorderPane) viewSceneController.getRoot(),
                    viewSceneController.rankArray[viewSceneController.currentRank],
                    "stay",
                    navigation);
            navigation.showViewScene();
        });

        topBar.getChildren().add(returnButton);
        root.setTop(topBar);

        FlowPane contentPane = new FlowPane();
        contentPane.setPadding(new Insets(20));
        contentPane.setVgap(10);
        contentPane.setHgap(10);
        contentPane.setAlignment(Pos.TOP_LEFT);

        if (speciesInfo != null) {
            Text scientificName = new Text(speciesInfo.species());
            scientificName.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

            Text description = new Text(speciesInfo.description() != null ? speciesInfo.description() : "");
            description.setWrappingWidth(700);

            VBox nameDescriptionBox = new VBox(10);
            nameDescriptionBox.getChildren().addAll(scientificName, description);

            VBox taxonomyBox = new VBox(5);
            taxonomyBox.setPadding(new Insets(10));
            taxonomyBox.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
            taxonomyBox.getChildren().addAll(
                new Text("Domain: " + speciesInfo.domain()),
                new Text("Kingdom: " + speciesInfo.kingdom()),
                new Text("Phylum: " + speciesInfo.phylum()),
                new Text("Class: " + speciesInfo.className()),
                new Text("Order: " + speciesInfo.order()),
                new Text("Family: " + speciesInfo.family()),
                new Text("Genus: " + speciesInfo.genus()),
                new Text("Species: " + speciesInfo.species()),
                new Text("Common Name: " + speciesInfo.common_name())
            );

            HBox contentHBox = new HBox(20);
            contentHBox.getChildren().addAll(nameDescriptionBox, taxonomyBox);
            contentPane.getChildren().add(contentHBox);
        }

        root.setCenter(contentPane);

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

        return entryScene;
    }
}
