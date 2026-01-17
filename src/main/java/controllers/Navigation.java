package controllers;

import javafx.stage.Stage;
import scenes.MainScene;
import scenes.SecondScene;

public class Navigation {

    private final Stage stage;

    public Navigation(Stage stage) {
        this.stage = stage;
    }

    public void showMainScene() {
        stage.setScene(MainScene.create(this));
    }

    public void showSecondScene() {
        stage.setScene(SecondScene.create(this));
    }
    
}
