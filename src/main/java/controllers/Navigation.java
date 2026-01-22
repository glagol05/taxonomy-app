package controllers;

import javafx.scene.Scene;
import javafx.stage.Stage;
import scenes.AddScene;
import scenes.EntryScene;
import scenes.FindScene;
import scenes.MainScene;
import scenes.ViewScene;

public class Navigation {

    private final Stage stage;
    private ViewScene viewSceneController;

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
        if (viewSceneController == null) {
            viewSceneController = new ViewScene();
            Scene scene = viewSceneController.create(this);
            stage.setScene(scene);
        } else {
            stage.setScene(viewSceneController.getRoot().getScene());
        }
    }

    public void showFindScene() {
        stage.setScene(FindScene.create(this));
    }

    public void showEntryScene(String speciesName, ViewScene previousView) {
        Scene entry = EntryScene.create(this, speciesName, previousView);
        stage.setScene(entry);
    }
}
