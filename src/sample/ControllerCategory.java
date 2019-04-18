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

public class ControllerCategory extends Controller{

    public ControllerCategory(String name, Controller previousController) {
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


    public void goBackToMainScene() throws IOException {
        Controller.stageMaster.loadPreviousScene();
    }

    public void submitFileName(){
        String a = "categories/" + categories_list.getSelectionModel().getSelectedItems().get(0) + "/";
        newFileName.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String b = a.concat(String.valueOf(newFileName.getCharacters()));
                createFile(b);
                try { goBackToMainScene(); } catch (IOException e) {}
            }
        });

    }

    private void createFile(String a) {
        System.out.println(a);
        File newFile = new File(a);
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
