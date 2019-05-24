package Management;

import Controllers.Controller;
import Controllers.ControllerPrimary;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setMinHeight(800);
        primaryStage.setMinWidth(1350);
        primaryStage.setHeight(1000);
        primaryStage.setWidth(1351);
        primaryStage.setTitle("NotesManager");
        //Make first primary controller, set its name. It will create static field for
        //Controller named StageMaster, who will receive primaryStage and will store & use it for every scene.
        Controller controllerPrimary = new ControllerPrimary("/Scenes/sample.fxml", primaryStage);
        //Static stageMaster is loading first scene using controllerPrimary.
        Controller.stageMaster.loadNewScene(controllerPrimary);
    }

    public static void main(String[] args) { launch(args); }

}
