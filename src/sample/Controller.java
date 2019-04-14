package sample;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class Controller implements Initializable {

    static StageMaster stageMaster;

    String name;
    Controller previousController;


    public String getName() {
        return this.name;
    }

    public Controller getPreviousController(){
        return this.previousController;
    }

    public void setPreviousScene(Controller controller){
        this.previousController = controller;
    }


    public Controller() { }

    public Controller(String name) {
        this.name = name;
    }

    public Controller(String name, Controller previousController) {
        this.name = name;
        this.previousController = previousController;
    }


    @Override
    public abstract void initialize(URL url, ResourceBundle resourceBundle);

}
