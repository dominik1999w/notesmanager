package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class CategoryController{
    Stage previousStage;
    public void setPreviousStage(Stage stage){
        previousStage=stage;
    }
    @FXML
    Button Back_Button;
    public void goBackToMainScene() throws IOException {
        Stage stage = new Stage();
        Pane myPane = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene scene = new Scene(myPane);
        stage.setScene(scene);
        stage.show();

    }

}
