package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerPrimary extends Controller implements Initializable{

    public ControllerPrimary(String name, Stage stage) {
        this.name = name;
        this.previousController = this;
        Controller.stageMaster = new StageMaster(stage); //One and only stageMaster
    }

    @FXML
    TreeView<File> FilesView; // FilesTreeView

    @FXML
    TextArea DisplayFileText;

    @FXML
    TextArea DisplayTitle;

    @Override //run on start
    public void initialize(URL url, ResourceBundle resourceBundle){
        FilesTreeView  filesTreeViewClass = new FilesTreeView(); //call FilesTreeView constructor
        List<String> categories = getCategories(true);
        List<TreeItem<File>> roots = new LinkedList<>();

        for(String categoryName : categories){
            roots.add(filesTreeViewClass.createNode(new File(categoryName)));
        }

        TreeItem<File> connectRoots = new TreeItem<>(null);
        connectRoots.getChildren().addAll(roots);
        FilesView.setRoot(connectRoots);
        FilesView.setShowRoot(false);
    }


    public void clickCategoryControllerButton() throws IOException {
        //Creating new ControllerCategory and loading it
        Controller.stageMaster.loadNewScene(new ControllerCategory("CategoryClass.fxml", this));
    }

    public void displayFile(){
        FilesView.setOnMouseClicked(mouseEvent -> {
            TreeItem<File> item = FilesView.getSelectionModel().getSelectedItem();
            if(item != null && item.getValue().isFile()){ //if clicked on file display content
                List<String> lines = new ArrayList<>();
                String line;
                try {
                    BufferedReader br = new BufferedReader(new FileReader(item.getValue()));
                    while ((line = br.readLine()) != null) {
                        lines.add(line);
                    }
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                displayTitle(item.getValue().getName());
                DisplayFileText.setText("");
                for(String tmp: lines){
                    DisplayFileText.appendText(tmp + "\n");
                }
            }
        });
    }

    public void displayTitle(String name){
        DisplayTitle.setText(regexManager.convertNameToReadable(name));
    }

}