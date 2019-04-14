package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller extends TreeItem<File> implements Initializable{
    Stage previousStage;
    public void setPreviousStage(Stage stage){
        previousStage=stage;
    }
    @FXML
    TreeView<File> FilesView; // FilesTreeView
    @FXML
    Button newFileButton;
    @FXML
    TextArea DisplayFileText;
    public void clickCategoryControllerButton() throws IOException {
        Stage stage = new Stage();
        Pane myPane = FXMLLoader.load(getClass().getResource("CategoryClass.fxml"));
        Scene scene = new Scene(myPane);
        stage.setScene(scene);
        previousStage.close();
        stage.show();
    }

    @Override //run on start
    public void initialize(URL url, ResourceBundle resourceBundle){
        FilesTreeView  filesTreeViewClass = new FilesTreeView(); //call FilesTreeView constructor
        TreeItem<File> root = filesTreeViewClass.createNode(new File("categories/a")); //get files from a
        TreeItem<File> root2 = filesTreeViewClass.createNode(new File("categories/b")); //get files from b
        TreeItem<File> root3 = filesTreeViewClass.createNode(new File("categories/c")); //get files from c
        TreeItem<File> connectRoots = new TreeItem<>(null);
        connectRoots.getChildren().addAll(root,root2,root3);
        FilesView.setRoot(connectRoots);
        FilesView.setShowRoot(false);
    }
    public void displayFile(){
        FilesView.setOnMouseClicked(mouseEvent -> {
            TreeItem<File> item = FilesView.getSelectionModel().getSelectedItem();
            if(item.getValue().isFile()){ //if clicked on file display content
                List<String> lines= new ArrayList<>();
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
                DisplayFileText.setText("");
                for(String tmp: lines){
                    DisplayFileText.appendText(tmp+"\n");
                }
            }
        });
    }
}