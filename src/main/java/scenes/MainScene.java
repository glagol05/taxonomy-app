package scenes;

import controllers.Navigation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class MainScene {
    
    public static Scene create(Navigation navigation) {
        BorderPane root = new BorderPane();
        Scene mainScene = new Scene(root, 800, 600);

        Button back = new Button("Second Scene");
        root.setCenter(back);
        back.setOnAction(e -> navigation.showSecondScene());

        return mainScene;
    }


}
