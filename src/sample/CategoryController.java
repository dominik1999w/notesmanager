package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class CategoryController implements Initializable {
    @FXML
    ListView<String> categories_list;
    @FXML
    TextField newFileName;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        ObservableList<String> categories = FXCollections.observableArrayList("a","b","c");
        categories_list.setItems(categories);
        categories_list.setOnMouseClicked(mouseEvent -> {
                    newFileName.setDisable(false);
                    newFileName.setEditable(true);
                }
        );
    }
    public void submitFileName(){
        String a="categories/" +categories_list.getSelectionModel().getSelectedItems().get(0)+"/";
        newFileName.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String b=a.concat(String.valueOf(newFileName.getCharacters()));
                createFile(b);
            }
        });

    }
    private void createFile(String a) {
        System.out.println(a);
        File newFile = new File(a);
        OutputStream out = null;
        try {
            out=new FileOutputStream(newFile);
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
