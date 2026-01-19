package scenes;

import java.sql.SQLException;

import controllers.Navigation;
import controllers.Queries;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class AddScene {

    public static Scene create(Navigation navigation) {
        BorderPane root = new BorderPane();
        Scene addScene = new Scene(root, 800, 600);
        HBox bottomBar = new HBox(10);
        bottomBar.setPrefHeight(50);
        bottomBar.setMinWidth(600);
        bottomBar.setMaxWidth(600);
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setBackground(new Background(new BackgroundFill(Color.SKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        VBox inputBox = new VBox(10);
        inputBox.setPrefWidth(300);
        inputBox.setMaxWidth(300);
        inputBox.setAlignment(Pos.CENTER);

        StackPane centerPane = new StackPane(inputBox);
        centerPane.setAlignment(Pos.CENTER);

        TextField domainInput = new TextField();
        TextField kingdomInput = new TextField();
        TextField phylumInput = new TextField();
        TextField classInput = new TextField();
        TextField orderInput = new TextField();
        TextField familyInput = new TextField();
        TextField genusInput = new TextField();
        TextField speciesInput = new TextField();
        Button addCreature = new Button();

        domainInput.setPromptText("Domain");
        kingdomInput.setPromptText("Kingdom");
        phylumInput.setPromptText("Phylum");
        classInput.setPromptText("Class");
        orderInput.setPromptText("Order");
        familyInput.setPromptText("Family");
        genusInput.setPromptText("Genus");
        speciesInput.setPromptText("Species");

        TextField[] fields = {
            domainInput, kingdomInput, phylumInput, classInput,
            orderInput, familyInput, genusInput, speciesInput
        };

        for (TextField tf : fields) {
            tf.setMaxWidth(300);
        }

        inputBox.getChildren().addAll(domainInput, kingdomInput, phylumInput, classInput, orderInput, familyInput, genusInput, speciesInput);

        addCreature.setOnAction(e -> {
            Queries queries = new Queries();

            try {
                queries.addEntry(
                    domainInput.getText(),
                    kingdomInput.getText(),
                    phylumInput.getText(),
                    classInput.getText(),
                    orderInput.getText(),
                    familyInput.getText(),
                    genusInput.getText(),
                    speciesInput.getText()
                );
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

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
        root.setCenter(centerPane);
        BorderPane.setAlignment(bottomBar, Pos.CENTER);

        return addScene;
    }
    
}
