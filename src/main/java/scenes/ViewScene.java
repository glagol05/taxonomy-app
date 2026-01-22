package scenes;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import controllers.Navigation;
import controllers.Queries;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import models.Creature;
import models.CreatureImage;

public class ViewScene {

    int currentRank = 0;
    String previousRank;
    List <Creature> currentCreatures;
    List <Creature> previousCreatures;
    List <String> currentRankList;

    String[] rankArray = {"domain", "kingdom", "phylum", "class_name", "order_name", "family", "genus", "species"};
    String[] selectedValues = new String[rankArray.length];

    public void creatureButton(BorderPane root, String rank, String mode) {
        Queries queries = new Queries();
        try {
            currentRankList = queries.getDistinctValuesFiltered(rankArray[currentRank], rankArray, selectedValues, currentRank - 1);
            currentCreatures = queries.getAllInSpecifics(rank, mode);
        } catch (SQLException e) {
            e.printStackTrace();
            currentRankList = List.of();
        }

        int targetRank = currentRank;

        if(mode.equals("forward") && currentRank < rankArray.length - 1) {
            targetRank = currentRank + 1;
        } else if(mode.equals("backward") && currentRank > 0) {
            targetRank = currentRank - 1;
        }

        String queryRank = rankArray[targetRank];

        HBox topBar = (HBox) root.getTop();
        if (topBar == null) {
            topBar = new HBox();
            topBar.setPadding(new Insets(10));
            topBar.setAlignment(Pos.TOP_LEFT);
            topBar.setMinHeight(50);
            root.setTop(topBar);
        } else {
            topBar.getChildren().clear();
        }

        if (currentRank > 0) {
            Button backButton = new Button("Return");
            int backRankIndex = currentRank - 1;
            backButton.setOnAction(e -> {
                selectedValues[currentRank] = null;
                currentRank--;
                creatureButton(root, rankArray[currentRank], "backward");
            });
            topBar.getChildren().add(backButton);
        }

        root.setTop(topBar);

        FlowPane pane = (FlowPane) root.getCenter();
        pane.getChildren().clear();

        for(int i = 0; i < currentRankList.size(); i++) {

            String item = currentRankList.get(i);
            String imgPath = "no_image.png";
            
            if (currentRank == rankArray.length - 1) {
                try {
                    List<Creature> creature = queries.getAllInSpecifics("species", item);

                    if (!creature.isEmpty()) {
                        int creature_id = creature.get(0).id();
                        CreatureImage stdImage = queries.getStandardImage(creature_id);
                        if (stdImage != null && stdImage.path() != null && !stdImage.path().isBlank()) {
                            imgPath = stdImage.path();
                        } else {
                            imgPath = "no_image.png";
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            Image buttonImg;
            try {
                File f = new File(imgPath);
                if (!f.exists()) {
                    f = new File("no_image.png");
                }
                buttonImg = new Image(f.toURI().toString(), 100, 100, true, true);
            } catch (Exception e) {
                buttonImg = new Image(new File("no_image.png").toURI().toString(), 100, 100, true, true);
            }
            ImageView iv = new ImageView(buttonImg);
            iv.setFitWidth(100);
            iv.setFitHeight(100);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);

            Button newButton = new Button(item);

            newButton.setGraphic(iv);
            newButton.setMinSize(140, 140);
            newButton.setPrefSize(140, 140);
            newButton.setMaxSize(140, 140);

            newButton.setContentDisplay(ContentDisplay.TOP);
            newButton.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: darkblue;");

            pane.getChildren().add(newButton);

            if(currentRank < rankArray.length - 1) {
                int nextRankIndex = currentRank + 1;
                newButton.setOnAction(e -> {
                    selectedValues[currentRank] = item;
                    currentRank++;
                    creatureButton(root, rankArray[currentRank], "forward");
                });
            } else {
                Navigation navigation = new Navigation(null);
                newButton.setOnAction(e -> {
                    navigation.
                });
            }
        }
    }

    String[] domainArray = {"Bacteria", "Archea", "Eukaryota"};
    
    public static Scene create(Navigation navigation) {
        ViewScene viewSceneController = new ViewScene();
        Queries queries = new Queries();
        List <Creature> creatures;
        try {
            creatures = queries.getAllInSpecifics("genus", "Canis");
        } catch (SQLException e) {
            e.printStackTrace();
            creatures = List.of();
        }

        BorderPane root = new BorderPane();
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.TOP_LEFT);
        topBar.setMinHeight(50);
        root.setTop(topBar);
        
        Scene viewScene = new Scene(root, 1200, 800);
        ListView<Text> animalList = new ListView<>();
        FlowPane centerPane = new FlowPane();
        centerPane.setHgap(10);
        centerPane.setVgap(10);
        centerPane.setAlignment(Pos.TOP_LEFT);
        centerPane.setPadding(new Insets(10, 0, 0, 10));
        centerPane.setPrefWrapLength(600);

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

        for(int i = 0; i < creatures.size(); i++) {
            Text sampleText = new Text(creatures.get(i).species());
            animalList.getItems().add(sampleText);
        }

        root.setRight(animalList);
        root.setBottom(bottomBar);
        root.setCenter(centerPane);
        BorderPane.setAlignment(bottomBar, Pos.CENTER);

        viewSceneController.creatureButton(root, "domain", "forward");

        return viewScene;
    }
}
