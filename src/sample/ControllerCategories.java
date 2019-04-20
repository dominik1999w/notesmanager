package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class ControllerCategories extends Controller {

    public ControllerCategories(String name, Controller previousController) {
        super(name,previousController);
    }

    @FXML
    ListView<String> categories_list;

    @FXML
    TextField newCategoryName;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        ObservableList<String> categories = FXCollections.observableArrayList(getCategories(false));
        categories_list.setItems(categories);
    }


    public void submitCategoryName(){
        newCategoryName.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String category = String.valueOf(newCategoryName.getCharacters());
                createCategory(category);
                try { goBack(); } catch (IOException e) {}
            }
        });

    }

    private void createCategory(String category) {
        System.out.println(category);
        String path = regexManager.categoryToPath(category);
        try { Files.createDirectory(Paths.get(path)); } catch (IOException e) { System.out.println("FAILED to create category."); }
    }


}
