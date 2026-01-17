import controllers.Navigation;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Navigation navigation = new Navigation(primaryStage);
        navigation.showMainScene();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
