package controllers;

import javafx.stage.Stage;
import scenes.AddScene;
import scenes.EntryScene;
import scenes.FindScene;
import scenes.MainScene;
import scenes.ViewScene;

public class Navigation {

    private final Stage stage;

    public Navigation(Stage stage) {
        this.stage = stage;
    }

    public void showMainScene() {
        stage.setScene(MainScene.create(this));
    }

    public void showAddScene() {
        stage.setScene(AddScene.create(this));
    }

    public void showViewScene() {
        stage.setScene(ViewScene.create(this));
    }

    public void showFindScene() {
        stage.setScene(FindScene.create(this));
    }

    public void showEntryScene(int creature_id) {
        stage.setScene(EntryScene.create(this));
    }
}
