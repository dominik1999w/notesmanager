package sample;

import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {

    private static final String MAIN_FXML = "sample.fxml";
    private static final String SCENE_ONE_FXML = "CategoryClass.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Make first primary controller, set its name. It will create static field for
        //Controller named StageMaster, who will receive primaryStage and will store & use it for every scene.
        Controller controllerPrimary = new ControllerPrimary("sample.fxml", primaryStage);
        //Static stageMaster is loading first scene using controllerPrimary.
        Controller.stageMaster.loadNewScene(controllerPrimary);
    }

    public static void main(String[] args) { launch(args); }

}
