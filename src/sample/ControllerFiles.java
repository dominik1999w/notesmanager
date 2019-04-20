package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ControllerFiles extends Controller{

    public ControllerFiles(String name, Controller previousController) {
        super(name,previousController);
    }

    @FXML
    ListView<String> categories_list;

    @FXML
    TextField newFileName;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        ObservableList<String> categories = FXCollections.observableArrayList(getCategories(false));
        categories_list.setItems(categories);
        categories_list.setOnMouseClicked(mouseEvent -> {
            if(categories_list.getSelectionModel().getSelectedItems().get(0) != null) {
                newFileName.setDisable(false);
                newFileName.setEditable(true);
            }
        });
    }


    public void submitFileName(){
        String path = regexManager.categoryToPath(categories_list.getSelectionModel().getSelectedItem());
        newFileName.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String file = path.concat(String.valueOf(newFileName.getCharacters()));
                createFile(file);
                try { goBack(); } catch (IOException e) {}
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
