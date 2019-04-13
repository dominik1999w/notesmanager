package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
public class Launcher extends Application{
    private static final String MAIN_FXML = "sample.fxml";
    private static final String SCENE_ONE_FXML = "CategoryClass.fxml";
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Pane myPane = loader.load();
        Controller controller = loader.getController();
        controller.setPreviousStage(primaryStage);
        Scene scene = new Scene(myPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
