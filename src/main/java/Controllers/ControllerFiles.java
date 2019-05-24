package Controllers;

import Others.Buttons;
import Others.RegexManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ControllerFiles extends Controller{

    ControllerFiles(String name, Controller previousController) {
        super(name,previousController);
    }

    @FXML
    ListView<String> categoriesList;
    @FXML
    TextField newFileName;
    @FXML
    Button back;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> categories = FXCollections.observableArrayList(getCategories(false));
        categoriesList.setItems(categories);
        categoriesList.setOnMouseClicked(mouseEvent -> {
            if (categoriesList.getSelectionModel().getSelectedItems().get(0) != null) {
                newFileName.setDisable(false);
                newFileName.setEditable(true);
            }
        });
        Buttons buttons = new Buttons();
        back.setGraphic(buttons.setCustomImage("back"));
    }

    @FXML
    public void submitFileName(){
        String path = RegexManager.categoryToPath(categoriesList.getSelectionModel().getSelectedItem());
        newFileName.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String file = path.concat(String.valueOf(newFileName.getCharacters()));
                createFile(file);
                try { goBack(); } catch (IOException e) {e.printStackTrace();}
            }
        });

    }
    private void createFile(String file) {
        System.out.println(file);
        File newFile = new File(file);
        OutputStream out = null;
        try {
            out = new FileOutputStream(newFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Objects.requireNonNull(out).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
