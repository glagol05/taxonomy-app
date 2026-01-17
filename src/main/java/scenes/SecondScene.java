package scenes;

import controllers.Navigation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class SecondScene {
    
    public static Scene create(Navigation navigation) {
        BorderPane root = new BorderPane();
        Scene secondScene = new Scene(root, 800, 600);

        Button back = new Button("Main Scene");
        root.setCenter(back);
        back.setOnAction(e -> navigation.showMainScene());
        
        return secondScene;
    }
}
