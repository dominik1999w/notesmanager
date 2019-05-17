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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerCategories extends Controller {

    public ControllerCategories(String name, Controller previousController) {
        super(name,previousController);
    }

    @FXML
    ListView<String> categoriesList;
    @FXML
    TextField newCategoryName;
    @FXML
    Button removeCategory;
    @FXML
    Button back;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        ObservableList<String> categories = FXCollections.observableArrayList(getCategories(false));
        categoriesList.setItems(categories);
        categoriesList.setOnMouseClicked(mouseEvent -> {
            if(categoriesList.getSelectionModel().getSelectedItems().get(0) != null) {
                removeCategory.setDisable(false);
            }
        });

        Buttons buttons = new Buttons();
        back.setGraphic(buttons.setButton("back"));
        removeCategory.setGraphic(buttons.setButton("remove"));
    }


    public void submitCategoryName(){
        newCategoryName.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String category = String.valueOf(newCategoryName.getCharacters());
                createCategory(category);
            }
        });

    }

    private void createCategory(String category) {
        try{
            Controller.stageMaster.loadNewScene(new ControllerAreYouSure("/Scenes/AreYouSure.fxml", this,"newCategory",category));
        } catch (NullPointerException e){
            System.out.println("You can't create nothing. ;)");
        } catch (IOException e) {
            System.out.println("FAILED to create category: " + category);
            e.printStackTrace();
        }
    }

    public void removeCategory(){
        String path = RegexManager.categoryToPath(categoriesList.getSelectionModel().getSelectedItem());
        File category = new File(path);


        try{
            Controller.stageMaster.loadNewScene(new ControllerAreYouSure("/Scenes/AreYouSure.fxml", this,"removeCategory",category));
        } catch (NullPointerException e){
            System.out.println("You can't remove nothing. ;)");
        } catch (IOException e) {
            System.out.println("FAILED to remove category" + category);
        }
    }

}
